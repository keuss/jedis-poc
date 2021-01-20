package org.keuss;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class RedisClient {
    private static volatile RedisClient instance = null;
    private static JedisPool jedisPool;

    public static RedisClient getInstance(String ip, final int port) {
        if (instance == null) {
            synchronized (RedisClient.class) {
                if (instance == null) {
                    instance = new RedisClient(ip, port);
                }
            }
        }
        return instance;
    }

    private RedisClient(String ip, int port) {
        try {
            if (jedisPool == null) {
                jedisPool = new JedisPool(new URI("http://" + ip + ":" + port));
            }
        } catch (URISyntaxException e) {
            log.error("Malformed server address", e);
        }
    }

    public String set(final String key, final String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        } catch (Exception ex) {
            log.error("Exception caught in set", ex);
        }
        return null;
    }

    public String get(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception ex) {
            log.error("Exception caught in get", ex);
        }
        return null;
    }

    public Long lpush(final String key, final String... strings) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, strings);
        } catch (Exception ex) {
            log.error("Exception caught in lpush", ex);
        }
        return null;
    }

    public String rpop(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(key);
        } catch (Exception ex) {
            log.error("Exception caught in rpop", ex);
        }
        return null;
    }

    public String hmset(final String key, final Map<String, String> hash) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmset(key, hash);
        } catch (Exception ex) {
            log.error("Exception caught in hmset", ex);
        }
        return null;
    }

    public Map<String, String> hgetAll(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception ex) {
            log.error("Exception caught in hgetAll", ex);
        }
        return new HashMap<>();
    }

    public Long sadd(final String key, final String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, members);
        } catch (Exception ex) {
            log.error("Exception caught in sadd", ex);
        }
        return null;
    }

    public Set<String> smembers(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Exception ex) {
            log.error("Exception caught in smembers", ex);
        }
        return new HashSet<>();
    }

    public String mset(final Map<String, String> keysValues) {
        try (Jedis jedis = jedisPool.getResource()) {
            ArrayList<String> keysValuesArrayList = new ArrayList<>();
            keysValues.forEach((key, value) -> {
                keysValuesArrayList.add(key);
                keysValuesArrayList.add(value);
            });
            return jedis.mset((keysValuesArrayList.toArray(new String[keysValues.size()])));
        } catch (Exception ex) {
            log.error("Exception caught in mset", ex);
        }
        return null;
    }

    public Set<String> keys(final String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys(pattern);
        } catch (Exception ex) {
            log.error("Exception caught in keys", ex);
        }
        return new HashSet<>();
    }

    //TODO implement others jedis methods here ...

    public void flushAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushAll();
        } catch (Exception ex) {
            log.error("Exception caught in flushAll", ex);
        }
    }

    public static void destroyInstance() {
        jedisPool = null;
        instance = null;
    }

}