package com.amazons3utils.interfaces.services;

public interface ILoggerService {
    void info(String message);
    void info(String message, Object... arguments);
    void error(String message);
    void error(String message, Object... arguments);
}