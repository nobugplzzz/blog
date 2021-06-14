package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.UniqueViewDTO;
import com.luqiyu.qiyublogspringboot.entity.UniqueView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-13
 */
public interface UniqueViewService extends IService<UniqueView> {
    /**
     * 获取7天用户量统计
     * @return 用户量
     */
    List<UniqueViewDTO> listUniqueViews();

}
