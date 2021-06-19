package com.luqiyu.qiyublogspringboot.handler;

import com.luqiyu.qiyublogspringboot.constant.RedisPrefixConst;
import com.luqiyu.qiyublogspringboot.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * request监听
 *
 * @author: 启誉
 * @create: 2021-06-19
 **/
public class ServletRequestListenerImpl implements ServletRequestListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute("ip");
        // 判断当前ip是否访问，增加访问量
        String ipAddr = IpUtil.getIpAddr(request);
        if (!ipAddr.equals(ip)) {
            session.setAttribute("ip", ipAddr);
            // 是boundValueOps
            redisTemplate.boundValueOps(RedisPrefixConst.BLOG_VIEWS_COUNT).increment(1);
        }
        // 将ip存入redis，统计每日用户量,是boundSetOps
        redisTemplate.boundSetOps(RedisPrefixConst.IP_SET).add(ipAddr);
    }
}
