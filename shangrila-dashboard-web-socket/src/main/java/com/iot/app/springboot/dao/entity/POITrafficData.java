package com.iot.app.springboot.dao.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity class for poi_traffic db table
 * 
 * @author jfonseca
 *
 */
@org.springframework.data.cassandra.core.mapping.Table("poi_traffic")
public class POITrafficData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="MST")
	@org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn(name = "timeStamp",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	private Date timeStamp;
	@org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn(name = "recordDate",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
	private String recordDate;
	@org.springframework.data.cassandra.core.mapping.Column(value = "vehicleId")
	private String vehicleId;
	@Column(value = "distance")
	private double distance;
	@Column(value = "vehicleType")
	private String vehicleType;
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
}
