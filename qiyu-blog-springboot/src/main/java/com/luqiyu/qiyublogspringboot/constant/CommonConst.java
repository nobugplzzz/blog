package com.luqiyu.qiyublogspringboot.constant;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 通用常量
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
public class CommonConst {

    /**
     * 默认昵称
     */
    public static final String DEFAULT_NICKNAME="用户" + IdWorker.getId();

    /**
     * 默认用户头像
     */
    public static final String DEFAULT_AVATAR="https://www.static.talkxj.com/avatar/user.png";

    /**
     * 否
     */
    public static final int FALSE = 0;

    /**
     * 是
     */
    public static final int TURE = 1;

    /**
     * 博主id
     */
    public static final int BLOGGER_ID = 1;

    /**
     * 删除操作
     */
    public static final String REMOVE = "删除";

    /**
     * 修改操作
     */
    public static final String UPDATE = "修改";
}
