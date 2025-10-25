package com.skillextractor.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl != null && databaseUrl.startsWith("postgres://")) {
            // Konwertuj format Render → JDBC
            databaseUrl = databaseUrl.replace("postgres://", "jdbc:postgresql://");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(databaseUrl);
            config.setMaximumPoolSize(5);

            return new HikariDataSource(config);
        }

        throw new IllegalStateException("DATABASE_URL not found or invalid");
    }
}