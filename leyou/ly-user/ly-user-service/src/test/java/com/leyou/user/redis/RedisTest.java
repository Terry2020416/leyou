package com.leyou.user.redis;

import com.leyou.LyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUserService.class)
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        //存储数据
        ValueOperations<String, String> stringStringValueOperations = this.redisTemplate.opsForValue();
        stringStringValueOperations.set("name","Terry");
        String name = stringStringValueOperations.get("name");
        System.out.println("name:"+name);
    }

    @Test
    public void testRedis2(){
        //存储数据的同时设置存活时间
        ValueOperations<String, String> stringStringValueOperations = this.redisTemplate.opsForValue();
        //stringStringValueOperations.set("name2","Lucky",1, TimeUnit.MINUTES);
        System.out.println(stringStringValueOperations.get("name2"));
    }

    @Test
    public void testHash(){
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps("user");
        hashOps.put("name","Jack");
        hashOps.put("age","21");

        //获取单个数据
        System.out.println(hashOps.get("name"));

        //获取所有数据
        Map<Object, Object> entries = hashOps.entries();
        for (Map.Entry<Object, Object> m : entries.entrySet()){
            System.out.println(m.getKey()+"----"+m.getValue());
        }
    }
}
