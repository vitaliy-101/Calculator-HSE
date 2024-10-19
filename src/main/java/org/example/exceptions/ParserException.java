package org.example.exceptions;
public class ParserException extends Exception {
    public ParserException(String message) {
        super("Ошибка во время парсинга выражения: " + message);
    }
}