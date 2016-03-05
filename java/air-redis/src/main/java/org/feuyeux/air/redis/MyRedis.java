package org.feuyeux.air.redis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyRedis {
    public static void main(String[] args) {
        final String host = "";
        final int port = 6379;
        Jedis jedis = new Jedis(host, port);
        redisString(jedis);
        redisList(jedis);
        redisSet(jedis);
        redisHash(jedis);
        redisZSet(jedis);
    }

    private static void redisZSet(Jedis jedis) {
        String key = "ZSET_2016";
        Map<String, Double> map = new HashMap<>();
        map.put("Java", 100D);
        map.put("Scala", 99D);
        map.put("Go", 98D);
        map.put("Python", 97D);
        map.put("Delphi", 80D);
        jedis.zadd(key, map);
        long count = jedis.zcard(key);
        jedis.zrem(key, "Delphi");
        jedis.zincrby(key, 2, "Scala");
        long orderNumber = jedis.zrank(key, "Scala");
        double score = jedis.zscore(key, "Scala");
    }

    private static void redisHash(Jedis jedis) {
        String key = "HASH_2016";
        Map<String, String> map = new HashMap<>();
        map.put("search_key", "JAVA");
        map.put("operator", "EricHan");
        map.put("search_times", "1250");
        jedis.hmset(key, map);
        long length = jedis.hlen(key);
        List<String> elements = jedis.hmget(key, "operator", "search_times");
        Set<String> keys = jedis.hkeys(key);
        jedis.hincrBy(key, "search_times", 1L);
        List<String> values = jedis.hvals(key);
        System.out.println(length);
        System.out.println(elements.get(0));
        System.out.println(keys.toArray(new String[keys.size()])[0]);
        System.out.println(values.get(0));
    }

    private static void redisSet(Jedis jedis) {
        String key = "SET_2016";
        jedis.sadd(key, "1", "2", "3", "4", "5");
        boolean existed = jedis.sismember(key, "1");
        jedis.srem(key, "1");
        long count = jedis.scard(key);
        jedis.smove(key, key + "_2", "2");
        Set<String> elements = jedis.smembers(key);
        System.out.println(existed);
        System.out.println(count);
        System.out.println(elements.toArray(new String[elements.size()])[0]);
    }

    private static void redisList(Jedis jedis) {
        String key = "LIST_2016";
        jedis.rpush(key, "1");
        jedis.lpush(key, "2");
        String element = jedis.lindex(key, 1L);
        List<String> elements = jedis.lrange(key, 0, -1);
        jedis.ltrim(key, 0, 1);
        String lpopElement = jedis.lpop(key);
        String rpopElement = jedis.rpop(key);
        System.out.println(element);
        System.out.println(elements.get(0));
        System.out.println(lpopElement);
        System.out.println(rpopElement);
    }

    private static void redisString(Jedis jedis) {
        String key = "STRING_2016";
        jedis.set(key, "1");
        jedis.incr(key);
        jedis.incrBy(key, 10);
        jedis.incrByFloat(key, 3.75d);
        jedis.decrBy(key, 10);
        jedis.decr(key);
        String result = jedis.get(key);
        System.out.println(result);
    }
}