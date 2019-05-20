package com.baeldung.dao;

import com.baeldung.dao.entity.WindowTweetTrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * DAO class for window_tweet_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface WindowTweetTrafficDataRepository extends CassandraRepository<WindowTweetTrafficData, String> {
	
	@Query("SELECT * FROM traffickeyspace.window_tweet_traffic LIMIT 10")
	 Iterable<WindowTweetTrafficData> findTrafficDataByDate();

}
