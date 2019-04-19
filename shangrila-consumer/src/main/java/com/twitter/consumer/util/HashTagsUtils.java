package com.twitter.consumer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twitter.consumer.model.HashTagAmostras;
import com.twitter.consumer.model.TweetPlayload;

public class HashTagsUtils {
	private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

	public static Iterator<String> hashTagsFromTweet(String text) {
		List<String> hashTags = fillMetaDataHashtag(text);
		return hashTags.iterator();
	}

	public static Iterator<String> hashTagsFromTweetAmostras(String text) {
		List<String> hashTags = new ArrayList<>();
		Matcher matcher = HASHTAG_PATTERN.matcher(text);
		while (matcher.find()) {
			String handle = matcher.group();
			for (HashTagAmostras hasTag : HashTagAmostras.values()) {
				if (hasTag.getHashTag().equalsIgnoreCase(handle)) {
					//TODO: Melhorar o Builder do TweetsPlayLoad
					hashTags.add(text.replace("#", ""));
				}
			}
		}
		return hashTags.iterator();
	}
	
	public static Iterator<String> hashTagsByUserAndLanguage(String text) {
		List<String> hashTags = new ArrayList<>();
		Matcher matcher = HASHTAG_PATTERN.matcher(text);
		
		while (matcher.find()) {
			String handle = matcher.group();
			for (HashTagAmostras hasTag : HashTagAmostras.values()) {
				if (hasTag.getHashTag().equalsIgnoreCase(handle)) {
					//TODO: Melhorar o Builder do TweetsPlayLoad		
					TweetPlayload playload = TweetPlayload.builder(text.replace("#", ""));
					hashTags.add(playload.hashTagByUserAndLanguage());
				}
			}
		}
		return hashTags.iterator();
	}

	/**
	 * @param text
	 * @return
	 */
	private static List<String> fillMetaDataHashtag(String text) {
		List<String> hashTags = new ArrayList<>();
		Matcher matcher = HASHTAG_PATTERN.matcher(text);
		while (matcher.find()) {
			String handle = matcher.group();
			hashTags.add(handle);
		}
		return hashTags;
	}
}
