package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserBackDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
    /**
     * 查询后台用户列表
     *
     * @param condition 查询条件
     * @return
     */
    List<UserBackDTO> listUsers(@Param("condition") ConditionVO condition);

    /**
     * 查询后台用户列表的总数
     *
     * @param condition 查询条件
     * @return 总数
     */
    Long countUsers(@Param("condition") ConditionVO condition);
}
