package com.tweet.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweet.app.springboot.dao.entity.TotalTweetsUsuarioTrafficData;

/**
 * DAO class for tweetsusuario_trafic
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTweetsUsuarioTrafficDataRepository
		extends CassandraRepository<TotalTweetsUsuarioTrafficData, String> {

	@Query("SELECT * FROM traffickeyspace.tweetsusuario_trafic WHERE recorddate = ?0 ALLOW FILTERING")
	Iterable<TotalTweetsUsuarioTrafficData> findTrafficDataByDate(String date);
}
