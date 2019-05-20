package com.tweet.app.spark.processor;

import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.google.common.base.Optional;
import com.iot.app.spark.util.GeoDistanceCalculator;
import com.tweet.app.spark.entity.POITrafficData;
import com.tweet.app.spark.entity.TotalTweetsFollwersTrafficData;
import com.tweet.app.spark.entity.TotalTweetsUsuarioTrafficData;
import com.tweet.app.spark.entity.WindowTweetTrafficData;
import com.tweet.app.spark.vo.AggregateUser;
import com.tweet.app.spark.vo.IoTData;
import com.tweet.app.spark.vo.POIData;
import com.tweet.app.spark.vo.TweetPlayload;

import scala.Tuple2;
import scala.Tuple3;

/**
 * Class to process IoT data stream and to produce traffic data details.
 * 
 * @author jfonseca
 *
 */
public class TweetTrafficDataProcessor {
	private static final Logger logger = Logger.getLogger(TweetTrafficDataProcessor.class);

	/**
	 * Method to get total traffic counts of different type of vehicles for each route.
	 * 
	 * @param filteredIotDataStream IoT data stream
	 */
	public void processTotalTrafficData(JavaDStream<TweetPlayload> filteredIotDataStream) {

		// We need to get count of vehicle group by routeId and vehicleType
		JavaPairDStream<AggregateUser, Long> countDStreamPair = filteredIotDataStream
				.mapToPair(iot -> new Tuple2<>(new AggregateUser(iot.getName(), iot.getIdioma()), 1L))
				.reduceByKey((a, b) -> a + b);
		
		// Need to keep state for total count
		JavaMapWithStateDStream<AggregateUser, Long, Long, Tuple2<AggregateUser, Long>> countDStreamWithStatePair = countDStreamPair
				.mapWithState(StateSpec.function(totalSumFunc).timeout(Durations.seconds(3600)));//maintain state for one hour

		// Transform to dstream of TrafficData
		JavaDStream<Tuple2<AggregateUser, Long>> countDStream = countDStreamWithStatePair.map(tuple2 -> tuple2);
		JavaDStream<TotalTweetsUsuarioTrafficData> trafficDStream = countDStream.map(totalTrafficDataFunc);

		// Map Cassandra table column
		Map<String, String> columnNameMappings = new HashMap<String, String>();
		columnNameMappings.put("name", "name");
		columnNameMappings.put("mensagem", "mensagem");
		columnNameMappings.put("idioma", "idioma");
		columnNameMappings.put("totalCount", "totalcount");
		columnNameMappings.put("timeStamp", "timestamp");
		columnNameMappings.put("recordDate", "recorddate");

		// call CassandraStreamingJavaUtil function to save in DB
		javaFunctions(trafficDStream).writerBuilder("traffickeyspace", "tweetsusuario_trafic",
				CassandraJavaUtil.mapToRow(TotalTweetsUsuarioTrafficData.class, columnNameMappings)).saveToCassandra();
	}
	
	/**
	 * Method to get total traffic counts of different type of vehicles for each route.
	 * 
	 * @param filteredIotDataStream IoT data stream
	 */
	public void processTotalFollowersTrafficData(JavaDStream<TweetPlayload> filteredIotDataStream) {

		// We need to get count of vehicle group by routeId and vehicleType
		JavaPairDStream<String, Long> countDStreamPair = filteredIotDataStream
				.mapToPair(iot -> new Tuple2<>(iot.getName(), Long.parseLong(iot.getSeguidores()+"")))
				.reduceByKey((a, b) -> a + b);
		
		// Need to keep state for total count
		JavaMapWithStateDStream<String, Long, Long, Tuple2<String, Long>> countDStreamWithStatePair = countDStreamPair
				.mapWithState(StateSpec.function(totalFollowersSumFunc).timeout(Durations.seconds(3600)));//maintain state for one hour

		// Transform to dstream of TrafficData
		JavaDStream<Tuple2<String, Long>> countDStream = countDStreamWithStatePair.map(tuple2 -> tuple2);
		JavaDStream<TotalTweetsFollwersTrafficData> trafficDStream = countDStream.map(totalFollowerTrafficDataFunc);

		// Map Cassandra table column
		Map<String, String> columnNameMappings = new HashMap<String, String>();
		columnNameMappings.put("name", "name");
		columnNameMappings.put("totalCount", "totalcount");
		columnNameMappings.put("timeStamp", "timestamp");
		columnNameMappings.put("recordDate", "recorddate");

		// call CassandraStreamingJavaUtil function to save in DB
		javaFunctions(trafficDStream).writerBuilder("traffickeyspace", "tweetsfollwers_trafic",
				CassandraJavaUtil.mapToRow(TotalTweetsFollwersTrafficData.class, columnNameMappings)).saveToCassandra();
	}

	/**
	 * Method to get window traffic counts of different type of vehicles for each route.
	 * Window duration = 30 seconds and Slide interval = 10 seconds
	 * 
	 * @param filteredIotDataStream IoT data stream
	 */
	public void processWindowTrafficData(JavaDStream<TweetPlayload> filteredIotDataStream) {

		// reduce by key and window (30 sec window and 10 sec slide).
		JavaPairDStream<String, Long> countDStreamPair = filteredIotDataStream
				.mapToPair(iot -> new Tuple2<>("QuantidadeTweet", 1L))
				.reduceByKeyAndWindow((a, b) -> a + b, Durations.seconds(3600), Durations.seconds(10));

		// Transform to dstream of TrafficData
		JavaDStream<WindowTweetTrafficData> trafficDStream = countDStreamPair.map(windowTrafficDataFunc);

		// Map Cassandra table column
		Map<String, String> columnNameMappings = new HashMap<String, String>();
		columnNameMappings.put("routeId", "routeid");
		columnNameMappings.put("totalCount", "totalcount");
		columnNameMappings.put("timeStamp", "timestamp");
		columnNameMappings.put("recordDate", "recorddate");

		// call CassandraStreamingJavaUtil function to save in DB
		javaFunctions(trafficDStream).writerBuilder("traffickeyspace", "window_tweet_traffic",
				CassandraJavaUtil.mapToRow(WindowTweetTrafficData.class, columnNameMappings)).saveToCassandra();
	}

	/**
	 * Method to get the vehicles which are in radius of POI and their distance from POI.
	 * 
	 * @param nonFilteredIotDataStream original IoT data stream
	 * @param broadcastPOIValues variable containing POI coordinates, route and vehicle types to monitor.
	 */
	public void processPOIData(JavaDStream<IoTData> nonFilteredIotDataStream,Broadcast<Tuple3<POIData, String, String>> broadcastPOIValues) {
		 
		// Filter by routeId,vehicleType and in POI range
		JavaDStream<IoTData> iotDataStreamFiltered = nonFilteredIotDataStream
				.filter(iot -> (iot.getRouteId().equals(broadcastPOIValues.value()._2())
						&& iot.getVehicleType().contains(broadcastPOIValues.value()._3())
						&& GeoDistanceCalculator.isInPOIRadius(Double.valueOf(iot.getLatitude()),
								Double.valueOf(iot.getLongitude()), broadcastPOIValues.value()._1().getLatitude(),
								broadcastPOIValues.value()._1().getLongitude(),
								broadcastPOIValues.value()._1().getRadius())));

		// pair with poi
		JavaPairDStream<IoTData, POIData> poiDStreamPair = iotDataStreamFiltered
				.mapToPair(iot -> new Tuple2<>(iot, broadcastPOIValues.value()._1()));

		// Transform to dstream of POITrafficData
		JavaDStream<POITrafficData> trafficDStream = poiDStreamPair.map(poiTrafficDataFunc);

		// Map Cassandra table column
		Map<String, String> columnNameMappings = new HashMap<String, String>();
		columnNameMappings.put("vehicleId", "vehicleid");
		columnNameMappings.put("distance", "distance");
		columnNameMappings.put("vehicleType", "vehicletype");
		columnNameMappings.put("timeStamp", "timestamp");

		// call CassandraStreamingJavaUtil function to save in DB
		javaFunctions(trafficDStream)
				.writerBuilder("traffickeyspace", "poi_traffic",CassandraJavaUtil.mapToRow(POITrafficData.class, columnNameMappings))
				.withConstantTTL(120)//keeping data for 2 minutes
				.saveToCassandra();
	}
	
	//Function to get running sum by maintaining the state
	private static final Function3<AggregateUser, Optional<Long>, State<Long>,Tuple2<AggregateUser,Long>> totalSumFunc = (key,currentSum,state) -> {
        long totalSum = currentSum.or(0L) + (state.exists() ? state.get() : 0);
        Tuple2<AggregateUser, Long> total = new Tuple2<>(key, totalSum);
        state.update(totalSum);
        return total;
    };
    
	//Function to get running sum by maintaining the state
	private static final Function3<String, Optional<Long>, State<Long>,Tuple2<String, Long>> totalFollowersSumFunc = (key,currentSum,state) -> {
        long totalSum = currentSum.or(0L) + (state.exists() ? state.get() : 0);
        Tuple2<String, Long> total = new Tuple2<>(key, totalSum);
        state.update(totalSum);
        return total;
    };
    
    
    //Function to create TotalTrafficData object from IoT data
    private static final Function<Tuple2<String, Long>, TotalTweetsFollwersTrafficData> totalFollowerTrafficDataFunc = (tuple -> {
		logger.info("Total Count : " + "Key " + tuple._1() + " value "+ tuple._2());
		TotalTweetsFollwersTrafficData trafficData = new TotalTweetsFollwersTrafficData();
		trafficData.setName(tuple._1());
		trafficData.setTotalCount(tuple._2());
		trafficData.setTimeStamp(new Date());
		trafficData.setRecordDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return trafficData;
	});
    
    
    //Function to create TotalTrafficData object from IoT data
    private static final Function<Tuple2<AggregateUser, Long>, TotalTweetsUsuarioTrafficData> totalTrafficDataFunc = (tuple -> {
		logger.debug("Total Count : " + "Key " + tuple._1().getName() + "-"  + tuple._1().getIdioma()+ " value "+ tuple._2());
		TotalTweetsUsuarioTrafficData trafficData = new TotalTweetsUsuarioTrafficData();
		trafficData.setName(tuple._1().getName());
		trafficData.setIdioma(tuple._1.getIdioma());
		trafficData.setMensagem("Mensagem Hoje");
		trafficData.setTotalCount(tuple._2());
		trafficData.setTimeStamp(new Date());
		trafficData.setRecordDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return trafficData;
	});
    
    //Function to create WindowTrafficData object from IoT data
    private static final Function<Tuple2<String, Long>, WindowTweetTrafficData> windowTrafficDataFunc = (tuple -> {
		logger.debug("Window Count : " + "key " + tuple._1() +  " value " + tuple._2());
		WindowTweetTrafficData trafficData = new WindowTweetTrafficData();
		trafficData.setRouteId(tuple._1());
		trafficData.setTotalCount(tuple._2());
		trafficData.setTimeStamp(new Date());
		trafficData.setRecordDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return trafficData;
	});
    
    //Function to create POITrafficData object from IoT data
    private static final Function<Tuple2<IoTData, POIData>, POITrafficData> poiTrafficDataFunc = (tuple -> {
		POITrafficData poiTraffic = new POITrafficData();
		poiTraffic.setVehicleId(tuple._1.getVehicleId());
		poiTraffic.setVehicleType(tuple._1.getVehicleType());
		poiTraffic.setTimeStamp(new Date());
		double distance = GeoDistanceCalculator.getDistance(Double.valueOf(tuple._1.getLatitude()).doubleValue(),
				Double.valueOf(tuple._1.getLongitude()).doubleValue(), tuple._2.getLatitude(), tuple._2.getLongitude());
		logger.debug("Distance for " + tuple._1.getLatitude() + "," + tuple._1.getLongitude() + ","+ tuple._2.getLatitude() + "," + tuple._2.getLongitude() + " = " + distance);
		poiTraffic.setDistance(distance);
		return poiTraffic;
	});

}
