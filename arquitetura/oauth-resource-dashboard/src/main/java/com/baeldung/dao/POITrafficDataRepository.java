package com.baeldung.dao;


import com.baeldung.dao.entity.POITrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * DAO class for poi_traffic
 *
 * @author jfonseca
 */
@Repository
public interface POITrafficDataRepository extends CassandraRepository<POITrafficData, Date> {

}
