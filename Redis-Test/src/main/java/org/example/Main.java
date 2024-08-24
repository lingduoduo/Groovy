package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Main {
    public static void main(String[] args) {
        // Connect to Redis on localhost at the default port 6379 using Jedis directly
        Jedis jedis = new Jedis("localhost", 6379);

        // Store a value
        jedis.set("myKey", "Hello, Redis!");

        // Retrieve a value
        String result = jedis.get("myKey");
        System.out.println("Value from Redis: " + result);

        jedis.close();  // Close the Jedis connection

        // Using JedisPool for managing connections
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(3);
        config.setMaxIdle(1);

        JedisPool jedisPool = new JedisPool(config, "localhost", 6379);
        Jedis pooledJedis = null;
        try {
            pooledJedis = jedisPool.getResource();
            pooledJedis.set("name", "Ling");
            String value = pooledJedis.get("name");
            System.out.println("Value from Redis (pooled connection): " + value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pooledJedis != null) {
                pooledJedis.close();  // Return the Jedis instance to the pool
            }
            jedisPool.close();  // Close the pool
        }
    }
}
