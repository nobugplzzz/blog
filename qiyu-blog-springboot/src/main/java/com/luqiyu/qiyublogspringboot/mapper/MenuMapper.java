package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.MenuDTO;
import com.luqiyu.qiyublogspringboot.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-03
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


}
