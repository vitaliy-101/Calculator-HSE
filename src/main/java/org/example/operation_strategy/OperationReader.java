package org.example.operation_strategy;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class OperationReader {
    protected Object second;
    protected Object first;
    protected ComplexNumber firstNumber;
    protected ComplexNumber secondNumber;
    protected Matrix firstMatrix;
    protected Matrix secondMatrix;

    protected void initNumbers(Stack<Object> stack) {
        second = getNumberFromStack(stack);
        first = getNumberFromStack(stack);
        firstNumber = new ComplexNumber();
        secondNumber = new ComplexNumber();
        firstMatrix = new Matrix();
        secondMatrix = new Matrix();
    }

    protected void initNumber(Stack<Object> stack) {
        first = getNumberFromStack(stack);
        firstNumber = new ComplexNumber();
        firstMatrix = new Matrix();
    }

    protected Object getNumberFromStack(Stack<Object> stack) {
        return !stack.isEmpty() ? stack.pop() : null;
    }
    public abstract String getType();

    public abstract Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException;
}
