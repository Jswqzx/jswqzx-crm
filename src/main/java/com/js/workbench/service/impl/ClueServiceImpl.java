package com.js.workbench.service.impl;

import com.js.utils.DateTimeUtil;
import com.js.utils.UUIDUtil;
import com.js.vo.paginationVo;
import com.js.workbench.dao.*;
import com.js.workbench.domain.*;
import com.js.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    //    线索相关表
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;

    //    客户相关表
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    //    联系人相关表
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //    交易相关表
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag = false;
        int i = clueDao.save(clue);
        if (i != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = false;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean bund(String[] aid, String cid) {
        boolean flag = false;
        int count = 0;
        for (String id : aid) {
            ClueActivityRelation ca = new ClueActivityRelation();
            ca.setId(UUIDUtil.getUUID());
            ca.setActivityId(id);
            ca.setClueId(cid);
            count += clueActivityRelationDao.bund(ca);
        }
        if (count != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

//        1.获取线索信息,根据线索id
        Clue clue = clueDao.getById(clueId);
//        2.通过线索信息获取客户信息(通过公司名称去查询客户表，)如果客户信息不存在就创建
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
//        如果客户对象为空就创建一个
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());//把线索的信息转换为客户的信息
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());

            int cont1 = customerDao.save(customer);
            if (cont1!=1){
                flag=false;
            }
        }
//        3.通过线索对象提取联系人信息,保存联系人
        Contacts con=new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        con.setCustomerId(customer.getId());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setContactSummary(clue.getContactSummary());
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());

        int count2=contactsDao.save(con);
        if (count2!=1){
            flag=false;
        }

//        4.线索备注转换到客户备注与联系人备注中

//        查询出该线索有关的备注信息列表
        List<ClueRemark> clueRemarkList= clueRemarkDao.getListByClueId(clueId);
//        取出每一个线索备注对象
        for (ClueRemark clueRemark : clueRemarkList) {
//            取出备注信息
            String noteContent = clueRemark.getNoteContent();
//            创建客户备注对象,添加客户备注对象

            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setEditFlag("0");
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            int count3=customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag=false;
            }
//            创建联系人备注对象,添加联系人备注对象
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(con.getId());
            int count4=contactsRemarkDao.save(contactsRemark);
            if (count4!=1){
                flag=false;
                }
            }
//          5.把线索和市场活动的关联关系转换到联系人和市场活动的关系
//            查询出与该条线索关联的市场活动,查询市场活动的关联关系表
        List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.getListByClueId(clueId);
//            遍历出每一条市场活动关联的关联关系记录
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
//                获取活动id
                String activityId=clueActivityRelation.getActivityId();
//                创建 联系人与市场活动的关联关系对象,让第三步生成的联系人与市场活动做关联
                ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(con.getId());

                int count5=contactsActivityRelationDao.save(contactsActivityRelation);
                if (count5!=1){
                    flag=false;
                }
        }
        System.out.println("5");
//        6.如果有交易就创建一条交易
        if (t!=null){
//            t对象已经在控制层封装好了,通过线索对象继续给交易对象添加信息
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(con.getId());
            System.out.println("6");
//            添加交易
            int count6=tranDao.save(t);
            if (count6!=1){
                flag=false;
            }

//         7.创建交易历时
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            System.out.println("7");
//            7。添加交易历时
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7!=1){
                flag=false;
            }
        }

//        8.删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count8=clueRemarkDao.delete(clueRemark);
            if (count8!=1){
                flag=false;
            }
        }

//        9.删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            int count9=clueActivityRelationDao.delete(clueActivityRelation);
            if (count9!=1){
                flag=false;
            }
        }

//        10.删除线索
        int count10=clueDao.delete(clueId);
        if (count10!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public paginationVo<Clue> pageList(Map<String,Object> map) {
        paginationVo<Clue> vo = new paginationVo<>();
        int total=clueDao.getClueList(map);//获取总线索总数
        System.out.println(total);
        List<Clue> clueList = clueDao.getPageList(map);
        System.out.println("3==");
        vo.setDataList(clueList);
        vo.setTotal(total);
        return vo;
    }
}
