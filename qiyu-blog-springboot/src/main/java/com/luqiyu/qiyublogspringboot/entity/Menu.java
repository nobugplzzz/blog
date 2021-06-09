package com.luqiyu.qiyublogspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 菜单icon
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 类型
     */
    private Integer type;


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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
