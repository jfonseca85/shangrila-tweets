package com.twitter.producer.service;import com.fasterxml.jackson.core.JsonProcessingException;import com.fasterxml.jackson.databind.ObjectMapper;import com.twitter.producer.config.KafkaProducer;import com.twitter.producer.config.KafkaProperties;import com.twitter.producer.model.TweetPlayload;import com.twitter.producer.service.utils.TwitterStringsUtils;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.social.twitter.api.*;import org.springframework.stereotype.Service;import java.util.ArrayList;import java.util.Date;import java.util.List;import java.util.Set;@Servicepublic class TwitterStreamingService {	private final Logger log = LoggerFactory.getLogger(TwitterStreamingService.class);	private final Twitter twitter;	private final KafkaProperties kafkaProperties;	private final KafkaProducer kafkaProducerService;	public TwitterStreamingService(Twitter twitter, KafkaProperties kafkaProperties,			KafkaProducer kafkaProducerService) {		this.twitter = twitter;		this.kafkaProperties = kafkaProperties;		this.kafkaProducerService = kafkaProducerService;	}	public void stream() {		List<StreamListener> listeners = new ArrayList<>();		StreamListener streamListener = new StreamListener() {			@Override			public void onTweet(Tweet tweet) {				TweetPlayload playload = new TweetPlayload();				playload.setIdioma(tweet.getLanguageCode());//				playload.setMensagem(tweet.getText());				playload.setDataTweet(tweet.getCreatedAt().toInstant().toEpochMilli());				playload.setName(tweet.getUser().getName());				playload.setSeguidores(tweet.getUser().getFollowersCount());													String tweetText = tweet.getText();				// filter non-English tweets://                if (!"en".equals(tweetLanguageCode)) {//                    return;//                }				Set<String> hashTags = TwitterStringsUtils.tweetToHashTags(tweetText);				// filter tweets without hashTags:				if (hashTags.isEmpty()) {					return;				}								playload.setMensagem(tweet.getText());				// logging Real Time Tweets				log.info("User '{}', Tweeted : {}, from ; {}", tweet.getUser().getName(), tweet.getText(),						tweet.getUser().getLocation());				try {					kafkaProducerService.send(kafkaProperties.getTemplate().getDefaultTopic(), new ObjectMapper().writeValueAsString(playload));				} catch (JsonProcessingException e) {					e.printStackTrace();				}			}			@Override			public void onDelete(StreamDeleteEvent streamDeleteEvent) {			}			@Override			public void onLimit(int i) {			}			@Override			public void onWarning(StreamWarningEvent streamWarningEvent) {			}		};		// Start Stream when run a service		listeners.add(streamListener);		twitter.streamingOperations().sample(listeners);	}}