package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.ArticlePreviewListDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.TagDTO;
import com.luqiyu.qiyublogspringboot.entity.Tag;
import com.luqiyu.qiyublogspringboot.service.ArticleService;
import com.luqiyu.qiyublogspringboot.service.TagService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import com.luqiyu.qiyublogspringboot.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-01
 */
@Api(tags="标签模块")
@RestController
public class TagController {

    @Autowired
    TagService tagService;
    @Autowired
    ArticleService articleService;

    @ApiOperation(value = "查看后台标签列表")
    @GetMapping("/admin/tags")
    public Result<PageDTO<Tag>> listTagBackDTO(ConditionVO condition) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", tagService.listTagBackDTO(condition));
    }

    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    public Result saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }


    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    public Result deleteTag(@RequestBody List<Long> tagIdList) {
        tagService.deleteTag(tagIdList);
        return new Result<>(true, StatusCodeConst.OK, "删除成功");
    }

    @ApiOperation(value = "查看前台标签列表")
    @GetMapping("/tags")
    public Result<PageDTO<TagDTO>> listTags() {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", tagService.listTags());
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/tags/{tagId}")
    public Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("tagId") Integer tagId, Integer current) {
        ConditionVO conditionVO = ConditionVO.builder()
                .tagId(tagId)
                .current(current)
                .build();
        return new Result<>(true, StatusCodeConst.OK, "查询成功", articleService.listArticlesByCondition(conditionVO));
    }
}

