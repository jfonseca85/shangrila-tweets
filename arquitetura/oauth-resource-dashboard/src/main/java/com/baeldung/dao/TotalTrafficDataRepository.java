package com.baeldung.dao;

import com.baeldung.dao.entity.TotalTrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * DAO class for total_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface TotalTrafficDataRepository extends CassandraRepository<TotalTrafficData, String> {

	 @Query("SELECT * FROM traffickeyspace.total_traffic WHERE recorddate = ?0 ALLOW FILTERING")
	 Iterable<TotalTrafficData> findTrafficDataByDate(String date);
}
