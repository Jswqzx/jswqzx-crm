package com.js.settings.service.impl;

import com.js.settings.dao.DicTypeDao;
import com.js.settings.dao.DicValueDao;
import com.js.settings.domain.DicType;
import com.js.settings.domain.DicValue;
import com.js.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeDao dicTypeDao;

    @Resource
    private DicValueDao dicValueDao;

    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        /*字典类型列表*/
        List<DicType> typeList = dicTypeDao.getTypeList();
        /*将字典类型列表遍历*/
        for(DicType dt:typeList){
            String code=dt.getCode();
            List<DicValue> dicList=dicValueDao.getListByCode(code);
            map.put(code,dicList);
        }
        return map;
    }

}
