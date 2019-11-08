package com.github.ibykhalov.simpleweb.config;

public final class EmployeeServerConfig {
    private final int serverPort;
    private final int workersCount;
    private final DatasourceConfig datasourceConfig;

    public EmployeeServerConfig(int serverPort, int workersCount, DatasourceConfig datasourceConfig) {
        this.serverPort = serverPort;
        this.workersCount = workersCount;
        this.datasourceConfig = datasourceConfig;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getWorkersCount() {
        return workersCount;
    }

    public DatasourceConfig getDatasourceConfig() {
        return datasourceConfig;
    }
}
