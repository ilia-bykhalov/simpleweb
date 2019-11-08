package com.github.ibykhalov.simpleweb.db;

public interface IDatabase {
    boolean createUser(String login, String password);
    USER_STATUS getUserStatus(String login, String password);

    enum USER_STATUS {
        EXISTS,
        NOT_EXISTS,
        WRONG_PASSWORD
    }
}
