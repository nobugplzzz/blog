package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.CategoryBackDTO;
import com.luqiyu.qiyublogspringboot.dto.CategoryDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.CategoryVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-05-30
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询后台分类，泛型是Category因编辑需要id
     *
     * @param conditionVO 条件
     * @return 分类列表
     */
    PageDTO<Category> listCategoryBackDTO(ConditionVO conditionVO);

    /**
     * 添加或修改分类
     * @param categoryVO 分类
     */
    void saveOrUpdateCategory(CategoryVO categoryVO);

    /**
     * 删除（根据ID 批量删除）
     * @param categoryIdList 分类集合
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 查询前台分类列表
     * @return 分类列表
     */
    PageDTO<CategoryDTO> listCategories();
}
