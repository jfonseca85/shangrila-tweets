package com.tweet.producer.model;

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
}
