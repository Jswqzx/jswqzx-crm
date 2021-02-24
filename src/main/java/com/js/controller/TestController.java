package com.js.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class TestController {

    @RequestMapping("/some.do")
    public void doSome(){
        UUID uuid=UUID.randomUUID();
        System.out.println(uuid);
    }
}
