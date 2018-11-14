package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/26.
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("sayHello")
    public String sayHello(){
        Jedis jedis=new Jedis();
        String ping = jedis.ping();
        System.out.println("ping = " + ping);
        jedis.set("name","张三");
        System.out.println(jedis.get("name"));
//        jedis.lpush("school","第一中学");
//        jedis.lpush("school","第二中学");
//        jedis.lpush("school","第三中学");
//        jedis.lpush("school","第四中学");
        List<String> school=new ArrayList<>();
        school.add("第一中学");
        school.add("第二中学");
        school.add("第三中学");
        school.add("第四中学");
        school.add("第五中学");
        jedis.lpush("school", String.valueOf(school));
        System.out.println(jedis.lrange("school",0,10));
//        Set<String> keys = jedis.keys("*");
//        for (String key : keys) {
//            System.out.println("key = " + key);
//        }
//        jedis.hset("user","username","12");
        return "hello world!1";
    }
}
