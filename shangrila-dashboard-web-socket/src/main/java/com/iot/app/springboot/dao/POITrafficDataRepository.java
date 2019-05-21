package com.iot.app.springboot.dao;

import java.util.Date;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.iot.app.springboot.dao.entity.POITrafficData;

/**
 * DAO class for poi_traffic 
 * 
 * @author jfonseca
 *
 */
@Repository
public interface POITrafficDataRepository extends CassandraRepository<POITrafficData, Date>{
	 
}