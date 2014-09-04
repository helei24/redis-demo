package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class HitCount {

    public Long getHitcount(String keyHash) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        String countStr = "";
        try {
            countStr = jedis.get(keyHash);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }

        if (countStr.matches("^\\d+$")) {
            return Long.parseLong(countStr);
        } else {
            return 0L;
        }
    }

    public synchronized Long updateHitCount(String keyHash) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        Long newHitCount = jedis.incr(keyHash);
        return newHitCount;
    }
}
