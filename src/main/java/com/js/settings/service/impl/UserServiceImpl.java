package com.js.settings.service.impl;

import com.js.exception.LoginException;
import com.js.settings.dao.UserDao;
import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.utils.DateTimeUtil;
import com.js.workbench.domain.Activity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    @Transactional
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user=userDao.login(loginAct,loginPwd);
        if (user==null){
            throw new LoginException("账号或者密码不正确");
        }
        String expireTime = user.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(sysTime)<0){
            throw new LoginException("账号失效");
        }
        String lockState=user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已被锁定");
        }
        String allowIps=user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("访问地址受限");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
