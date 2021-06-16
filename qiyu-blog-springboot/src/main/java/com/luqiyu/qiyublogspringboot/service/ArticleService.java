package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.*;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ArticleVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.LogicDeleteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-02
 */
public interface ArticleService extends IService<Article> {
    /**
     * 查看后台文章列表
     *
     * @param conditionVO 分页和搜索条件
     * @return
     */
    PageDTO<ArticleBackDTO> listArticleBackDTO(ConditionVO conditionVO);

    /**
     * 修改文章置顶
     *
     * @param articleId 文章id
     * @param isTop     是否置顶
     */
    void updateArticleTop(Long articleId, Integer isTop);

    /**
     * 后台根据id查询文章，编辑文章时查询
     *
     * @param articleId
     * @return 文章
     */
    ArticleVO getArticleBackById(Long articleId);

    /**
     * 查询新建/编辑文章 选项
     *
     * @return 文章选项
     */
    ArticleOptionDTO listArticleOptionDTO();

    /**
     * 新建或更新文章
     *
     * @param articleVO 新建或更新文章的视图对象
     */
    void saveOrUpdateArticle(@Param("articleVO") ArticleVO articleVO);

    /**
     * 物理删除文章，根据ID删除/批量删除
     *
     * @param articleIdList 文章id集合
     */
    void deleteArticles(List<Long> articleIdList);

    /**
     * 修改文章逻辑删除状态，恢复或逻辑删除文章
     *
     * @param logicDeleteVO 逻辑删除状态和id列表
     */
    void updateArticleLogicDelete(LogicDeleteVO logicDeleteVO);

    /**
     * 查询文章归档
     *
     * @param current 当前页码
     * @return 文章
     */
    PageDTO<ArchiveDTO> listArchives(Long current);

    /**
     * 查询前台首页文章
     *
     * @param current 当前页
     * @return 前台首页文章列表
     */
    List<ArticleHomeDTO> listArticles(Long current);

    /**
     * 前台根据id查看文章
     *
     * @param articleId 文章id
     * @return ArticleDTO
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 查看最新文章
     * @return 最新文章
     */
    List<ArticleRecommendDTO> listNewestArticles();
}
