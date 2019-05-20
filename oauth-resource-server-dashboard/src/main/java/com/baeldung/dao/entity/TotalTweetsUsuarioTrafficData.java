package com.baeldung.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity class for total_traffic db table
 * 
 * @author jfonseca
 *
 */
@Table("tweetsusuario_trafic")
public class TotalTweetsUsuarioTrafficData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4556456502999827670L;

	@PrimaryKeyColumn(name = "name", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String name;

	@PrimaryKeyColumn(name = "mensagem", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String mensagem;

	@PrimaryKeyColumn(name = "idioma", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String idioma;

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


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Override
	public String toString() {
		return "TotalTweetsUsuarioTrafficData [name=" + name + ", mensagem=" + mensagem + ", idioma=" + idioma
				+ ", recordDate=" + recordDate + ", totalCount=" + totalCount + ", timeStamp=" + timeStamp + "]";
	}

}
