package com.luqiyu.qiyublogspringboot.config;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * elasticsearch配置类
 *
 * @author: 启誉
 * @create: 2021-06-20
 **/
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

    @Resource //按名称匹配？
    private RestClientBuilder restClientBuilder;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig ->
                requestConfig.setKeepAliveStrategy((response, context) -> TimeUnit.MINUTES.toMillis(3))));
    }
}
