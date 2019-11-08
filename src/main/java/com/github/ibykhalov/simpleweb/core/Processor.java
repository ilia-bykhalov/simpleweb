package com.github.ibykhalov.simpleweb.core;

import com.github.ibykhalov.simpleweb.db.IDatabase;
import com.github.ibykhalov.simpleweb.db.UserBalance;
import com.github.ibykhalov.simpleweb.xml.data.Request;
import com.github.ibykhalov.simpleweb.xml.data.RequestType;
import com.github.ibykhalov.simpleweb.xml.data.Response;
import com.github.ibykhalov.simpleweb.xml.data.ResponseCode;

import static com.github.ibykhalov.simpleweb.xml.data.Response.error;
import static com.github.ibykhalov.simpleweb.xml.data.Response.successRegister;

public class Processor implements IProcessor {
    private final IDatabase database;

    public Processor(IDatabase database) {
        this.database = database;
    }

    @Override
    public Response processRequest(Request request) {
        if (request.getRequestType() == RequestType.REGISTER) {
            boolean userRegistered = database.createUser(request.getLogin(), request.getPassword());
            return userRegistered ? successRegister() : error(ResponseCode.USER_ALREADY_EXISTS);
        } else {
            UserBalance userBalance = database.getUserBalance(request.getLogin(), request.getPassword());
            if (userBalance.hasValue()) {
                return Response.successGetBalance(userBalance.getValue());
            } else {
                switch (userBalance.getError()) {
                    case USER_NOT_EXISTS:
                        return Response.error(ResponseCode.USER_NOT_FOUND);

                    case WRONG_PASSWORD:
                        return Response.error(ResponseCode.PASSWORD_INCORRECT);

                    default:
                        return Response.error(ResponseCode.UNKNOWN_ERROR);
                }
            }
        }
    }
}