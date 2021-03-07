package com.js.exceptionhandler;

import com.js.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyExceptionHandler {

//    @ExceptionHandler
//    public void exceptionHandler(Exception ex){
////        ModelAndView mv = new ModelAndView();
////        mv.addObject("msg",ex.getMessage());
//        System.out.println(ex.getMessage());
//    }
}
