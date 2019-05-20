package com.baeldung.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * Spring bean configuration for Cassandra db.
 * 
 * @author jfonseca
 *
 */
@Configuration
@PropertySource(value = {"classpath:iot-springboot.properties"})
@EnableCassandraRepositories(basePackages = {"com.iot.app.springboot.dao"})
public class CassandraConfig extends org.springframework.data.cassandra.config.AbstractCassandraConfiguration{
	
    @Autowired
    private Environment environment;
    
    
    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(environment.getProperty("com.iot.app.cassandra.host"));
        cluster.setPort(Integer.parseInt(environment.getProperty("com.iot.app.cassandra.port")));
        cluster.setMetricsEnabled(false);
        cluster.setJmxReportingEnabled(false);
        return cluster;
    }
  
    
	@Override
	@Bean
	protected String getKeyspaceName() {
		return environment.getProperty("com.iot.app.cassandra.keyspace");
	}
}
