package com.tweet.app.spark.vo;

import java.io.Serializable;

public class TweetPlayload implements Serializable{
	
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
	
	
	public String hashTagByUserAndLanguage() {
		return "TweetPlayload [name=" + name + ", mensagem=" + mensagem + ", idioma=" + idioma + "]";
	}
	
	
	@Override
	public String toString() {
		return "TweetPlayload [name=" + name + ", mensagem=" + mensagem + ", seguidores=" + seguidores + ", dataTweet="
				+ dataTweet + ", idioma=" + idioma + "]";
	}
}
