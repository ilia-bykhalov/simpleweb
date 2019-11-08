package com.github.ibykhalov.simpleweb.db;

import com.github.ibykhalov.simpleweb.exception.DatabaseAccessException;

public interface IUserInfoDAO {
    boolean createUser(String login, String password) throws DatabaseAccessException;
    UserBalance getUserBalance(String login, String password) throws DatabaseAccessException;
}
