package com.iot.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.app.springboot.dao.entity.TotalTweetsFollwersTrafficData;

/**
 * DAO class for tweetsfollwers_trafic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTweetsFollwersTrafficDataRepository extends CassandraRepository<TotalTweetsFollwersTrafficData, String>{

	 @Query("SELECT * FROM traffickeyspace.tweetsfollwers_trafic WHERE recorddate = ?0 ALLOW FILTERING")
	 Iterable<TotalTweetsFollwersTrafficData> findTrafficDataByDate(String date);	 
}
