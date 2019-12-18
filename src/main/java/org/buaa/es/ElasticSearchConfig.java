package org.buaa.es;

import org.springframework.boot.SpringBootConfiguration;

import javax.annotation.PostConstruct;

@SpringBootConfiguration
public class ElasticSearchConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}