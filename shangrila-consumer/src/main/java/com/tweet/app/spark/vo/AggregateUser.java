package com.tweet.app.spark.vo;

import java.io.Serializable;

/**
 * Key class for calculation
 * 
 * @author jfonseca
 *
 */
public class AggregateUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9145529317187885491L;

	private String name;

	private String mensagem;

	private String idioma;

	/**
	 * @param name
	 * @param mensagem
	 * @param idioma
	 */
	public AggregateUser(String name, String mensagem, String idioma) {
		super();
		this.name = name;
		this.mensagem = mensagem;
		this.idioma = idioma;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * @return the idioma
	 */
	public String getIdioma() {
		return idioma;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idioma == null) ? 0 : idioma.hashCode());
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregateUser other = (AggregateUser) obj;
		if (idioma == null) {
			if (other.idioma != null)
				return false;
		} else if (!idioma.equals(other.idioma))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
