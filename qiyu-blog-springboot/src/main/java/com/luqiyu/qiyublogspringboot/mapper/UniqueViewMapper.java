package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.UniqueViewDTO;
import com.luqiyu.qiyublogspringboot.entity.UniqueView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-13
 */
@Mapper
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取7天用户量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户量
     */
    List<UniqueViewDTO> listUniqueViews(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
