package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.CategoryVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.TagVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-01
 */
public interface TagService extends IService<Tag> {


    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return 标签列表
     */
    PageDTO<Tag> listTagBackDTO(ConditionVO condition);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Long> tagIdList);

    /**
     * 保存或更新标签
     * @param tagVO 标签
     */
    void saveOrUpdateTag(TagVO tagVO);

}
