package com.twitter.consumer.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.twitter.consumer.config.KafkaConsumerConfig;
import com.twitter.consumer.config.KafkaProperties;
import com.twitter.consumer.model.TweetPlayload;
import com.twitter.consumer.util.HashTagsUtils;

import scala.Tuple2;

@Service
public class SparkConsumerService {

	private final Logger log = LoggerFactory.getLogger(SparkConsumerService.class);

	private final SparkConf sparkConf;

	private final KafkaConsumerConfig kafkaConsumerConfig;

	private final KafkaProperties kafkaProperties;

	private final Collection<String> topics;

	public SparkConsumerService(SparkConf sparkConf, KafkaConsumerConfig kafkaConsumerConfig,
			KafkaProperties kafkaProperties) {
		this.sparkConf = sparkConf;
		this.kafkaConsumerConfig = kafkaConsumerConfig;
		this.kafkaProperties = kafkaProperties;
		this.topics = Arrays.asList(kafkaProperties.getTemplate().getDefaultTopic());
	}

	public void run() {
		log.debug("Running Spark Consumer Service..");

		// Create context with a 10 seconds batch interval
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));
		createConection(sparkConf);

		// Create direct kafka stream with brokers and topics
		JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(jssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.Subscribe(topics, kafkaConsumerConfig.consumerConfigs()));

//        //Coleta de seguidores por Usuarios para as amostras
		JavaDStream<String> lines = messages.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());
		log.info("<<<<<<<<<<<<< INICIO - Salvando do playLoad no Banco >>>>>>>>>>>>>>>>>>>>>");
		lines.flatMap(text -> HashTagsUtils.hashTagsFromTweetAmostras(text)).foreachRDD(rrdd -> {
			// Counts
			rrdd.collect().forEach(record -> {
				TweetPlayload tweetPlayload = new TweetPlayload();
				tweetPlayload = TweetPlayload.builder(record);
				log.info("Salvando do playLoad no Banco >>>> : {} ", tweetPlayload.toString());
			});
		});
		log.info("<<<<<<<<<<<<< FIM - Salvando do playLoad no Banco >>>>>>>>>>>>>>>>>>>>>");

		// Coleta de seguidores por Usuarios para as amostras
		log.info(
				"<<<<<<<<<<<<< INICIO - Salvando do playLoad Tweets por Usuario, hashTag and Idioma >>>>>>>>>>>>>>>>>>>>>");
		lines.flatMap(hashTagsByUserAndLanguage -> HashTagsUtils.hashTagsByUserAndLanguage(hashTagsByUserAndLanguage)) // Listar
																														// os
																														// Tweets
																														// por
																														// usuario
				.mapToPair(hashTag -> new Tuple2<>(hashTag, 1)).reduceByKey((a, b) -> Integer.sum(a, b))
				.mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap()).foreachRDD(rrdd -> {
					// Counts
					rrdd.sortByKey(false).collect().forEach(record -> {
						log.info("Agrupando pelo usuario: <<<<<<<:>>>>>>>>> "
								+ String.format(" %s (%d)", record._2, record._1));
					});
				});
		log.info(
				"<<<<<<<<<<<<< FIM - Salvando do playLoad Tweets por Usuario, hashTag and Idioma >>>>>>>>>>>>>>>>>>>>>");

		log.info("<<<<<<<<<<<<< INICIO - Agrupando por Hora >>>>>>>>>>>>>>>>>>>>>");
		JavaDStream<String> wordCounts = lines.flatMap(text -> HashTagsUtils.hashTagsFromTweet(text));
		wordCounts.print();
		wordCounts.window(Durations.seconds(60), Durations.seconds(10)).countByValue()
				.foreachRDD(tRDD -> tRDD.foreach(x -> System.out
						.println(new Date() + " ::The window count  60 segundos, remove depois de 5 segundos tag is ::"
								+ x._1() + " and the val is ::" + x._2())));
		log.info("<<<<<<<<<<<<< FIM - Agrupando por Horas >>>>>>>>>>>>>>>>>>>>>");

		// Start the computation
		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			log.error("Interrupted: {}", e);
			// Restore interrupted state...
		}
	}

	private void createConection() {
		CassandraConnector connector = CassandraConnector.apply(sparkConf);

		// Prepare the schema
		try (Session session = connector.openSession()) {
			session.execute("DROP KEYSPACE IF EXISTS java_api");
			session.execute("CREATE KEYSPACE java_api WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
			session.execute("CREATE TABLE java_api.products (id INT PRIMARY KEY, name TEXT, parents LIST<INT>)");
			session.execute("CREATE TABLE java_api.sales (id UUID PRIMARY KEY, product INT, price DECIMAL)");
			session.execute("CREATE TABLE java_api.summaries (product INT PRIMARY KEY, summary DECIMAL)");
		}

	}

}
