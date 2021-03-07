package com.js.settings.service;

import com.js.exception.LoginException;
import com.js.settings.domain.User;
import com.js.workbench.domain.Activity;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
