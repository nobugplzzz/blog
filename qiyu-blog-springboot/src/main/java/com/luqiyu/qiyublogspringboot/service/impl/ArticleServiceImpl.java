package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.constant.RedisPrefixConst;
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
import com.luqiyu.qiyublogspringboot.service.ArticleTagService;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.util.UserUtil;
import com.luqiyu.qiyublogspringboot.vo.ArticleVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.LogicDeleteVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.beans.Transient;
import java.util.*;
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
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HttpSession session;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


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

        // 分页，不使用Page对象，因为不使用mp的selectPage方法
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticleBacks(condition);
        // 查询文章点赞量和浏览量,entries()获取map中的键值对
        Map<String,Integer> likeCountMap = redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_LIKE_COUNT).entries();
        Map<String,Integer> viewCountMap = redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_VIEWS_COUNT).entries();
        // Iterable接口的forEach方法
        articleBackDTOList.forEach((item)->{
            // Objects.requireNonNull要求不能传null值
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
            item.setViewsCount(Objects.requireNonNull(viewCountMap).get(item.getId().toString()));
        });
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
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleVO.getId()) && articleVO.getIsDraft().equals(CommonConst.FALSE)) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        // 添加文章标签
        if (!articleVO.getTagIdList().isEmpty()) {
            List<ArticleTag> articleTagList = articleVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                    .articleId(article.getId().intValue())
                    .tagId(tagId)
                    .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
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

    @Override
    public List<ArticleHomeDTO> listArticles(Long current) {
        // 转换页码
        Long c = (current - 1) * 10;
        List<ArticleHomeDTO> articleHomeDTOList = articleMapper.listArticles(c);
        return articleHomeDTOList;
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // 更新文章浏览量
        updateArticleViewsCount(articleId);
        // 查询id对应的文章
        ArticleDTO articleDTO = articleMapper.getArticleById(articleId);
        // 查询上一篇下一篇文章
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDeleted, CommonConst.FALSE)
                .eq(Article::getIsDraft, CommonConst.FALSE)
                // lt???? 小于 <
                //例: lt("age", 18)--->age < 18
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                // 无视优化规则直接拼接到 sql 的最后
                .last("limit 1"));
        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDeleted, CommonConst.FALSE)
                .eq(Article::getIsDraft, CommonConst.FALSE)
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));
        articleDTO.setLastArticle(BeanCopyUtil.copyObject(lastArticle, ArticlePaginationDTO.class));
        articleDTO.setNextArticle(BeanCopyUtil.copyObject(nextArticle, ArticlePaginationDTO.class));
        // 查询相关推荐文章
        articleDTO.setArticleRecommendList(articleMapper.listArticleRecommends(articleId));
        // 封装点赞量和浏览量
        articleDTO.setViewsCount((Integer) redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_VIEWS_COUNT).get(articleId.toString()));
        articleDTO.setLikeCount((Integer) redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_LIKE_COUNT).get(articleId.toString()));

        return articleDTO;
    }

    @Override
    public List<ArticleRecommendDTO> listNewestArticles() {
        // 查询最新文章
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                .eq(Article::getIsDeleted, CommonConst.FALSE)
                .eq(Article::getIsDraft, CommonConst.FALSE)
                .orderByDesc(Article::getId)
                .last("limit 5"));
        return BeanCopyUtil.copyList(articleList, ArticleRecommendDTO.class);
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO conditionVO) {
        // 转换页码
        conditionVO.setCurrent((conditionVO.getCurrent() - 1) * 9);
        // 搜索条件对应数据
        List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.listArticlesByCondition(conditionVO);
        // 搜索条件对应名(标签或分类名)
        String name;
        if (Objects.nonNull(conditionVO.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .select(Category::getCategoryName)
                    .eq(Category::getId, conditionVO.getCategoryId()))
                    .getCategoryName();
        } else {
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getName)
                    .eq(Tag::getId, conditionVO.getTagId()))
                    .getName();
        }
        return ArticlePreviewListDTO.builder()
                .articlePreviewDTOList(articlePreviewDTOList)
                .name(name)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveArticleLike(Integer articleId) {
        // 查询当前用户点赞过的文章id集合
        // 文章id集合的key是userInfoId,所以get(userInfoId),这个map的key是ARTICLE_USER_LIKE
        // redisTemplate的Hash类型(key-map)操作,根据key获取值,boundHashOps(K key).get(K key)
        Object object = redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_USER_LIKE)
                .get(UserUtil.getLoginUser().getUserInfoId().toString());
        // 强转Set，作用是存储不重复的元素
        Set<Integer> articleLikeSet = (Set<Integer>) object;

        // 第一次点赞则创建
        if (CollectionUtils.isEmpty(articleLikeSet)) {
            articleLikeSet = new HashSet<>();
        }

        // 判断是否点赞
        if (articleLikeSet.contains(articleId)) {
            // 点过赞则删除文章id
            articleLikeSet.remove(articleId);
            // 文章点赞量-1
            redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_LIKE_COUNT)
                    .increment(articleId.toString(), -1);
        }else{
            // 未点赞则增加文章id
            articleLikeSet.add(articleId);
            // 文章点赞量+1
            redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_LIKE_COUNT)
                    .increment(articleId.toString(), 1);
        }
        // 保存点赞记录
        redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_USER_LIKE)
                .put(UserUtil.getLoginUser().getUserInfoId().toString(),articleLikeSet);
    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchArticle(buildQuery(condition));
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    @Async
    public void updateArticleViewsCount(Integer articleId) {
        // 判断是否第一次访问，增加浏览量
        // 获取HttpSession属性判断
        Object object = session.getAttribute("articleSet");
        // 强转Set，不允许重复
        Set<Integer> set = (Set<Integer>) object;
        //  判断是否第一次访问
        if (Objects.isNull(set)){
            set=new HashSet<>();
        }
        if(!set.contains(articleId)){
            set.add(articleId);
            session.setAttribute("articleSet",set);
            // 浏览量+1
            redisTemplate.boundHashOps(RedisPrefixConst.ARTICLE_VIEWS_COUNT).increment(articleId.toString(),1);
        }
    }

    /**
     * 搜索文章构造
     *
     * @param condition 条件
     * @return es条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(ConditionVO condition) {
        // 条件构造器，构造查询条件，类似mp的条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 根据关键词搜索文章标题或内容
        if (Objects.nonNull(condition.getKeywords())) {
            boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("articleTitle", condition.getKeywords()))
                    .should(QueryBuilders.matchQuery("articleContent", condition.getKeywords())))
                    .must(QueryBuilders.termQuery("isDeleted", CommonConst.FALSE));
        }
        // 查询
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 文章搜索结果高亮
     *
     * @param nativeSearchQueryBuilder es条件构造器
     * @return 搜索结果
     */
    private List<ArticleSearchDTO> searchArticle(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 添加文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("articleTitle");
        titleField.preTags("<span style='color:#f47466'>");
        titleField.postTags("</span>");
        // 添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags("<span style='color:#f47466'>");
        contentField.postTags("</span>");
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        // 搜索
        SearchHits<ArticleSearchDTO> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ArticleSearchDTO.class);
        // 获取搜索结果
        List<SearchHit<ArticleSearchDTO>> hitList = search.getSearchHits();
        // 替换文章内容高亮
        List<ArticleSearchDTO> articleSearchDTOList = hitList.stream().map(hit -> {
            ArticleSearchDTO article = hit.getContent();
            // 获取文章标题高亮数据
            List<String> titleHighLightList = hit.getHighlightFields().get("articleTitle");
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(titleHighLightList)) {
                // 替换标题数据
                article.setArticleTitle(titleHighLightList.get(0));
            }
            // 获取文章内容高亮数据
            List<String> contentHighLightList = hit.getHighlightFields().get("articleContent");
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(contentHighLightList)) {
                // 替换内容数据
                article.setArticleContent(contentHighLightList.get(0));
            }
            return article;
        }).collect(Collectors.toList());

        return articleSearchDTOList;
    }
}

