package org.example.bookstore.config.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Profile({"dev", "test"})
@Configuration
@ComponentScan({"org.example.bookstore"})
@EntityScan({"org.example.bookstore.model"})
@EnableJpaRepositories(basePackages = {"org.example.bookstore.repository"})
@RequiredArgsConstructor
@EnableJpaAuditing
public class DataSourceConfig {

    private final AppPropertyConfig propertyConfig;

    @Bean
    @Primary
    @Profile({"dev", "test"})
    public DataSource dataSource() {
        return this.getHikariDataSource();
    }

    private HikariDataSource getHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(propertyConfig.getDatabaseDriver());
        dataSource.setJdbcUrl(propertyConfig.getDatabaseUrl());
        dataSource.setUsername(propertyConfig.getDatabaseUsername());
        dataSource.setPassword(propertyConfig.getDatabasePassword());
        dataSource.setConnectionTimeout(30000L);
        return dataSource;
    }

}
