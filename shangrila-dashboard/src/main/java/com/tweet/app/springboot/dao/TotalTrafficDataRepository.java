package com.tweet.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweet.app.springboot.dao.entity.TotalTrafficData;

/**
 * DAO class for total_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTrafficDataRepository extends CassandraRepository<TotalTrafficData, String>{

	 @Query("SELECT * FROM traffickeyspace.total_traffic WHERE recorddate = ?0 ALLOW FILTERING")
	 Iterable<TotalTrafficData> findTrafficDataByDate(String date);	 
}
