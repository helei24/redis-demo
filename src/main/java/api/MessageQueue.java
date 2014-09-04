package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MessageQueue {

    public void lpushMessage(String msgName, String... messages) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        try {
            jedis.lpush(msgName, messages);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public String rpopMessage(String msgName) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        String msg = "";
        try {
            msg = jedis.rpop(msgName);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return msg;
    }
}
