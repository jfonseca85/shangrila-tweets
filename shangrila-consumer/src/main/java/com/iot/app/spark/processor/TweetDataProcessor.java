package com.iot.app.spark.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.base.Optional;
import com.iot.app.spark.util.HashTagsUtils;
import com.iot.app.spark.util.PropertyFileReader;
import com.iot.app.spark.util.TweetPlayloadDecoder;
import com.iot.app.spark.vo.TweetPlayload;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

/**
 * This class consumes Kafka IoT messages and creates stream for processing the
 * IoT data.
 * 
 * @author jfonseca
 *
 */
public class TweetDataProcessor {

	private static final Logger log = Logger.getLogger(TweetDataProcessor.class);

	public static void main(String[] args) throws Exception {
		// read Spark and Cassandra properties and create SparkConf
		Properties prop = PropertyFileReader.readPropertyFile();
		SparkConf conf = new SparkConf().setAppName(prop.getProperty("com.iot.app.spark.app.name"))
				.setMaster(prop.getProperty("com.iot.app.spark.master"))
				.set("spark.cassandra.connection.host", prop.getProperty("com.iot.app.cassandra.host"))
				.set("spark.cassandra.connection.port", prop.getProperty("com.iot.app.cassandra.port"))
				.set("spark.cassandra.connection.keep_alive_ms", prop.getProperty("com.iot.app.cassandra.keep_alive"));
		// batch interval of 5 seconds for incoming stream
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		// add check point directory
		jssc.checkpoint(prop.getProperty("com.iot.app.spark.checkpoint.dir"));

		// read and set Kafka properties
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("zookeeper.connect", prop.getProperty("com.iot.app.kafka.zookeeper"));
		kafkaParams.put("metadata.broker.list", prop.getProperty("com.iot.app.kafka.brokerlist"));
		String topic = prop.getProperty("com.iot.app.kafka.topic");
		Set<String> topicsSet = new HashSet<String>();
		topicsSet.add(topic);
		// create direct kafka stream
		JavaPairInputDStream<String, TweetPlayload> directKafkaStream = KafkaUtils.createDirectStream(jssc,
				String.class, TweetPlayload.class, StringDecoder.class, TweetPlayloadDecoder.class, kafkaParams,
				topicsSet);
		log.info("Starting Stream Processing");

		// We need non filtered stream for poi traffic data calculation
		JavaDStream<TweetPlayload> nonFilteredIotDataStream = directKafkaStream.map(tuple -> tuple._2());

		// We need filtered stream for total and traffic data calculation
		JavaPairDStream<String, TweetPlayload> iotDataPairStream = nonFilteredIotDataStream
				.mapToPair(iot -> new Tuple2<String, TweetPlayload>(iot.getMensagem(), iot)).reduceByKey((a, b) -> a);

		// Check vehicle Id is already processed
		JavaMapWithStateDStream<String, TweetPlayload, Boolean, Tuple2<TweetPlayload, Boolean>> iotDStreamWithStatePairs = iotDataPairStream
				.mapWithState(StateSpec.function(processedVehicleFunc).timeout(Durations.seconds(10)));// maintain  state for one hour

		// Filter processed vehicle ids and keep un-processed
		JavaDStream<Tuple2<TweetPlayload, Boolean>> filteredIotDStreams = iotDStreamWithStatePairs.map(tuple2 -> tuple2)
				.filter(tuple -> tuple._2.equals(Boolean.TRUE));

		// Get stream of IoTdata
		JavaDStream<TweetPlayload> filteredIotDataStream = filteredIotDStreams.map(tuple -> tuple._1);

		// cache stream as it is used in total and window based computation
		filteredIotDataStream.cache();
		nonFilteredIotDataStream.cache();

		// process data
		TweetTrafficDataProcessor trafficDataProcessor = new TweetTrafficDataProcessor();
		trafficDataProcessor.processTotalTrafficData(nonFilteredIotDataStream);
		trafficDataProcessor.processWindowTrafficData(nonFilteredIotDataStream);
		trafficDataProcessor.processTotalFollowersTrafficData(nonFilteredIotDataStream);

		// start context
		jssc.start();
		jssc.awaitTermination();
	}

	// Funtion to check processed vehicles.
	private static final Function3<String, Optional<TweetPlayload>, State<Boolean>, Tuple2<TweetPlayload, Boolean>> processedVehicleFunc = ( String, iot, state) -> {
		Tuple2<TweetPlayload, Boolean> vehicle = new Tuple2<>(iot.get(), false);
		
		Boolean hashTagsFromTweetAmostras = HashTagsUtils.hashTagsFromTweetAmostras(iot.get().getMensagem());
		if(hashTagsFromTweetAmostras) {
			System.out.println("HashTag das Amostras :  "+ iot.get().getMensagem());
			vehicle = new Tuple2<>(iot.get(), true);
		} 	
		
		return vehicle;
	};

}
