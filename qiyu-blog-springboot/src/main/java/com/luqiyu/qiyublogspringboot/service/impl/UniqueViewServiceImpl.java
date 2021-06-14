package com.luqiyu.qiyublogspringboot.service.impl;

import com.luqiyu.qiyublogspringboot.dto.UniqueViewDTO;
import com.luqiyu.qiyublogspringboot.entity.UniqueView;
import com.luqiyu.qiyublogspringboot.mapper.UniqueViewMapper;
import com.luqiyu.qiyublogspringboot.service.UniqueViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-13
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Autowired
    UniqueViewMapper uniqueViewMapper;


    @Override
    public List<UniqueViewDTO> listUniqueViews() {
        String startTime = DateUtil.getMinTime(DateUtil.getSomeDay(new Date(), -7));
        String endTime = DateUtil.getMaxTime(new Date());
        return uniqueViewMapper.listUniqueViews(startTime, endTime);
    }
}
