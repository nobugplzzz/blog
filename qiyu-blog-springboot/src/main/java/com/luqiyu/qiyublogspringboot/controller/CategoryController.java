package com.luqiyu.qiyublogspringboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.ArticlePreviewListDTO;
import com.luqiyu.qiyublogspringboot.dto.CategoryBackDTO;
import com.luqiyu.qiyublogspringboot.dto.CategoryDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Category;
import com.luqiyu.qiyublogspringboot.mapper.CategoryMapper;
import com.luqiyu.qiyublogspringboot.service.ArticleService;
import com.luqiyu.qiyublogspringboot.service.CategoryService;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.vo.CategoryVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-05-30
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {


    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleService articleService;


    @ApiOperation("查看后台分类列表")
    @GetMapping("/admin/categories")
    public Result<PageDTO<CategoryBackDTO>> listCategoryBackDTO(ConditionVO conditionVO) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", categoryService.listCategoryBackDTO(conditionVO));
    }

    @ApiOperation("新增或修改分类")
    @PostMapping("/admin/categories") // @Valid 是配合CategoryVO的NotBlank()使用
    public Result saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation("删除（根据ID 批量删除）")
    @DeleteMapping("/admin/deleteCategory") // 数组也使用@RequestBody
    public Result deleteCategory(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "查看前台分类列表")
    @GetMapping("/categories")
    public Result<PageDTO<CategoryDTO>> listCategories() {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", categoryService.listCategories());
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/categories/{categoryId}")
    public Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("categoryId") Integer categoryId, Integer current) {
        ConditionVO conditionVO = ConditionVO.builder()
                .categoryId(categoryId)
                .current(current)
                .build();
        return new Result<>(true, StatusCodeConst.OK, "查询成功", articleService.listArticlesByCondition(conditionVO));
    }
}

