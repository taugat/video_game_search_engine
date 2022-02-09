package fr.lernejo.search.api;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("elastic_search")
public class ElasticSearchConfiguration
{

    @Value("${elasticsearch.userName}") private String userName;
    @Value("${elasticsearch.password}") private String password;

    @Value("${elasticsearch.hostname}") private String hostname;
    @Value("${elasticsearch.port}") private int port;

    @Bean
    public CredentialsProvider getElasticSearchCredentialsProvider()
    {
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        return basicCredentialsProvider;
    }

    @Bean
    public HttpHost getElasticSearchHttpHost()
    {
        return new HttpHost(hostname, port);
    }

    @Bean
    public RestHighLevelClient getRestHighLevelClient(HttpHost httpHost, CredentialsProvider credentialsProvider)
    {
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost)
            .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(restClientBuilder);
    }

}
