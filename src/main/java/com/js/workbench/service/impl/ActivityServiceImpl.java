package com.js.workbench.service.impl;

import com.js.settings.dao.UserDao;
import com.js.settings.domain.User;
import com.js.settings.service.UserService;
import com.js.vo.paginationVo;
import com.js.workbench.dao.ActivityDao;
import com.js.workbench.dao.ActivityRemarkDao;
import com.js.workbench.domain.Activity;
import com.js.workbench.domain.ActivityRemark;
import com.js.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao remarkDao;

    @Resource
    private UserDao userDao;

    @Override
    public boolean save(Activity a) {
        boolean falg = false;
        int i = activityDao.save(a);
        if (i >= 1) {
            falg = true;
        }
        return falg;
    }

    @Override
    public paginationVo<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        paginationVo<Activity> vo = new paginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean falg = true;

//      1.查询出要删除的备注的数量
        int count = remarkDao.getCountById(ids);
        System.out.println("count=" + count);
//      2.删除备注,返回收到影响的条数(实际删除的数量)
        int cound2 = remarkDao.deleteById(ids);
        System.out.println("count2=" + cound2);
//      3.删除市场活动
        if (count != cound2) {
            falg = false;
        }
//      4.删除活动表中的数据
        int count3 = activityDao.delete(ids);
        System.out.println("count3=" + count3 + "----" + ids.length);
//      5.判断删除的条数是否和要删除的数量相同
        if (count3 != ids.length) {
            falg = false;
        }
        return falg;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
//        取ulist
        List<User> uList = userDao.getUserList();
//        取a
        Activity a = activityDao.getById(id);
//        打包成map
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean falg = false;
        int i = activityDao.update(a);
        if (i >= 1) {
            falg = true;
        }
        return falg;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        return remarkDao.getRemarkListByAid(activityId);
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = false;
        int i = remarkDao.deleteRemark(id);
        if (i != 0) {
            flag = true;
        }
        System.out.println(flag);
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        int count = remarkDao.saveRemark(activityRemark);
        boolean flag = false;
        if (count != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        int count = remarkDao.updateRemark(activityRemark);
        boolean flag = false;
        if (count != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> activities = activityDao.getActivityListByClueId(clueId);
        return activities;
    }

    @Override
    public List<Activity> ByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> aList=activityDao.ByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> actList=activityDao.getActivityListByName(aname);
        return actList;
    }


}
