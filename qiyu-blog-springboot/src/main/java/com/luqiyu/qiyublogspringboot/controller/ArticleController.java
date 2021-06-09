package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.annotation.OptLog;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.ArticleBackDTO;
import com.luqiyu.qiyublogspringboot.dto.ArticleOptionDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.service.ArticleService;
import com.luqiyu.qiyublogspringboot.vo.ArticleVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.LogicDeleteVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.luqiyu.qiyublogspringboot.constant.OptTypeConst.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-02
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @ApiOperation("查看后台文章列表")
    @GetMapping("/admin/articles")
    public Result<PageDTO<ArticleBackDTO>> listArticleBackDTO(ConditionVO conditionVO){
        return new Result<>(true, StatusCodeConst.OK,"查询成功",articleService.listArticleBackDTO(conditionVO));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改文章置顶")
    //用在 @ApiImplicitParams 注解中，指定一个请求参数的配置信息
    @ApiImplicitParam(name = "articleId",value = "文章id",required = true,dataType = "Long")
    @PutMapping("/admin/articles/top/{articleId}")
    public Result updateArticleTop(@PathVariable("articleId") Long articleId,Integer isTop){
        articleService.updateArticleTop(articleId,isTop);
        return new Result<>(true, StatusCodeConst.OK,"操作成功");
    }

    @ApiOperation("根据id查询后台文章")
    //用在 @ApiImplicitParams 注解中，指定一个请求参数的配置信息
    @ApiImplicitParam(name = "articleId",value = "文章id",required = true,dataType = "Long")
    @GetMapping("/admin/articles/{articleId}")
    public Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Long articleId){
        // 注意，查询的是ArticleVO（新建和编辑的视图对象），返回的是ArticleVO
        return new Result<>(true, StatusCodeConst.OK,"查询成功", articleService.getArticleBackById(articleId));
    }

    @ApiOperation(value = "查询新建/编辑文章选项 ")
    @GetMapping("/admin/articles/options")
    public Result<ArticleOptionDTO> listArticleOptionDTO(){
        return new Result<>(true, StatusCodeConst.OK,"查询成功", articleService.listArticleOptionDTO());
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新建或修改文章 ")
    @PostMapping("/admin/articles")
    public Result saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO){
        articleService.saveOrUpdateArticle(articleVO);
        return new Result<>(true, StatusCodeConst.OK,"操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    public Result deleteArticles(@RequestBody List<Long> articleIdList){
        articleService.deleteArticles(articleIdList);
        return new Result<>(true, StatusCodeConst.OK,"操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章逻辑删除状态，恢复或逻辑删除文章")
    @PutMapping("/admin/articles")
    public Result updateArticleLogicDelete(LogicDeleteVO logicDeleteVO){
        articleService.updateArticleLogicDelete(logicDeleteVO);
        return new Result<>(true, StatusCodeConst.OK,"操作成功");
    }
}

