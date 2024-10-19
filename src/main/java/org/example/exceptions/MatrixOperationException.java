package org.example.exceptions;

public class MatrixOperationException extends Exception {
    public MatrixOperationException(String message) {
        super("Ошибка во время операций над матрицами: " + message);
    }
}