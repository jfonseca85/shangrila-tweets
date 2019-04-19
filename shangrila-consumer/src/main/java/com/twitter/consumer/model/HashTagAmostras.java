/**
 * 
 */
package com.twitter.consumer.model;

/**
 * @author jorge
 *
 */
public enum HashTagAmostras {
	
	OPENBANKING("#openbanking"),
	APIFIRST ("#apifirst"),
	DEVOPS ("#devops"),
	CLOUDFIRST ("#cloudfirst"),
	MICROSERVICES ("#microservices"), 
	APIGATEWAY ("#apigateway"),
	OAUTH("#oauth"),
	SWAGGER("#swagger"),
	RAML("#raml"),
	OPENAPIS("#openapis");
	
	private final String hashTag;
	
	HashTagAmostras(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getHashTag() {
		return hashTag;
	}

}
