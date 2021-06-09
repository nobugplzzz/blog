package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Tag;
import com.luqiyu.qiyublogspringboot.exception.ServeException;
import com.luqiyu.qiyublogspringboot.mapper.TagMapper;
import com.luqiyu.qiyublogspringboot.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-01
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public PageDTO<Tag> listTagBackDTO(ConditionVO condition) {
        //分页查询标签
        // 分页
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        // 条件构造器
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Tag::getId,Tag::getName,Tag::getCreateTime,Tag::getUpdateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()),Tag::getName,condition.getKeywords())
                .orderByDesc(Tag::getUpdateTime);
        Page<Tag> tagPage = tagMapper.selectPage(page, wrapper);
        // 封装的分页类
        List<Tag> records = tagPage.getRecords();
        long count = tagPage.getTotal();
        PageDTO<Tag> pageDTO = new PageDTO<>(records, count);
        return pageDTO;
    }

    @Override
    public void deleteTag(List<Long> tagIdList) {
        // 检查标签下是否有文章
        // ..

        //删除/批量删除
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        // 检查标签名是否存在
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getName,tagVO.getName());

        Integer count = tagMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ServeException("标签名已存在");
        }
        // 构建器，构建对象的
        Tag tag = Tag.builder()
                .id(tagVO.getId())
                .name(tagVO.getName())
                .createTime(Objects.isNull(tagVO.getId()) ? new Date() : null)
                .updateTime(new Date())
                .build();

        this.saveOrUpdate(tag);
    }
}
