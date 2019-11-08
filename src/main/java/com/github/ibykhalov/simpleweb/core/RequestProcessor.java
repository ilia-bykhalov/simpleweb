package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.data.Request;
import com.github.ibykhalov.simpleweb.data.Response;
import com.github.ibykhalov.simpleweb.data.ResponseCode;
import com.github.ibykhalov.simpleweb.db.IUserInfoDAO;
import com.github.ibykhalov.simpleweb.db.UserBalance;
import com.github.ibykhalov.simpleweb.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);

    private final IUserInfoDAO database;

    public RequestProcessor(IUserInfoDAO database) {
        this.database = database;
    }

    public Response process(Request request) throws DatabaseAccessException {
        switch (request.getRequestType()) {
            case REGISTER:
                return register(request);

            case GET_BALANCE:
                return getBalance(request);

            default:
                logger.error("Unknown request type " + request.getRequestType().name(), new IllegalArgumentException());
                return Response.error(ResponseCode.UNKNOWN_ERROR);
        }
    }

    private Response register(Request request) throws DatabaseAccessException {
        boolean userRegistered = database.createUser(request.getLogin(), request.getPassword());
        return userRegistered
                ? Response.successRegister()
                : Response.error(ResponseCode.USER_ALREADY_EXISTS);
    }

    private Response getBalance(Request request) throws DatabaseAccessException {
        UserBalance userBalance = database.getUserBalance(request.getLogin(), request.getPassword());
        if (userBalance.hasValue()) {
            return Response.successGetBalance(userBalance.getValue());
        } else {
            switch (userBalance.getError()) {
                case USER_NOT_FOUND:
                    return Response.error(ResponseCode.USER_NOT_FOUND);

                case PASSWORD_INCORRECT:
                    return Response.error(ResponseCode.PASSWORD_INCORRECT);

                default:
                    return Response.error(ResponseCode.UNKNOWN_ERROR);
            }
        }
    }
}
