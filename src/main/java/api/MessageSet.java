package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageSet {

    public void putMessage(List<Map<String, String>> msgs, List<Long> userIds) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();
        try {
            int i = 1;
            for (Map<String, String> map : msgs) {
                String msgName = "msg:" + i;
                jedis.hmset(msgName, map);
                sendToUsers(jedis, msgName, userIds);
                i++;
            }
            /*
             * Pipeline pipeline = jedis.pipelined(); int i = 1; for
             * (Map<String, String> map : msgs) { Set<String> keys =
             * map.keySet(); for (String key : keys) { pipeline.hset("msg:" + i,
             * key, map.get(key)); } i++; } pipeline.exec();
             */
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    public List<Map<String, String>> getMsgsByUserId(Long userId) {

        JedisPool pool = RedisUtil.getRedisPool();
        Jedis jedis = pool.getResource();

        String userMsg = "user:" + userId + ":msg";
        List<Map<String, String>> msgs = new ArrayList<Map<String, String>>();
        try {
            Set<String> msgNames = jedis.smembers(userMsg);
            msgs = getMsgs(jedis, msgNames);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
        return msgs;
    }

    public List<Map<String, String>> getMsgs(Jedis jedis, Set<String> msgNames) {

        List<Map<String, String>> msgs = new ArrayList<Map<String, String>>();
        for (String msgName : msgNames) {
            Map<String, String> msg = jedis.hgetAll(msgName);
            msgs.add(msg);
        }
        return msgs;
    }

    private void sendToUsers(Jedis jedis, String msgName, List<Long> userIds) {

        for (Long Id : userIds) {
            jedis.sadd("user:" + Id + ":msg", msgName);
        }
    }
}
