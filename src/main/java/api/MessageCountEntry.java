package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MessageCountEntry {

  public void updateEntryCount(String userName, String messageName, Long incrCounts) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();

    try {
      jedis.hincrBy(userName, messageName, incrCounts);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
  }

  public Long getEntryMessageCountByUsername(String userName, String messageName) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();
    String countStr = "";
    try {
      countStr = jedis.hget(userName, messageName);
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
}
