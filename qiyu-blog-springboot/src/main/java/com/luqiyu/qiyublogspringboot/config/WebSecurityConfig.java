package com.luqiyu.qiyublogspringboot.config;

import com.luqiyu.qiyublogspringboot.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * SpringSecurity配置类，配置认证和权限
 *
 * @author: 启誉
 * @create: 2021-06-10
 **/
@Configuration
@EnableWebSecurity //开启WebSecurity模式
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    // FilterInvocationSecurityMetadataSource实现类，需自定义，获取url需要的权限（角色）
    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new FilterInvocationSecurityMetadataSourceImpl();
    }

    // AccessDecisionManager实现类，需自定义，查看当前用户是否具备上述权限
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AccessDecisionManagerImpl();
    }

    // 现在我们有这样一个业务场景，我允许用户多次登录，同时登录的用户可以查看相同的账号在
    // 不同的地方的登录session，并且对这些session进行管理，比如查看，删除等操作；如何使用
    // spring security实现上述功能？就这个方法
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 单用户并发session控制 确保单个用户的单个账号，只有一个活跃的session，这也是一个常见的需求
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 密码加密后，登录处理
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 用于在手动加入账号和无加密密码的条件下测试，表示此密码编辑器什么都不做
        // return NoOpPasswordEncoder.getInstance();
    }


    /**
     * SpringSecurity认证和授权配置方法
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置认证
        // 登录注销
        http.formLogin()
                .loginProcessingUrl("/login") // 指定验证凭据的url
                .successHandler(authenticationSuccessHandler) // 登录成功处理器
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler);
        // 配置授权
        // 角色和菜单路由的权限处理
        http.authorizeRequests()
                // 动态权限
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        // 设置“通过当前的请求地址，获取该地址需要的用户权限（角色）“的方法
                        fsi.setSecurityMetadataSource(securityMetadataSource());
                        // 设置"查看当前用户是否具备上述权限"的方法
                        fsi.setAccessDecisionManager(accessDecisionManager());
                        return fsi;
                    }
                })
                // 设置url权限之后需设置允许所有
                .anyRequest().permitAll()
                .and()
                // 关闭跨站请求防护
                .csrf().disable().exceptionHandling()
                // 未登录处理
                .authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足处理
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                //开启frame嵌入
                .headers().frameOptions().disable()
                .and()
                // 控制session
                .sessionManagement()
                // session并发登录处理 ，同时可以20个用户在线
                .maximumSessions(20)
                // 允许用户多次多地登录
                .sessionRegistry(sessionRegistry());
    }
}
