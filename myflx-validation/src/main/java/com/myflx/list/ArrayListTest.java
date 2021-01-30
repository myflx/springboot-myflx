package com.myflx.list;

import com.myflx.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
    public static void main(String[] args) {
        ArrayList<Object> objects = new ArrayList<>(0);
        for (int i = 0; i < 10; i++) {
            objects.add(new UserVO());
        }
        List<Object> objects2 = objects.subList(0, 5);

        System.out.println("子列表的长度：" + objects2.size());
        objects2.remove(0);
        System.out.println("移除一个元素后子列表的长度：" + objects2.size());
        System.out.println("移除一个元素后原列表的长度：" + objects.size());

    }
}
