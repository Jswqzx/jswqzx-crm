package com.js.workbench.controller;

import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.utils.DateTimeUtil;
import com.js.utils.PrintJson;
import com.js.utils.UUIDUtil;
import com.js.vo.paginationVo;
import com.js.workbench.domain.Activity;
import com.js.workbench.domain.Clue;
import com.js.workbench.domain.Tran;
import com.js.workbench.service.ActivityService;
import com.js.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource
    private UserService userService;

    @Resource
    private ClueService clueService;

    @Resource
    private ActivityService activityService;
    @ResponseBody
    @RequestMapping("/getUserList.do")
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping("/save.do")
    public void save(Clue clue, HttpServletRequest request, HttpServletResponse response){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        boolean flag=clueService.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue c=clueService.detail(id);
        mv.addObject("c",c);
        mv.setViewName("clue/detail");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getActivityListByClueId.do")
    public List<Activity> getActivityListByClueId(String clueId){
        List<Activity> activities=activityService.getActivityListByClueId(clueId);
        return activities;
    }

    @RequestMapping("/unbund.do")
    public void unbund(String id,HttpServletResponse response){
        boolean flag=clueService.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    @ResponseBody
    @RequestMapping("/ByNameAndNotByClueId.do")
    public List<Activity> ByNameAndNotByClueId(String aname,String clueId){
        Map<String,String> map=new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> aList=activityService.ByNameAndNotByClueId(map);
        return aList;
    }

    @ResponseBody
    @RequestMapping("/bund.do")
    public boolean bund(String aid[],String cid,HttpServletResponse response){
        boolean success = clueService.bund(aid,cid);
        return success;
    }

    @ResponseBody
    @RequestMapping("/getActivityListByName.do")
    public List<Activity> getActivityListByName(String aname){
        List<Activity> actList=activityService.getActivityListByName(aname);
        return actList;
    }

    @RequestMapping("/convert.do")
    public ModelAndView convert(Tran tran,String flag,HttpServletRequest request,String clueId){
        ModelAndView mv = new ModelAndView();
        Tran t=null;
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)){
            t=tran;
            t.setId(UUIDUtil.getUUID());
            t.setCreateBy(createBy);
            t.setCreateTime(DateTimeUtil.getSysTime());
        }
        boolean flag1 =clueService.convert(clueId,t,createBy);

        if (flag1){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping("/pageList.do")
    public paginationVo<Clue> pageList(Clue clue, String pageNo, String pageSize){
        int pageNo1 = Integer.valueOf(pageNo);
        int pageSize1 = Integer.valueOf(pageSize);
        //获取跳过的页数,比如从第2页开始查,并且每页显示两条数据,就跳过(2-1)*2=2条数据
        int skipCount=(pageNo1-1)*pageSize1;
        Map<String,Object> map=new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize1);
        map.put("fullnames",clue.getFullname());
        map.put("companys",clue.getCompany());
        map.put("phones",clue.getPhone());
        map.put("mphones",clue.getMphone());
        map.put("sources",clue.getSource());
        map.put("owners",clue.getOwner());
        map.put("states",clue.getState());
        paginationVo<Clue> vo = clueService.pageList(map);
        return vo;
    }
}
