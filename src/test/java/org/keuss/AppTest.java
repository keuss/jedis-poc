package org.keuss;

import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AppTest {

    private static RedisServer redisServer;
    private static int port;
    private static RedisClient redisClient;

    @BeforeClass
    public static void setUp() throws IOException {

        // Take an available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        redisServer = new RedisServer(port);
    }

    @AfterClass
    public static void destroy() {
        if (redisServer.isActive()) {
            redisServer.stop();
            RedisClient.destroyInstance();
        }
    }

    @Before
    public void init() {
        if (!redisServer.isActive()) {
            redisServer.start();
        }
        redisClient = RedisClient.getInstance("127.0.0.1", port);
    }

    @After
    public void flushAll() {
        redisClient.flushAll();
    }

    @Test
    public void testKeys() {
        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("balls:cricket", "160");
        keyValues.put("balls:football", "450");
        keyValues.put("balls:volleyball", "270");
        redisClient.mset(keyValues);
        Set<String> readKeys = redisClient.keys("ball*");
        log.info("readKeys {}", readKeys);
        Assert.assertEquals(keyValues.size(), readKeys.size());

    }

    @Test
    public void testLpush() {
        redisClient.lpush("queue#tasks", "firstTask", "secondTask");

        Assert.assertEquals("firstTask", redisClient.rpop("queue#tasks"));
        Assert.assertEquals("secondTask", redisClient.rpop("queue#tasks"));
        Assert.assertNull(redisClient.rpop("queue#tasks"));
    }

    @Test
    public void testSadd() {
        redisClient.sadd("nicknames", "nicknames1", "nicknames2", "nicknames1");

        Set<String> nicknames = redisClient.smembers("nicknames");
        log.info("nicknames {}", nicknames);
        Assert.assertEquals(2, nicknames.size());
    }

    @Test
    public void TestHmset() {
        Map<String, String> props = new HashMap<>();
        props.put("name", "keuss");
        props.put("job", "coder");
        redisClient.hmset("user#1", props);

        Map<String, String> fields = redisClient.hgetAll("user#1");
        log.info("fields {}", fields);
        Assert.assertEquals(2, fields.size());
        Assert.assertEquals("coder", fields.get("job"));
    }

}