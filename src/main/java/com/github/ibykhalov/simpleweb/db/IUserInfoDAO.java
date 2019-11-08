package com.github.ibykhalov.simpleweb.db;

public interface IUserInfoDAO {
    boolean createUser(String login, String password);
    UserBalance getUserBalance(String login, String password);
}
