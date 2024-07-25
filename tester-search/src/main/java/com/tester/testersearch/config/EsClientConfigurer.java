package com.tester.testersearch.config;

import com.tester.testersearch.service.ESClientInterface;
import com.tester.testersearch.util.ESClient;
import com.tester.testersearch.util.ESClient_Bonsai;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsClientConfigurer {

    @Bean("esClient")
    @ConditionalOnProperty(name = "my.es.client.type", havingValue = "default", matchIfMissing = true)
    public ESClientInterface getEsClient() {
        ESClient esClient = new ESClient();
        return esClient;
    }

    @Bean("esClient")
    @ConditionalOnProperty(name = "my.es.client.type", havingValue = "bonsai", matchIfMissing = true)
    public ESClientInterface getEsClient_Bonsai() {
        ESClient_Bonsai esClient = new ESClient_Bonsai();
        return esClient;
    }
}
