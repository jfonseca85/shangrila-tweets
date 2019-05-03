package com.iot.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.app.springboot.dao.entity.WindowTweetTrafficData;

/**
 * DAO class for window_tweet_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface WindowTweetTrafficDataRepository extends CassandraRepository<WindowTweetTrafficData, String>{
	
	@Query("SELECT * FROM traffickeyspace.window_tweet_traffic WHERE recorddate = ?0 ALLOW FILTERING")
	 Iterable<WindowTweetTrafficData> findTrafficDataByDate(String date);

}
