package org.example.operation_strategy.operations;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import java.util.Stack;

public class DegreeReader extends OperationReader {

    private void validDegree(ComplexNumber degreeNumber) throws OperationsException {
        if (degreeNumber.getImaginaryPart() != 0 || (int) degreeNumber.getActualPart() != degreeNumber.getActualPart()) {
            throw new OperationsException("The degree must be an integer");
        }
    }

    @Override
    public String getType() {
        return OperationsType.DEGREE;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException {
        initNumbers(stack);
        switch (second) {
            case ComplexNumber complexNumber when first instanceof ComplexNumber -> {
                firstNumber = (ComplexNumber) first;
                secondNumber = complexNumber;
                validDegree(secondNumber);
                return firstNumber.exponentiation((int) secondNumber.getActualPart());
            }
            case ComplexNumber complexNumber when first instanceof Matrix -> {
                firstMatrix = (Matrix) first;
                secondNumber = complexNumber;
                validDegree(secondNumber);
                firstMatrix.exponentiationMatrix((int) secondNumber.getActualPart());
                return firstMatrix;
            }
            case null, default -> throw new OperationsException("Operation is not correct");
        }
    }
}
