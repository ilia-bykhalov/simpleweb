package com.github.ibykhalov.simpleweb.db;

import com.github.ibykhalov.simpleweb.config.DatasourceConfig;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private final BasicDataSource basicDataSource = new BasicDataSource();

    public DataSource(DatasourceConfig config) {
        basicDataSource.setDriverClassName(DRIVER_CLASS_NAME);
        basicDataSource.setUrl(config.getUrl());
        basicDataSource.setUsername(config.getUser());
        basicDataSource.setPassword(config.getPassword());
        basicDataSource.setInitialSize(config.getConnectionPoolSize());
    }

    public Connection getConnection() throws SQLException {
        return basicDataSource.getConnection();
    }

    public void close() {
        try {
            basicDataSource.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
