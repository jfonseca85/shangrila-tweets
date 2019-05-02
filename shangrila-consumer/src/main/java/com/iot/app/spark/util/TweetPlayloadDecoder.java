package com.iot.app.spark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.app.spark.vo.TweetPlayload;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

/**
 * Class to deserialize JSON string to IoTData java object
 * 
 * @author jfonseca
 *
 */
public class TweetPlayloadDecoder implements Decoder<TweetPlayload> {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public TweetPlayloadDecoder(VerifiableProperties verifiableProperties) {

    }
	
	public TweetPlayload fromBytes(byte[] bytes) {
		try {
			return objectMapper.readValue(bytes, TweetPlayload.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
