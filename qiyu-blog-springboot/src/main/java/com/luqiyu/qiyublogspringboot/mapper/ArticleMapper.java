package com.luqiyu.qiyublogspringboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.dto.ArticleBackDTO;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-02
 */
@Mapper //springboot识别为mapper，然后动态代理生成实现类，实现类才能操作数据库
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据条件查询后台文章，多表查询？-是
     *
     * @param condition 查询条件
     * @return 后台文章集合
     */
    List<ArticleBackDTO> listArticleBacks(@Param("condition") ConditionVO condition);

    /**
     * 根据条件查询后台文章数量
     *
     * @param condition 查询条件
     * @return
     */
    Long countArticleBacks(@Param("condition") ConditionVO condition);

    /**
     * 查询文章排行
     *
     * @param articleIdList
     * @return
     */
    List<Article> listArticleRank(@Param("articleIdList") List<Integer> articleIdList);
}
