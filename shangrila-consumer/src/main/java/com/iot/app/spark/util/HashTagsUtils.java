package com.iot.app.spark.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iot.app.spark.vo.HashTagAmostras;
import com.iot.app.spark.vo.TweetPlayload;

public class HashTagsUtils {
	private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

	public static Iterator<String> hashTagsFromTweet(String text) {
		List<String> hashTags = fillMetaDataHashtag(text);
		return hashTags.iterator();
	}

	public static Iterator<String> anyashTagsFromTweet(String text) {
		List<String> hashTags = new ArrayList<>();
		try {
			Matcher matcher = HASHTAG_PATTERN.matcher(text);
			String handle = matcher.group();
			while (matcher.find()) {
				for (HashTagAmostras hasTag : HashTagAmostras.values()) {
					if (hasTag.getHashTag().equalsIgnoreCase(handle)) {
						// TODO: Melhorar o Builder do TweetsPlayLoad
						hashTags.add("QTD_TWEETS");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("WARN >>>> : {}" + text.toString());
		}
		return hashTags.iterator();
	}

	public static Boolean hashTagsFromTweetAmostras(String text) {
		Boolean result = false;
		try {
			Matcher matcher = HASHTAG_PATTERN.matcher(text);
			while (matcher.find()) {
				String handle = matcher.group();
				for (HashTagAmostras hasTag : HashTagAmostras.values()) {
					if (hasTag.getHashTag().equalsIgnoreCase(handle)) {
						// TODO: Melhorar o Builder do TweetsPlayLoad
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("WARN >>>> : {}" + text.toString());
		}
		return result;
	}

	public static Iterator<String> hashTagsByUserAndLanguage(TweetPlayload hashTagsByUserAndLanguage) {
		List<String> hashTags = new ArrayList<>();
		Matcher matcher = HASHTAG_PATTERN.matcher(hashTagsByUserAndLanguage.toString());

		while (matcher.find()) {
			String handle = matcher.group();
			for (HashTagAmostras hasTag : HashTagAmostras.values()) {
				if (hasTag.getHashTag().equalsIgnoreCase(handle)) {
					// TODO: Melhorar o Builder do TweetsPlayLoad
					TweetPlayload playload = hashTagsByUserAndLanguage;
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
