package com.luqiyu.qiyublogspringboot.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luqiyu.qiyublogspringboot.entity.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单传输对象，没有创建和更新时间属性
 *
 * @author: 启誉
 * @create: 2021-06-03
 **/
@Getter
@Setter //对象 to json
public class MenuDTO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 菜单路径
     */
    private String url;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 菜单icon
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 是否禁用 0否1是
     */
    private Integer isDisable;

    /**
     * 是否隐藏  0否1是
     */
    private Integer isHidden;

    /**
     * 子菜单
     */
    private List<MenuDTO> children;



}
