package org.example.operation_strategy.operations;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import java.util.Stack;

public class TildaReader extends OperationReader {
    @Override
    public String getType() {
        return OperationsType.TILDA;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException {
        initNumber(stack);
        if (first instanceof Matrix) {
            firstMatrix = (Matrix) first;
            firstMatrix.multiplyNumber(new ComplexNumber(-1, 0));
            return firstMatrix;
        }
        else if (first instanceof ComplexNumber) {
            firstNumber = (ComplexNumber) first;
            firstNumber.multiply(new ComplexNumber(-1, 0));
            return firstNumber;
        }
        else {
            throw new OperationsException("Determinant operation is not supported for this type");
        }
    }
}
