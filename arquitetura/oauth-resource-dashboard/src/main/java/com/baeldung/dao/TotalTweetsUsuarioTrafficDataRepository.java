package com.baeldung.dao;

import com.baeldung.dao.entity.TotalTweetsUsuarioTrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * DAO class for tweetsusuario_trafic
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTweetsUsuarioTrafficDataRepository
		extends CassandraRepository<TotalTweetsUsuarioTrafficData, String> {

	@Query("SELECT * FROM traffickeyspace.tweetsusuario_trafic LIMIT 10")
	Iterable<TotalTweetsUsuarioTrafficData> findTrafficDataByDate();
}
