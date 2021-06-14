package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.dto.CategoryBackDTO;
import com.luqiyu.qiyublogspringboot.dto.CategoryDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.luqiyu.qiyublogspringboot.entity.Category;
import com.luqiyu.qiyublogspringboot.exception.ServeException;
import com.luqiyu.qiyublogspringboot.mapper.ArticleMapper;
import com.luqiyu.qiyublogspringboot.mapper.CategoryMapper;
import com.luqiyu.qiyublogspringboot.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.vo.CategoryVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.rmi.ServerException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-05-30
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public PageDTO<Category> listCategoryBackDTO(ConditionVO condition) {
        // 分页查询后台分类
        Page page = new Page(condition.getCurrent(), condition.getSize());
        // 条件构造器（Lambda）
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper
                // select("id", "name", "age")
                .select(Category::getId, Category::getCategoryName, Category::getCreateTime, Category::getUpdateTime)
                // like(boolean condition, R column, Object val),condition=true： LIKE '%值%
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName, condition.getKeywords())
                .orderByDesc(Category::getId);
        // 根据 entity 条件，查询全部记录（并翻页）IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
        Page<Category> categoryPage = categoryMapper.selectPage(page, wrapper);
        // 返回分页数据
        PageDTO<Category> pageDTO = new PageDTO<>(categoryPage.getRecords(), categoryPage.getTotal());

        return pageDTO;
    }

    @Override
    public void saveOrUpdateCategory(CategoryVO categoryVO) {
        // 判断分类是否重复
        // 条件构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        // eq equal 等于
        wrapper.eq(Category::getCategoryName, categoryVO.getName());
        Integer count = categoryMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServeException("分类名已存在");
        }
        // 构建器，构建对象
        Category category = Category.builder()
                .id(categoryVO.getId())
                .categoryName(categoryVO.getName())
                .createTime(Objects.isNull(categoryVO.getId()) ? new Date() : null)
                .updateTime(new Date())
                .build();
        // this是categoryService，saveOrUpdate是service接口的方法
        this.saveOrUpdate(category);

    }

    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 查询分类下是否有文章
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (count > 0) {
            new ServeException("删除失败，该分类下存在文章");
        }

        categoryMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    public PageDTO<CategoryDTO> listCategories() {
        List<CategoryBackDTO> categoryBackDTOList = categoryMapper.listCategoryDTO();
        List<CategoryDTO> categoryDTOS = BeanCopyUtil.copyList(categoryBackDTOList, CategoryDTO.class);
        Long count = categoryMapper.selectCount(null).longValue();
        return new PageDTO<>(categoryDTOS,count);
    }

}
