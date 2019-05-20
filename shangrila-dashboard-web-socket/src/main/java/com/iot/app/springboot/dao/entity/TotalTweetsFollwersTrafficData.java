package com.iot.app.springboot.dao.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Entity class for total_traffic db table
 * 
 * @author jfonseca
 *
 */
@Table("tweetsfollwers_trafic")
public class TotalTweetsFollwersTrafficData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4556456502999827670L;

	@PrimaryKeyColumn(name = "name", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String name;

	@PrimaryKeyColumn(name = "recordDate", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String recordDate;

	@Column(value = "totalcount")
	private long totalCount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "MST")
	@Column(value = "timestamp")
	private Date timeStamp;

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
		return "TotalTweetsFollwersTrafficData [name=" + name + ", recordDate=" + recordDate + ", totalCount="
				+ totalCount + ", timeStamp=" + timeStamp + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
