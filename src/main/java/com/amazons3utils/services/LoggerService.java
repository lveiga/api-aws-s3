package com.amazons3utils.services;

import org.springframework.stereotype.Service;

import com.amazons3utils.interfaces.services.ILoggerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class LoggerService implements ILoggerService{

    private static final Logger log = LoggerFactory.getLogger(LoggerService.class);


    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Object... arguments) {

        if (arguments != null) {
            log.info(message, arguments);
        }
        else
            info(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void error(String message, Object... arguments) {
        if (arguments != null) {
            log.error(message, arguments);
        }
        else
            error(message);
    }

}

