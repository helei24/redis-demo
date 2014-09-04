package redis_test;

import api.CommonRelation;
import api.CountRank;
import api.HitCount;
import api.MessageCountEntry;
import api.MessageQueue;
import api.MessageSet;
import api.PostCount;
import common.MD5Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisTest {

  public static void main(String[] args) {

    // Jedis test = new Jedis("127.0.0.1", 6379);

    RedisTest redisTest = new RedisTest();

    //Key String
//    redisTest.hitCountTest();
//    redisTest.postCountTest();

    //List
 //   redisTest.messageQueueTest();

    //Set
//    redisTest.messageSetTest();
//    redisTest.commonRalationTest();

    //Hash
//    redisTest.messageCountEntryTest();

    //Sorted Set
    redisTest.countRankTest();
  }

  /**
   * ****Type String*********
   */
  /* HitCount Test */
  private void hitCountTest() {

    String expression = "(A and B) OR (C and D)";
    String expressionHash = MD5Util.getMd5(expression);
    // System.out.println(expressionHash);
    HitCount hc = new HitCount();
    for (int i = 0; i < 100; i++) {
      hc.updateHitCount(expressionHash);
      // System.out.println(hitLong);
    }
    Long hitCount = hc.getHitcount(expressionHash);
    System.out.println("current hitcount:" + hitCount);
  }

  /**
   * *******Type String*********
   */
   /* PostCount Test */
  private void postCountTest() {

    String entryHash = "254f938f87a5aff1efabb4e5a0d51445";
    Long customerId = 2L;
    PostCount pc = new PostCount();
    for (int i = 0; i < 10; i++) {
      pc.updatePostCount(entryHash + customerId);
    }
    Long postCount = pc.getPostCount(entryHash + customerId);
    Long ttl = pc.getTTL(entryHash + customerId);
    System.out.println("current postCount: " + postCount);
    System.out.println("time to live: " + ttl / 3600 + " hours.");
  }

  /**
   * *****Type List****
   */
   /* MessageQueue Test */
  private void messageQueueTest() {

    String msgQueueName = "newMsg";
    String msg1 = "New message one.";
    String msg2 = "New message two.";
    String msg3 = "New message three.";

    MessageQueue mq = new MessageQueue();
    mq.lpushMessage(msgQueueName, msg1, msg2, msg3);
    String msg = null;
    do {
      msg = mq.rpopMessage(msgQueueName);
      if (msg != null) {
        System.out.println("Current message: " + msg);
      }
    } while (msg != null);
  }

  /**
   * *******Type Set***********
   */
  /* MessageSet Test */
  private void messageSetTest() {

    List<Map<String, String>> msgs = RedisTest.buildMessages();
    List<Long> userIds = RedisTest.buildUserIds();
    MessageSet ms = new MessageSet();
    ms.putMessage(msgs, userIds);

    List<Map<String, String>> msgsOfUser = ms.getMsgsByUserId(4L);
    for (Map<String, String> msg : msgsOfUser) {
      System.out.println("User 4L: message title: " + msg.get("title"));
      System.out.println("User 4L: message content: " + msg.get("content"));
    }
  }

  /**
   * *******Type Set***********
   */
  /*Common Relationship Test*/
  private void commonRalationTest() {

    String Id1 = "1";
    String Id2 = "2";
    String Id3 = "3";
    String Id4 = "4";
    String Id5 = "5";
    String user1 = "IdKey:" + Id1;
    String user2 = "IdKey:" + Id2;
    CommonRelation cr = new CommonRelation();
    cr.putFriends(user1, Id3, Id4, Id5);
    cr.putFriends(user2, Id4, Id5);
    Set<String> commonFriendIds = cr.getCommonFriends(user1, user2);
    System.out.print("user 1 and user2's common friends Ids:");
    for (String friendId : commonFriendIds) {
      System.out.print(" " + friendId);
    }
  }

  /**
   * *******Type Hash***********
   */
   /* Message Count In Object*/
  private void messageCountEntryTest() {

    MessageCountEntry mse = new MessageCountEntry();
    String systemMessage = "system";
    String commentMessage = "comment";
    String userName = "user:1";

    mse.updateEntryCount(userName, systemMessage, 1L);
    mse.updateEntryCount(userName, commentMessage, 10L);
    mse.updateEntryCount(userName, systemMessage, 3L);
    mse.updateEntryCount(userName, commentMessage, 5L);

    Long systemMessageCount = mse.getEntryMessageCountByUsername(userName, systemMessage);
    Long commentMessageCount = mse.getEntryMessageCountByUsername(userName, commentMessage);
    System.out.println(userName + "'s new system message count:" + systemMessageCount);
    System.out.println(userName + "'s new comment message count:" + commentMessageCount);
  }

  /**
   * *****Type Sorted Set*****
   */
  private void countRankTest() {

    String gameName = "just play it";
    CountRank cr = new CountRank();
    for (int i = 1; i <= 100; i++) {
      String useId = "" + i;
      Double score = Math.random() * 3999 + 1;
      cr.putUserScore(gameName, score, useId);
    }

    Set<String> rankedUserIds = cr.getBrankByCount(gameName, 10L);

    System.out.println("Game " + gameName + "'s first 10 ranking result:");
    for (String userId : rankedUserIds) {
      Double score = (double) Math.round(cr.getScoreByUserId(gameName, userId) * 100) / 100;
      System.out.println("Score: " + score + ", userId:" + userId);
    }
  }

  private static List<Map<String, String>> buildMessages() {

    List<Map<String, String>> msgs = new ArrayList<Map<String, String>>();
    for (int i = 1; i <= 5; i++) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("title", "title" + i);
      map.put("content", "content" + i);
      msgs.add(map);
    }
    return msgs;
  }

  private static List<Long> buildUserIds() {

    List<Long> userIds = new ArrayList<Long>();
    for (long i = 1L; i <= 10L; i++) {
      userIds.add(i);
    }
    return userIds;
  }

}