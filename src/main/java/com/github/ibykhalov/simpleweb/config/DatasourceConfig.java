package com.github.ibykhalov.simpleweb.config;

public class DatasourceConfig {
    private final String url;
    private final String user;
    private final String password;
    private final int connectionPoolSize;

    public DatasourceConfig(String url, String user, String password, int connectionPoolSize) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPoolSize = connectionPoolSize;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }
}
