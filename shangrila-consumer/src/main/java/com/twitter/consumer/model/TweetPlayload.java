package com.twitter.consumer.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TweetPlayload {
	
	private String name;

	private String mensagem;
	
	private int seguidores;
	
	private long dataTweet;
	
	private String idioma;
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(int seguidores) {
		this.seguidores = seguidores;
	}

	public long getDataTweet() {
		return dataTweet;
	}

	public void setDataTweet(long dataTweet) {
		this.dataTweet = dataTweet;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	public static TweetPlayload builder (String tweetPlayload) {
	    ObjectMapper mapper = new ObjectMapper();
	    TweetPlayload builder = new TweetPlayload();
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			builder = mapper.readValue(tweetPlayload, TweetPlayload.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return builder;
	}

	@Override
	public String toString() {
		return "TweetPlayload [name=" + name + ", mensagem=" + mensagem + ", seguidores=" + seguidores + ", dataTweet="
				+ dataTweet + ", idioma=" + idioma + "]";
	}
}
