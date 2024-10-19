package org.example.exceptions;

public class NumberValidityException extends Exception {
    public NumberValidityException(String message) {
        super("Ошибка во время чтения чисел: " + message);
    }
}
