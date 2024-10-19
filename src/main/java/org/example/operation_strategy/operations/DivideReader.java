package org.example.operation_strategy.operations;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.util.Stack;

public class DivideReader extends OperationReader {
    @Override
    public String getType() {
        return OperationsType.DIVIDE;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws OperationsException {
        initNumbers(stack);
        switch (second) {
            case ComplexNumber complexNumber when first instanceof ComplexNumber -> {
                firstNumber = (ComplexNumber) first;
                secondNumber = complexNumber;
                firstNumber.divide(secondNumber);
                return firstNumber;
            }
            case null, default -> throw new OperationsException("Division is only supported for numbers");
        }
    }
}
