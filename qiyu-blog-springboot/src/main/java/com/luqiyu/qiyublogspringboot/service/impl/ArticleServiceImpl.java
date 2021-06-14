package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.dto.*;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.luqiyu.qiyublogspringboot.entity.ArticleTag;
import com.luqiyu.qiyublogspringboot.entity.Category;
import com.luqiyu.qiyublogspringboot.entity.Tag;
import com.luqiyu.qiyublogspringboot.mapper.ArticleMapper;
import com.luqiyu.qiyublogspringboot.mapper.ArticleTagMapper;
import com.luqiyu.qiyublogspringboot.mapper.CategoryMapper;
import com.luqiyu.qiyublogspringboot.mapper.TagMapper;
import com.luqiyu.qiyublogspringboot.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.vo.ArticleVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.LogicDeleteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-02
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    ArticleService articleService;

    @Override
    public PageDTO<ArticleBackDTO> listArticleBackDTO(ConditionVO condition) {
        // 分页查询后台文章,ArticleBackDTO有一对多，多对多，需要多表查询文章分类、标签，得自己写sql

        // 转换页码，利用设置起始点，上一页*页码大小 就是当前页起点
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 查询文章总量
        Long count = articleMapper.countArticleBacks(condition);
        if (count == 0) {
            return new PageDTO<>();
        }

        // 分页，不使用Page对象
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticleBacks(condition);

        return new PageDTO<>(articleBackDTOList, count);
    }


    @Override
    public void updateArticleTop(Long articleId, Integer isTop) {
        // 修改文章置顶状态
        // builder 构建器 构建对象 更新，
        Article article = Article.builder()
                .id(articleId)
                .isTop(isTop)
                .build();
        // updateById是只更新变更的部分
        articleMapper.updateById(article);
    }

    @Override
    public ArticleVO getArticleBackById(Long articleId) {
        // 查询文章信息
        // 注意是查询ArticleVO，所以需要条件构造器只查询ArticleVO的属性
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper
                // select("id"."title"....)
                .select(Article::getId, Article::getArticleTitle, Article::getArticleContent, Article::getArticleCover, Article::getCategoryId, Article::getIsTop, Article::getIsDraft)
                // equal(‘id’=‘’) 相当于 where id=‘id’
                .eq(Article::getId, articleId);
        Article article = articleMapper.selectOne(wrapper);

        // 查询文章标签，ArticleVO的标签属性是tagIdList集合，所以需要转化
        // 先根据文章id查文章对应的ArticleTag对象
        LambdaQueryWrapper<ArticleTag> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2
                // select("TagId")
                .select(ArticleTag::getTagId)
                // where ArticleId=‘id’
                .eq(ArticleTag::getArticleId, article.getId());
        List<ArticleTag> articleTagList = articleTagMapper.selectList(wrapper2);

        // 再根据ArticleTag对象查标签id
        // 流 操作数据，获取文章标签id集合，常规方法是创建空id集合，for循环遍历articleTagList
        List<Integer> tagIdList = articleTagList
                // 为集合创建串行流。
                .stream()
                // map 方法用于映射每个元素到对应的结果
                .map(ArticleTag::getTagId)
                // Collectors 可用于返回列表或字符串：
                .collect(Collectors.toList());

        // 构建器 构建ArticleVO对象
        ArticleVO articleVO = ArticleVO.builder()
                .id(article.getId())
                .articleTitle(article.getArticleTitle())
                .articleContent(article.getArticleContent())
                .articleCover(article.getArticleCover())
                .categoryId(article.getCategoryId())
                .isDraft(article.getIsDraft())
                .isTop(article.getIsTop())
                .tagIdList(tagIdList)
                .build();

        return articleVO;
    }

    @Override
    public ArticleOptionDTO listArticleOptionDTO() {
        // 查询新建/编辑文章选项

        // 先查询，再复制属性
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getCategoryName));
        List<CategoryBackDTO> categoryBackDTOList = BeanCopyUtil.copyList(categoryList, CategoryBackDTO.class);

        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getName));
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);

        // 构建器
        ArticleOptionDTO articleOptionDTO = ArticleOptionDTO.builder()
                .tagDTOList(tagDTOList)
                .categoryBackDTOList(categoryBackDTOList)
                .build();
        return articleOptionDTO;
    }

    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 直接构建器
        Article article = Article.builder()
                .id(articleVO.getId())
                .categoryId(articleVO.getCategoryId())
                .articleCover(articleVO.getArticleCover())
                .articleTitle(articleVO.getArticleTitle())
                .articleContent(articleVO.getArticleContent())
                .createTime(Objects.isNull(articleVO.getId()) ? new Date() : null)
                .updateTime(Objects.nonNull(articleVO.getId()) ? new Date() : null)
                .isTop(articleVO.getIsTop())
                .isDraft(articleVO.getIsDraft())
                .build();
        this.saveOrUpdate(article);
    }

    @Override
    public void deleteArticles(List<Long> articleIdList) {
        // 删除文章标签关联,即文章标签表的相关内容
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    @Override
    public void updateArticleLogicDelete(LogicDeleteVO logicDeleteVO) {
        List<Long> idList = logicDeleteVO.getIdList();

        // id列表的流操作使得id列表每个id对应一个指定Article，返回Article集合
        // 每个Article再利用构建器设置更新的状态
        List<Article> articleList = idList.stream().map(id -> Article.builder()
                .id(id)
                .isTop(0)
                .isDeleted(logicDeleteVO.getIsDeleted())
                .build()).collect(Collectors.toList());
        // articleService是this，也可以@AutoWire使用
        articleService.updateBatchById(articleList);
    }

    @Override
    public PageDTO<ArchiveDTO> listArchives(Long current) {
        Page<Article> page = new Page<>(current, 10);
        // 获取分页数据
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDeleted, CommonConst.FALSE)
                .eq(Article::getIsDraft, CommonConst.FALSE));
        // 拷贝dto集合
        List<ArchiveDTO> archiveDTOList = BeanCopyUtil.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageDTO<>(archiveDTOList, articlePage.getTotal());
    }
}

