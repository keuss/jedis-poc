package org.keuss;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

@Slf4j
public class App {

    public static void main(String[] args) {

        log.info("App is RUNNING with args : {}", Arrays.toString(args));

        // Test Redis with Jedis client libs
        try (Jedis jedis = new Jedis("myredis", 6379)) {
            jedis.set("events/city/rome", "32,15,223,828");
            String cachedResponse = jedis.get("events/city/rome");
            log.info("cachedResponse : {}", cachedResponse);
        }

        long wait = Long.parseLong(args[0]);
        log.info("Sleeping for : {} ms ...", wait);
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ie) {
            log.error("ERROR in App", ie);
        }

    }

}