package com.zenglb.framework;

/**
 * Created by zlb on 2018/3/5.
 */

import java.util.Iterator;
import java.util.List;


public class Test {

    // 出现java.util.ConcurrentModificationException
    public List<String> m1(List<String> list) {
        for (String temp : list) {
            if ("2".equals(temp)) {
                list.remove(temp);
            }
        }
        return list;
    }

    // 出现java.util.ConcurrentModificationException
    public List<String> m2(List<String> list) {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            if ("2".equals(temp)) {
//                list.clear();
                iterator.remove();
            }
        }
        return list;
    }


    //successful! for for 循环
    public List<String> m3(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if ("2".equals(list.get(i))) {
                list.remove(i);
            }
        }
        return list;
    }

}