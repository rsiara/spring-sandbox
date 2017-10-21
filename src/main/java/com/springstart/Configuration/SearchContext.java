package com.springstart.Configuration;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration

@EnableSolrRepositories(basePackages = {"com.springstart.Model" }, multicoreSupport = true)
public class SearchContext {

    @Bean
    public HttpSolrClient solrClient(@Value("${solr.host}") String solrHost) {
        return new HttpSolrClient (solrHost);
    }

    @Bean
    @Autowired
    public SolrOperations solrTemplate(HttpSolrClient solrClient) {
        System.out.println("SOLR TEMPLATE ########################");
        return new SolrTemplate(solrClient);
    }

}