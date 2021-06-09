package com.luqiyu.qiyublogspringboot.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean属性复制工具类
 *
 * @author: 启誉
 * @create: 2021-05-30
 **/
public class BeanCopyUtil {
    /**
     * 根据现有对象的属性创建目标对象，并赋值
     * 参数使用Class对象，就无需自己new对象了
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T copyObject(Object source, Class<T> target) {
        T temp = null;
        try {
            // 用Class对象创建对象
            temp = target.newInstance();
            if (null != source) {
                // 属性类型和名称要一致
                org.springframework.beans.BeanUtils.copyProperties(source, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 拷贝集合
     *
     * @param source
     * @param target
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source && source.size() > 0) {
            for (Object obj : source) {
                list.add(BeanCopyUtil.copyObject(obj, target));
            }
        }
        return list;
    }
}
