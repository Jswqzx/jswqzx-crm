package com.js.settings.dao;


import com.js.settings.domain.User;
import com.js.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    User login(@Param("user") String loginAct,@Param("pass") String loginPwd);

    List<User> getUserList();
}
