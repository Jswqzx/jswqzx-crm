package com.js.listenter;

import com.js.settings.domain.DicValue;
import com.js.settings.service.DicService;
import com.js.settings.service.impl.DicServiceImpl;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
//        通过上下文获取到Service对象
        DicService dicService=(DicService) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("dicServiceImpl");
        Map<String, List<DicValue>> all = dicService.getAll();
        Set<String> set = all.keySet();
        for(String key:set){
            servletContext.setAttribute(key,all.get(key));
        }

//处理properites文件

        Map<String,String> pMap=new HashMap<>();
        ResourceBundle rb=ResourceBundle.getBundle("conf/Stage2Possibility");
        Enumeration<String> keys = rb.getKeys();

        while (keys.hasMoreElements()){

//            阶段
            String key=keys.nextElement();
//            可能性
            String value = rb.getString(key);

            pMap.put(key,value);
        }
        servletContext.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
