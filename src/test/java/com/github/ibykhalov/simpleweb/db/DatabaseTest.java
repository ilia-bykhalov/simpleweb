package com.github.ibykhalov.simpleweb.db;

import com.github.ibykhalov.simpleweb.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static com.github.ibykhalov.simpleweb.TestUtils.TEST_DATASOURCE_CONFIG;
import static org.junit.Assert.*;

public class DatabaseTest {
    private IUserInfoDAO database;

    @Before
    public void setUp() throws Exception {
        TestUtils.truncateTable();
        database = new UserInfoDAO(new DataSource(TEST_DATASOURCE_CONFIG));
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
        assertEquals(UserBalance.error(GetBalanceError.USER_NOT_FOUND), userBalance);
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
        assertEquals(UserBalance.error(GetBalanceError.PASSWORD_INCORRECT), userBalance);
    }
}
