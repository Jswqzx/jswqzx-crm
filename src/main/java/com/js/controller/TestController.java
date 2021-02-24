package com.js.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class TestController {

    @RequestMapping("/some.do")
    public void doSome(){
        UUID uuid=UUID.randomUUID();
        System.out.println(uuid);
    }

    @RequestMapping("/other.do")
    public ModelAndView doOther(){
        ModelAndView mv=new ModelAndView();
        System.out.println(mv);
        return mv;
    }
}
