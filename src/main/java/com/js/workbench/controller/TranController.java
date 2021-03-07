package com.js.workbench.controller;

import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.utils.DateTimeUtil;
import com.js.utils.UUIDUtil;
import com.js.workbench.domain.Tran;
import com.js.workbench.domain.TranHistory;
import com.js.workbench.service.CustomerService;
import com.js.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

    @Resource
    private TranService tranService;

    @Resource
    private UserService userService;

    @Resource
    private CustomerService customerService;
    @RequestMapping("/add.do")
    public ModelAndView add(){
        List<User> userList = userService.getUserList();
        ModelAndView mv = new ModelAndView();
        mv.addObject("uList",userList);
        mv.setViewName("transaction/save");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getCustomerName.do")
    public List<String> getCustomerName(String name){
        List<String> uList=customerService.getCustomerName(name);
        return uList;
    }


    @RequestMapping("/save.do")
    public ModelAndView save(Tran tran, String customerName, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        boolean falg=tranService.save(tran,customerName);
        if (falg){
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        Tran t=tranService.detail(id);
        ServletContext context = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) context.getAttribute("pMap");
        String s = pMap.get(t.getStage());
        t.setPossibility(s);
        mv.addObject("t",t);
        mv.setViewName("transaction/detail");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getHistoryListByTranId.do")
    public List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest request){
        List<TranHistory> tList=tranService.getHistoryListByTranId(tranId);
        ServletContext context = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) context.getAttribute("pMap");
        for (TranHistory tran : tList) {
            String s = pMap.get(tran.getStage());
            tran.setPossibility(s);
        }
        return tList;
    }

    @ResponseBody
    @RequestMapping("/changeStage.do")
    public Map<String,Object> changeStage(Tran tran,HttpServletRequest request){
//        设置修改时间与修改者
        tran.setEditTime(DateTimeUtil.getSysTime());
        tran.setEditBy(((User)request.getSession().getAttribute("user")).getName());
//        获取上下文域对象,然后取出可能性对应关系的map集合,然后根据交易的状态取出可能性
        ServletContext context = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) context.getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
//        将可能性赋给交易对象
        tran.setPossibility(possibility);
//        调用业务层,返回一个boolean类型
        boolean flag=tranService.changeStage(tran);
//        用map集合装载数据,返回给前台
        Map<String,Object> map=new HashMap<>();
        map.put("t",tran);
        map.put("success",flag);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getCharts.do")
    public Map<String,Object> getCharts(){
        Map<String,Object> map=tranService.getCharts();
        return map;
    }
}
