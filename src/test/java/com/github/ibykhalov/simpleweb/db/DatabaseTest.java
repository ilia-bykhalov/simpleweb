package com.github.ibykhalov.simpleweb.db;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
    @Before
    public void setUp() throws Exception {
        new Database().truncate();
    }

    @Test
    public void createUser() {
        IDatabase database = new Database();

        boolean userCreated = database.createUser("123", "123");
        assertTrue(userCreated);
    }

    @Test
    public void createUserTwice() {
        IDatabase database = new Database();
        boolean userCreated = database.createUser("123", "123");
        assertTrue(userCreated);

        boolean userCreatedAgain = database.createUser("123", "321");
        assertFalse(userCreatedAgain);
    }
}