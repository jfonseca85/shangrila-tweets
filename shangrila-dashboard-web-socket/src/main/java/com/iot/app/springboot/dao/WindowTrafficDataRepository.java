package com.iot.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.app.springboot.dao.entity.WindowTrafficData;

/**
 * DAO class for window_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface WindowTrafficDataRepository extends CassandraRepository<WindowTrafficData, String>{
	
	@Query("SELECT * FROM traffickeyspace.window_traffic WHERE recorddate = ?0 ALLOW FILTERING")
	 Iterable<WindowTrafficData> findTrafficDataByDate(String date);

}
