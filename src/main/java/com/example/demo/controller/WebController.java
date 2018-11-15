package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2018/11/14.
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("helloTestTh")
    public String helloTestTh(HttpServletRequest request) {
        String name = "yang";
        request.setAttribute("name", name);
        System.out.println("hello!");
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "张三", 18));
        users.add(new User(2, "李四", 19));
        users.add(new User(3, "王五", 20));
        request.setAttribute("users", users);
        Random random=new Random();
        int num=random.nextInt(2);
        System.out.println("num = " + num);
        request.setAttribute("num",num);
        return "hello";
    }

    @RequestMapping("testMybatis")
    public String testMybatis(HttpServletRequest request){
        ArrayList<User> users = (ArrayList<User>) userMapper.selectAll();
        System.out.println("users = " + users);
        request.setAttribute("users",users);
        return "hello";
    }
}
