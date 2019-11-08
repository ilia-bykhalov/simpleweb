package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.db.IUserInfoDAO;
import com.github.ibykhalov.simpleweb.db.UserBalance;
import com.github.ibykhalov.simpleweb.data.Request;
import com.github.ibykhalov.simpleweb.data.RequestType;
import com.github.ibykhalov.simpleweb.data.Response;
import com.github.ibykhalov.simpleweb.data.ResponseCode;
import com.github.ibykhalov.simpleweb.exception.DatabaseAccessException;

import static com.github.ibykhalov.simpleweb.data.Response.error;
import static com.github.ibykhalov.simpleweb.data.Response.successRegister;

public class RequestProcessor {
    private final IUserInfoDAO database;

    public RequestProcessor(IUserInfoDAO database) {
        this.database = database;
    }

    public Response process(Request request) throws DatabaseAccessException {
        if (request.getRequestType() == RequestType.REGISTER) {
            boolean userRegistered = database.createUser(request.getLogin(), request.getPassword());
            return userRegistered ? successRegister() : error(ResponseCode.USER_ALREADY_EXISTS);
        } else {
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
}
