package com.twitter.consumer.model;

import java.io.Serializable;

import org.apache.spark.api.java.function.Function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class NameUserTweet implements Function<String, String>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String call(String tweet) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    JavaTimeModule module = new JavaTimeModule();
	    mapper.registerModule(module);
		try {
			JsonNode root = mapper.readValue(tweet, JsonNode.class);
//			if (root.get("lang") != null && "en".equals(root.get("lang").textValue())) {
//				if (root.get("name") != null && root.get("mensagem") != null) {
			System.out.println("the text is ::" + root.findValue("name").asText());
			return root.findValue("name").asText();
//				}
//				return null;
//			}
//			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
