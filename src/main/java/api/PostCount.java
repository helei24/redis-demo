package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PostCount {

    public void updatePostCount(String key) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();

        try {
            Long postCount = jedis.incr(key);

            if (postCount == 1) {
                jedis.expire(key, 3600 * 24 * 30);
            }
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public Long getPostCount(String key) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        String postCountStr = "";
        try {
            if (jedis.ttl(key) >= 0L) { // the key's remaining time to live
                postCountStr = jedis.get(key);
            }
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }

        if (postCountStr.matches("^\\d+$")) {
            return Long.parseLong(postCountStr);
        } else {
            return 0L;
        }
    }

    public Long getTTL(String key) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        Long ttl = 0L;
        try {
            ttl = jedis.ttl(key); // the key's remaining time to live
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return ttl;
    }
}
