package com.baeldung.dao;

import com.baeldung.dao.entity.WindowTrafficData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * DAO class for window_traffic
 *
 * @author jfonseca
 */
@Repository
public interface WindowTrafficDataRepository extends CassandraRepository<WindowTrafficData, String> {

    @Query("SELECT * FROM traffickeyspace.window_traffic LIMIT 10")
    Iterable<WindowTrafficData> findTrafficDataByDate(String date);

}
