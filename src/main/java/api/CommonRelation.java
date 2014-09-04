package api;

import common.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class CommonRelation {

  public void putFriends(String IdKyes, String... friendsIds) {

    putRelations(IdKyes, friendsIds);
  }

  public void putFollowers(String IdKyes, String... friendsIds) {

    putRelations(IdKyes, friendsIds);
  }

  public void putLikers(String IdKyes, String... friendsIds) {

    putRelations(IdKyes, friendsIds);
  }

  public Set<String> getCommonFriends(String... IdKeys) {

    //   Some other Oprations...
    return getCommonSet(IdKeys);
  }

  public Set<String> getCommonFollowers(String... IdKeys) {

    //   Some other Oprations...
    return getCommonSet(IdKeys);
  }

  public Set<String> getCommonLikers(String... IdKeys) {

    //   Some other Oprations...
    return getCommonSet(IdKeys);
  }

  private void putRelations(String IdKyes, String... friendsIds) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();

    try {
      jedis.sadd(IdKyes, friendsIds);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
  }

  private Set<String> getCommonSet(String... IdKeys) {

    JedisPool pool = RedisUtil.getRedisPool();
    Jedis jedis = pool.getResource();
    Set<String> commmonSet = new HashSet<String>();

    try {
      commmonSet = jedis.sinter(IdKeys);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      e.printStackTrace();
    } finally {
      pool.returnResource(jedis);
    }
    return commmonSet;
  }
}
