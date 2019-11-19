package com.example.localproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class RedisScan扫描Test {

	private static Logger log = LoggerFactory.getLogger(RedisScan扫描Test.class);

	static String key = "exp_new_pda_website_filter";

	static String new_org_code = ",031605,031604";

	public static void main(String[] args) {
		RedisScan扫描Test rt = new RedisScan扫描Test();


		redisTest();


	}
	public static void redisTest() {
		Jedis redis = new Jedis("10.128.78.93", 8173);
//		Jedis redis = new Jedis("10.129.220.29", 7336);
//		String key = redis.randomKey();
//		key = "810";
		log.info("key: {} ", redis.info("Keyspace") );
		String cursor = "66820888";
		long keyCount = 0;
		ScanResult<String> scanResult = redis.scan(cursor, new ScanParams().count(3000));
		keyCount = checkKey(redis, scanResult, keyCount);
		log.info("scan 扫描完成, keyCount: {}", keyCount);
	}

	private static long checkKey(Jedis redis, ScanResult<String> scanResult, long keyCount) {
		keyCount = scanResult.getResult().size();
		for( String key : scanResult.getResult()) {
//			log.info("key: {} ", key );
			long ttl = redis.ttl(key);
			if(ttl == -1){
				log.info("==================================key: {} , ttl : {}", key , ttl);
				redis.expire(key, 1*60*60*24*15); // 15天
			}
		}
//		log.info("scanResult.getResult().size() : {} ", scanResult.getResult().size() );
		String cursor = scanResult.getStringCursor();
//		log.info("cursor: {} ", cursor );

		if(! cursor.equals("0")){
			keyCount += checkKey(redis, redis.scan(cursor, new ScanParams().count(3000)), keyCount);
		}
		return keyCount;
	}

}
