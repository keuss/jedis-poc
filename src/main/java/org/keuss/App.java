package org.keuss;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        redisClient.hmset("users/user#1/profile", props);
        redisClient.sadd("users/user#1/roles", "role1", "role2");

        Map<String, String> fields = redisClient.hgetAll("users/user#1/profile");
        log.info("fields {}", fields);
        Set<String> readKeys = redisClient.keys("users/user#1/*");
        log.info("readKeys {}", readKeys);

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