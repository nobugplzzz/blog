package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 逻辑删除视图对象
 *
 * @author: 启誉
 * @create: 2021-06-05
 **/
@Getter // get属性 对象 to json
@Setter
public class LogicDeleteVO {

    /**
     * id列表
     */
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Long> idList;

    /**
     * 逻辑删除状态值
     */
    @ApiModelProperty(name = "isDelete", value = "删除状态", required = true, dataType = "Integer")
    private Integer isDeleted;
}
