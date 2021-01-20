package org.keuss;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class App {

    private static final RedisClient redisClient = RedisClient.getInstance("myredis", 6379);

    public static void main(String[] args) {

        log.info("App is RUNNING with args : {}", Arrays.toString(args));

        // Test Redis with Jedis client libs
        redisClient.set("myKey", "myValue");
        String cachedResponse = redisClient.get("myKey");
        log.info("cachedResponse : {}", cachedResponse);

        // Other test
        Map<String, String> props = new HashMap<>();
        props.put("name", "keuss");
        props.put("job", "coder");
        redisClient.hmset("user#1", props);

        Map<String, String> fields = redisClient.hgetAll("user#1");
        log.info("fields {}", fields);

        long wait = Long.parseLong(args[0]);
        log.info("Sleeping for : {} ms ...", wait);
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ie) {
            log.error("ERROR in App", ie);
        }

        redisClient.flushAll();
        RedisClient.destroyInstance();

    }

}