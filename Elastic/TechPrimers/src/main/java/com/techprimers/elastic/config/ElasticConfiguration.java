package com.techprimers.elastic.config;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.File;
import java.io.IOException;

/** This is config for elastic search  */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.techprimers.elastic.repository")
public class ElasticConfiguration {


    // this bean needed by later....
    @Bean
    public NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws IOException {
        // creating temp dir for storing        
        File tmpDir = File.createTempFile("elastic", Long.toString(System.nanoTime()));
        System.out.println("Temp directory: " + tmpDir.getAbsolutePath());
        
        // this settings are required to ES kickstart  (later it was in XML, but no with Spring Boot)
        Settings.Builder elasticsearchSettings =
                Settings.settingsBuilder()
                        .put("http.enabled", "true") // 1
                        .put("index.number_of_shards", "1") // some kind of shared partition in the ES
                        // ElasticSerach is gonna save everything on disk to in-memory!
                        .put("path.data", new File(tmpDir, "data").getAbsolutePath()) // 2: path to directory, where elastic should be saved
                        .put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) // 2
                        .put("path.work", new File(tmpDir, "work").getAbsolutePath()) // 2
                        .put("path.home", tmpDir); // 3


        // this is specific to Spring Data
        return new ElasticsearchTemplate(nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node()
                .client());
    }
}
