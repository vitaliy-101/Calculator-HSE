package org.example.operation_strategy.operations;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.util.Stack;

public class SubtractReader extends OperationReader {
    @Override
    public String getType() {
        return OperationsType.SUBTRACT;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException {
        initNumbers(stack);
        switch (second) {
            case ComplexNumber complexNumber when first instanceof ComplexNumber -> {
                firstNumber = (ComplexNumber) first;
                secondNumber = complexNumber;
                firstNumber.subtract(secondNumber);
                return firstNumber;
            }
            case Matrix matrix when first instanceof Matrix -> {
                firstMatrix = (Matrix) first;
                secondMatrix = matrix;
                firstMatrix.subtractMatrix(secondMatrix);
                return firstMatrix;
            }
            case null, default -> throw new OperationsException("Operation is not correct");
        }
    }
}
