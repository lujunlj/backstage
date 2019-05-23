package com.tencent.backstage.common.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:16:13
 */
public class PageUtil {

    /**
     * Pageinfo 数据处理，预防redis反序列化报错
     * @param page
     * @return
     */
    public static Map toPage(Page page) {
        Map map = new HashMap();
        map.put("content",page.getRecords());
        map.put("totalElements",page.getTotal());
        return map;
    }

    /**
     * List 分页
     * @param page
     * @param size
     * @param list
     * @return
     */
    public static List toPage(int page, int size , List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;

        if(fromIndex > list.size()){
            return new ArrayList();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex,list.size());
        } else {
            return list.subList(fromIndex,toIndex);
        }
    }

    /**
     * @param object
     * @param totalElements
     * @return
     */
    public static Map toPage(Object object, Object totalElements) {
        Map map = new HashMap();

        map.put("content",object);
        map.put("totalElements",totalElements);

        return map;
    }
}
