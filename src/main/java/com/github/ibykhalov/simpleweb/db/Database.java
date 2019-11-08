package com.github.ibykhalov.simpleweb.db;

import java.sql.*;
import java.util.UUID;
import java.util.function.Function;

public class Database implements IDatabase {
    private static final String CREATE_USER_QUERY =
            "insert into userinfo(id,login,pass,balance) values(?,?,?,?) ON CONFLICT DO NOTHING;";

    @Override
    public boolean createUser(String login, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5400/billing_test",
                                                                 "postgres", "docker"
        )) {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER_QUERY);
            statement.setObject(1, UUID.randomUUID());
            statement.setString(2,login);
            statement.setString(3,password);
            statement.setInt(4,0);

            boolean execute = statement.execute();
            int updateCount = statement.getUpdateCount();
            System.out.println(updateCount);
            return updateCount == 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public USER_STATUS getUserStatus(String login, String password) {
        return USER_STATUS.NOT_EXISTS;
    }

    public  void truncate() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5400/billing_test",
                                                                 "postgres", "docker"
        )) {
            Statement statement = connection.createStatement();
            statement.execute("truncate table userinfo;");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public interface  IResultProcessor<T>{
        T process(ResultSet resultSet) throws SQLException;
    }
}
