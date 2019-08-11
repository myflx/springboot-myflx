package com.myflx.repository;

import com.myflx.annotation.FirstLevelRepository;
import com.myflx.annotation.SecondLevelRepository;
import com.myflx.annotation.StringRepository;

import java.util.Arrays;
import java.util.List;

//@StringRepository("nameRepository")
//@FirstLevelRepository("nameRepository")
@SecondLevelRepository("nameRepository")
public class NameRepository {

    public List<String> getAll(){
        return Arrays.asList("大王","小王","炸弹","飞机","三带一");
    }
}
