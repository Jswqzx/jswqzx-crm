package com.js.settings.test;

import com.js.exception.LoginException;
import com.js.utils.MD5Util;
import org.junit.Test;

public class Test1 {

    @Test
    public void Test01(){
        String pwd="jswqzx8963";
        String s = MD5Util.getMD5(pwd);
        System.out.println(s);
    }
}
