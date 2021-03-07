package com.js.workbench.dao;

import com.js.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

     int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
