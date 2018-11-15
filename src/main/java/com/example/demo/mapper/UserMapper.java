package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2018/11/15.
 */
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User selectUser(int id);

    @Select("select * from user")
    List<User> selectAll();
}
