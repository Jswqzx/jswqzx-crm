package com.js.workbench.dao;

import com.js.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);

    int getClueList(Map<String,Object> map);

    List<Clue> getPageList(Map<String,Object> map);

}
