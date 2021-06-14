package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.CategoryBackDTO;
import com.luqiyu.qiyublogspringboot.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-01
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 查询分类和对应文章数量
     * @return 分类集合
     */
    List<CategoryBackDTO> listCategoryDTO();
}
