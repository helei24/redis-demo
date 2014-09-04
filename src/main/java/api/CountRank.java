package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class CountRank {

  public void putUserScore(String leaderboard, Double score, String userId) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();

    try {
      jedis.zadd(leaderboard, score, userId);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
  }

  public Set<String> getBrankByCount(String leaderboard, Long count) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();
    Set<String> rankResult = new HashSet<String>();
    try {
      rankResult = jedis.zrevrange(leaderboard, 0, count);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
    return rankResult;
  }

  public Double getScoreByUserId(String leaderboard, String userId) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();
    Double score = 0.0;
    try {
      score = jedis.zscore(leaderboard, userId);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
    return score;
  }
}