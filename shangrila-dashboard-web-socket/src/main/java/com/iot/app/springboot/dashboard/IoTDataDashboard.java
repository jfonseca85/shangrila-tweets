package com.iot.app.springboot.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import brave.sampler.Sampler;

/**
 * Spring boot application class for Dashboard.
 * 
 * @author jfonseca
 *
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.iot.app.springboot.dashboard", "com.iot.app.springboot.dao" })
@EnableDiscoveryClient
public class IoTDataDashboard {
	public static void main(String[] args) {
		SpringApplication.run(IoTDataDashboard.class, args);
	}

	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}
