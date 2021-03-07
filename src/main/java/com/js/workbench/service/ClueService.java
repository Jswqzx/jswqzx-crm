package com.js.workbench.service;

import com.js.vo.paginationVo;
import com.js.workbench.domain.Clue;
import com.js.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {

    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String[] aid, String cid);

    boolean convert(String clueId, Tran t, String createBy);

    paginationVo<Clue> pageList(Map<String,Object> map);
}
