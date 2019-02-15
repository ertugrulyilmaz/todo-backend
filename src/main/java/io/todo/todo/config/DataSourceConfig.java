package io.todo.todo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbConnectionUrl;

    @Value("${spring.datasource.driverClassName}")
    private String dbDriverClassName;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.idle-timeout}")
    private long hikariIdleTimeout;

    @Value("${spring.datasource.minimum-idle}")
    private int hikariMinimumIdle;

    @Value("${spring.datasource.maximum-pool-size}")
    private int hikariMaximumPoolSize;

    @Value("${spring.datasource.connection-timeout}")
    private long hikariConnectionTimeout;

    @Value("${spring.datasource.max-life-time}")
    private long hikariMaxLifetime;

    @Bean
    DataSource primaryDataSource() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbConnectionUrl);
        hikariConfig.setUsername(dbUsername);
        hikariConfig.setPassword(dbPassword);
        hikariConfig.setDriverClassName(dbDriverClassName);

        hikariConfig.setMaximumPoolSize(hikariMaximumPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setConnectionTimeout(hikariConnectionTimeout);
        hikariConfig.setMinimumIdle(hikariMinimumIdle);
        hikariConfig.setIdleTimeout(hikariIdleTimeout);
        hikariConfig.setMaxLifetime(hikariMaxLifetime);

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public Sql2o sql2o(DataSource primaryDataSource) {
        return new Sql2o(primaryDataSource);
    }
}
