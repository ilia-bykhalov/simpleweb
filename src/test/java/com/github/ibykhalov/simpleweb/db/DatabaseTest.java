package com.github.ibykhalov.simpleweb.db;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
    private IDatabase database;

    @Before
    public void setUp() throws Exception {
        Database testDatabase = new Database();
        testDatabase.truncate();
        database = testDatabase;
    }

    @Test
    public void createUser() {
        boolean userCreated = database.createUser("employee1", "employee1_pass");
        assertTrue(userCreated);
    }

    @Test
    public void createUserTwice() {
        boolean userCreated = database.createUser("employee1", "employee1_pass");
        assertTrue(userCreated);

        boolean userCreatedAgain = database.createUser("employee1", "321");
        assertFalse(userCreatedAgain);
    }

    @Test
    public void getBalanceOnUnknownUser() {
        UserBalance userBalance = database.getUserBalance("employee1", "employee1_pass");
        assertEquals(UserBalance.error(GetBalanceError.USER_NOT_EXISTS), userBalance);
    }

    @Test
    public void getBalanceOnCreatedUser() {
        database.createUser("employee1", "employee1_pass");

        UserBalance userBalance = database.getUserBalance("employee1", "employee1_pass");
        assertEquals(UserBalance.value(0), userBalance);
    }

    @Test
    public void getBalanceWrongPassword() {
        database.createUser("employee1", "employee1_pass");

        UserBalance userBalance = database.getUserBalance("employee1", "employee1_wrong_pass");
        assertEquals(UserBalance.error(GetBalanceError.WRONG_PASSWORD), userBalance);
    }
}
