package com.js.settings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.utils.DateTimeUtil;
import com.js.utils.MD5Util;
import com.js.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public Map<String,Object> login(String loginPwd, String loginAct, HttpServletRequest request){
        //接收转换后的密码
        loginPwd= MD5Util.getMD5(loginPwd);
        //获取ip地址
        String ip=request.getRemoteAddr();
        Map<String,Object>map=new HashMap<>();
        try {
            User user=userService.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            map.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg",e.getMessage());
        }finally {
            return map;
        }
    }
}
