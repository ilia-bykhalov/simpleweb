package com.github.ibykhalov.simpleweb.db;

import com.github.ibykhalov.simpleweb.exception.DatabaseAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class UserInfoDAO implements IUserInfoDAO {
    private static final String CREATE_USER_QUERY =
            "insert into userinfo(id,login,pass,balance) values(?,?,?,?) ON CONFLICT DO NOTHING;";

    private static final String GET_BALANCE_QUERY = "select balance, pass from userinfo where login=?";

    private static final int DEFAULT_BALANCE = 0;

    private final DataSource dataSource;

    public UserInfoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean createUser(String login, String password) throws DatabaseAccessException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER_QUERY);

            int index = 1;
            statement.setObject(index++, UUID.randomUUID());
            statement.setString(index++, login);
            statement.setString(index++, password);
            statement.setInt(index++, DEFAULT_BALANCE);

            statement.execute();
            int updateCount = statement.getUpdateCount();
            return updateCount == 1;

        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public UserBalance getUserBalance(String login, String password) throws DatabaseAccessException {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_BALANCE_QUERY);

            statement.setString(1, login);

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                int balance = resultSet.getInt(1);
                String pass = resultSet.getString(2);
                if (password.equals(pass)) {
                    return UserBalance.value(balance);
                } else {
                    return UserBalance.error(GetBalanceError.PASSWORD_INCORRECT);
                }
            } else {
                return UserBalance.error(GetBalanceError.USER_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }
}
