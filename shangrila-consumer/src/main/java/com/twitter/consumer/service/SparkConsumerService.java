package com.twitter.consumer.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.twitter.consumer.config.KafkaConsumerConfig;
import com.twitter.consumer.config.KafkaProperties;
import com.twitter.consumer.model.NameUserTweet;
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

    public SparkConsumerService(SparkConf sparkConf, KafkaConsumerConfig kafkaConsumerConfig, KafkaProperties kafkaProperties) {
        this.sparkConf = sparkConf;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProperties = kafkaProperties;
        this.topics = Arrays.asList(kafkaProperties.getTemplate().getDefaultTopic());
    }

    public void run() {
        log.debug("Running Spark Consumer Service..");

        // Create context with a 10 seconds batch interval
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaConsumerConfig.consumerConfigs()));
        

		JavaPairDStream<String, String> pairRDD = messages.mapToPair(record-> new Tuple2<>(record.key(), record.value()));
        pairRDD.foreachRDD(pRDD-> { pRDD.foreach(tuple-> System.out.println(new Date()+" :: Kafka msg key ::"+tuple._1() +" the val is ::"+tuple._2()));});
        
        JavaDStream<String> tweetRDD = pairRDD.map(x-> x._2()).map(new NameUserTweet());
//      tweetRDD.foreachRDD(tRDD -> tRDD.foreach(x->System.out.println(new Date()+" <<<<<<<::>>>>>> "+x)));
        
        //Coleta de seguidores por Usuarios para as amostras
        JavaDStream<String> lines = messages.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());
	    lines
	    	 .flatMap(text -> HashTagsUtils.hashTagsFromTweetAmostras(text))
	         .foreachRDD(rrdd -> {
	             log.info("<<<<<<<<<<<<< Salvando do playLoad no Banco >>>>>>>>>>>>>>>>>>>>>");
	             //Counts
	             rrdd.collect()
	                     .forEach(record -> {
	                    	 TweetPlayload tweetPlayload = new TweetPlayload();
	                    	 tweetPlayload = TweetPlayload.builder(record);
	                    	 log.info("Salvando do playLoad no Banco >>>> : {} ", tweetPlayload.toString());
	             });
	         });
        
	    
        //Agrupando pelo usuario: <<<<<<<:>>>>>>>>>
        tweetRDD.flatMap(text -> HashTagsUtils.hashTagsFromTweet(text))
             .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
             .reduceByKey((a, b) -> Integer.sum(a, b))
             .mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap())
                .foreachRDD(rrdd -> {
                    log.info("---------------------------------------------------------------");
                    //Counts
                    rrdd.sortByKey(false).collect()
                            .forEach(record -> {
                                log.info("Agrupando pelo usuario: <<<<<<<:>>>>>>>>> "+String.format(" %s (%d)", record._2, record._1));
                    });
                });
                
        // Get the lines, split them into words, count the words and print
//        JavaDStream<String> lines = messages.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());
        

//        Duration.apply(arg0);
		//Quantidade de Tweets for hora
        JavaDStream<Long> countByWindow = lines.countByWindow(Durations.seconds(30), Durations.seconds(60));
        log.info("Quantidade de Tweets por minuto:>>>>>>>>>>>> [{}]", countByWindow);
        
        //Count the tweets and print
        lines.count()
             .map(cnt -> "Popular hash tags in last 60 seconds (" + cnt + " total tweets):")
             .print();

        //
//        lines.flatMap(text -> HashTagsUtils.hashTagsFromTweet(text))
//             .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
//             .reduceByKey((a, b) -> Integer.sum(a, b))
//             .mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap())
//                .foreachRDD(rrdd -> {
//                    log.info("---------------------------------------------------------------");
//                    //Counts
//                    rrdd.sortByKey(false).collect()
//                            .forEach(record -> {
//                                log.info(String.format(" %s (%d)", record._2, record._1));
//                    });
//                });

        // Start the computation
        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            log.error("Interrupted: {}", e);
            // Restore interrupted state...
        }
    }
}