package com.github.ibykhalov.simpleweb.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import java.io.File;

import static com.google.common.base.Preconditions.checkState;

public final class ConfigLoader {
    private ConfigLoader() {
        throw new UnsupportedOperationException();
    }

    public static EmployeeServerConfig fromArgs(String[] args) {
        checkState(args.length < 2, "set config file, or leave args empty for default_employee_server.conf");
        String configPath;
        if (args.length == 1) {
            configPath = args[0];
        } else {
            configPath = "default_employee_server.conf";
        }

        try {
            File file = new File(configPath);
            checkState(file.exists(), "File \"" + configPath + "\" not found");
            Config config = ConfigFactory.parseFile(file);
            int serverPort = config.getInt("employee-server-config.serverPort");
            int workersCount = config.getInt("employee-server-config.workersCount");

            Config rawDsConfig = config.getConfig("employee-server-config.datasource");

            String datasourceUrl = rawDsConfig.getString("url");
            String datasourceUser = rawDsConfig.getString("user");
            String datasourcePassword = rawDsConfig.getString("password");
            int databaseConnectionPoolSize = rawDsConfig.getInt("connectionPoolSize");

            DatasourceConfig datasourceConfig =
                    new DatasourceConfig(datasourceUrl, datasourceUser, datasourcePassword, databaseConnectionPoolSize);
            return new EmployeeServerConfig(serverPort, workersCount, datasourceConfig);
        } catch (ConfigException ex) {
            throw new IllegalArgumentException("wrong config file: " + configPath, ex);
        }
    }
}
