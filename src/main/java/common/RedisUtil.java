package common;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

  public static JedisPool getRedisPool() {

    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(100);
    config.setMaxIdle(20);
    config.setMaxWaitMillis(1000L);
    config.setTestOnBorrow(true);
    JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);

    return pool;
  }
}
