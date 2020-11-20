package com.julianjupiter.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.webmvc.json.DomainObjectReader;
import org.springframework.data.rest.webmvc.mapping.Associations;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public DomainObjectReader domainObjectReader(PersistentEntities persistentEntities, Associations associations) {
        return new DomainObjectReader(persistentEntities, associations);
    }
}

