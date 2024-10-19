package org.example;

import org.example.exceptions.MatrixOperationException;
import org.example.exceptions.NumberValidityException;
import org.example.exceptions.ParserException;

import javax.management.OperationsException;

public class Main {
    public static void main(String[] args) throws NumberValidityException, ParserException {
        var calculator = new Calculator();
        calculator.getResult();

    }
}