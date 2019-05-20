package com.baeldung.dao;

import com.baeldung.dao.entity.TotalTweetsFollwersTrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * DAO class for tweetsfollwers_trafic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTweetsFollwersTrafficDataRepository extends CassandraRepository<TotalTweetsFollwersTrafficData, String> {

	 @Query("SELECT * FROM traffickeyspace.tweetsfollwers_trafic LIMIT 10")
	 Iterable<TotalTweetsFollwersTrafficData> findTrafficDataByDate();
}
