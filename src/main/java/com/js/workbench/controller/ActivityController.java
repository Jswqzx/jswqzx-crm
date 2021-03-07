package com.js.workbench.controller;

import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.utils.DateTimeUtil;
import com.js.utils.PrintJson;
import com.js.utils.UUIDUtil;
import com.js.vo.paginationVo;
import com.js.workbench.domain.Activity;
import com.js.workbench.domain.ActivityRemark;
import com.js.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping("/getUserList.do")
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    public void save(HttpServletRequest request, Activity a, HttpServletResponse response){
        String id= UUIDUtil.getUUID();
        String createTime= DateTimeUtil.getSysTime();
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        a.setId(id);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);
        boolean flag=activityService.save(a);
        PrintJson.printJsonFlag(response,flag);
    }

    @ResponseBody
    @RequestMapping("pageList.do")
    public paginationVo<Activity> pageList(HttpServletRequest request){
        String endDate=request.getParameter("endDate");
        String startDate=request.getParameter("startDate");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        paginationVo<Activity> vo=activityService.pageList(map);
        return vo;
    }

    @RequestMapping("/delete.do")
    public void delete(HttpServletRequest request,HttpServletResponse response){
        String ids[]=request.getParameterValues("id");
        boolean flag=activityService.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    @ResponseBody
    @RequestMapping("/getUserListAndActivity.do")
    public Map<String,Object> getUserListAndActivity(String id){
        System.out.println("id=="+id);

        Map<String,Object> map=new HashMap<>();

        map=activityService.getUserListAndActivity(id);

        return map;
    }

    @RequestMapping("/update.do")
    public void update(HttpServletRequest request, Activity a, HttpServletResponse response){
        /*修改时间*/
        String createTime= DateTimeUtil.getSysTime();
        /*修改者*/
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);
        boolean flag=activityService.update(a);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        Activity activity=activityService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("a",activity);
        mv.setViewName("activity/detail");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getRemarkListByAid.do")
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        List<ActivityRemark> arList=activityService.getRemarkListByAid(activityId);
        return arList;
    }

    @RequestMapping("/deleteRemark.do")
    public void deleteRemark(String id,HttpServletResponse response){
        System.out.println(id);
        boolean flag=activityService.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    @ResponseBody
    @RequestMapping(value = "/saveRemark.do",method = RequestMethod.POST)
    public Map<String,Object> saveRemark(ActivityRemark activityRemark,HttpServletRequest request){
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setEditFlag("0");
        boolean flag=activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        return map;
    }

    @ResponseBody
    @RequestMapping("/updateRemark.do")
    public Map<String, Object> updateRemark(ActivityRemark activityRemark, HttpServletRequest request){
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        boolean flag=activityService.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        return map;
    }

}
