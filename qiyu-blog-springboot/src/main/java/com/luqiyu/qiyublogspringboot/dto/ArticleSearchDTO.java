package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 文章搜索结果传输对象，用于es搜索文章结果
 *
 * @author: 启誉
 * @create: 2021-06-19
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "article") //es文档 (行),所属索引是“article”
public class ArticleSearchDTO {
    /**
     * 文章id
     */
    @Id
    private Integer id;

    // es fields（字段）type类型，analyzer分词器：ik_max_word：最细粒度划分
    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;
    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleContent;

    /**
     * 文章状态
     */
    @Field(type = FieldType.Integer)
    private Integer isDelete;
}
