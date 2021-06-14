package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.dto.ArticleRankDTO;
import com.luqiyu.qiyublogspringboot.dto.BlogBackInfoDTO;
import com.luqiyu.qiyublogspringboot.dto.CategoryBackDTO;
import com.luqiyu.qiyublogspringboot.dto.UniqueViewDTO;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.luqiyu.qiyublogspringboot.mapper.ArticleMapper;
import com.luqiyu.qiyublogspringboot.mapper.CategoryMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.service.BlogInfoService;
import com.luqiyu.qiyublogspringboot.service.UniqueViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 博客信息服务实现类
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
@Service
public class BlogInfoSerciveImpl implements BlogInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private UniqueViewService uniqueViewService;
    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
//        Integer viewsCount = (Integer) redisTemplate.boundValueOps(BLOG_VIEWS_COUNT).get();
        // 查询留言量
//        Integer messageCount = messageDao.selectCount(null);
        // 查询用户量
        Integer userCount = userInfoMapper.selectCount(null);
        // 查询文章量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDeleted, CommonConst.FALSE)
                .eq(Article::getIsDraft, CommonConst.FALSE));
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // 查询分类数据
        List<CategoryBackDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        // 查询redis访问量前五的文章
//        Map<String, Integer> articleViewsMap = redisTemplate.boundHashOps(ARTICLE_VIEWS_COUNT).entries();
        // 将文章进行倒序排序
//        List<Integer> articleIdList = Objects.requireNonNull(articleViewsMap).entrySet().stream()
//                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                .map(item -> Integer.valueOf(item.getKey()))
//                .collect(Collectors.toList());
        // 提取前五篇文章
//        int index = Math.min(articleIdList.size(), 5);
//        articleIdList = articleIdList.subList(0, index);
//        // 文章为空直接返回
//        if (articleIdList.isEmpty()) {
//            return BlogBackInfoDTO.builder()
//                    .viewsCount(viewsCount)
//                    .messageCount(messageCount)
//                    .userCount(userCount)
//                    .articleCount(articleCount)
//                    .categoryDTOList(categoryDTOList)
//                    .uniqueViewDTOList(uniqueViewList)
//                    .build();
//        }
        // 查询文章标题
//        List<Article> articleList = articleMapper.listArticleRank(articleIdList);
        // 封装浏览量
//        List<ArticleRankDTO> articleRankDTOList = articleList.stream().map(article -> ArticleRankDTO.builder()
//                .articleTitle(article.getArticleTitle())
//                .viewsCount(articleViewsMap.get(article.getId().toString()))
//                .build())
//                .collect(Collectors.toList());
        return BlogBackInfoDTO.builder()
//                .viewsCount(viewsCount)
//                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .uniqueViewDTOList(uniqueViewList)
//                .articleRankDTOList(articleRankDTOList)
                .build();
    }

}
