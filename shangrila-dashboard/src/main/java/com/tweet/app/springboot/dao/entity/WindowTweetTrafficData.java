package com.tweet.app.springboot.dao.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity class for window_tweet_traffic db table
 * 
 * @author jfonseca
 *
 */
@Table("window_tweet_traffic")
public class WindowTweetTrafficData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8093406759833548960L;

	@PrimaryKeyColumn(name = "routeid",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	private String routeId;
	
	@PrimaryKeyColumn(name = "recordDate",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
	private String recordDate;
	
	@Column(value = "totalcount")
	private long totalCount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="MST")
	@Column(value = "timestamp")
	private Date timeStamp;
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "WindowTweetTrafficData [routeId=" + routeId + ", recordDate=" + recordDate + ", totalCount="
				+ totalCount + ", timeStamp=" + timeStamp + "]";
	}

	
	
	
}
