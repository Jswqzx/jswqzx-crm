package com.js.workbench.service.impl;

import com.js.utils.DateTimeUtil;
import com.js.utils.UUIDUtil;
import com.js.workbench.dao.ContactsDao;
import com.js.workbench.dao.CustomerDao;
import com.js.workbench.dao.TranDao;
import com.js.workbench.dao.TranHistoryDao;
import com.js.workbench.domain.Customer;
import com.js.workbench.domain.Tran;
import com.js.workbench.domain.TranHistory;
import com.js.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao tranHistoryDao;


    @Override
    public boolean save(Tran tran, String customerName) {
        boolean flag=true;

        Customer cus = customerDao.getCustomerByName(customerName);
        if (cus!=null){
            tran.setCustomerId(cus.getId());
        }else {
            cus=new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setCreateTime(tran.getCreateTime());
            cus.setName(customerName);
            cus.setCreateBy(tran.getCreateBy());
            cus.setContactSummary(tran.getContactSummary());
            cus.setNextContactTime(tran.getNextContactTime());
            cus.setOwner(tran.getOwner());
            int count1=customerDao.save(cus);
            if (count1!=1){
                flag=false;
            }
            tran.setCustomerId(cus.getId());
        }

        int count2=tranDao.save(tran);
        if (count2!=1){
            flag=false;
        }

        TranHistory th=new TranHistory();

        th.setId(UUIDUtil.getUUID());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setTranId(tran.getId());
        th.setCreateTime(tran.getCreateTime());
        th.setCreateBy(th.getCreateBy());

        int count3=tranHistoryDao.save(th);
        if (count3!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t=tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tList=tranHistoryDao.getHistoryListByTranId(tranId);
        return tList;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag=true;
        int count1=tranDao.changeStage(tran);
        if (count1!=1){
            flag=false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        int save = tranHistoryDao.save(tranHistory);
        if (save!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        int total = tranDao.getTotal();
        List<Map<String,Object>> dataList=tranDao.getCharts();
        Map<String, Object> map =new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }


}
