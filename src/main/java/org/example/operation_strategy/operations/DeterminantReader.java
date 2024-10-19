package org.example.operation_strategy.operations;

import org.example.ComplexNumber;
import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.util.Stack;

public class DeterminantReader extends OperationReader {
    @Override
    public String getType() {
        return OperationsType.DETERMINANT;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException {
        initNumber(stack);
        if (first instanceof Matrix) {
            return ((Matrix) first).getDeterminant();
        }
        else {
            throw new OperationsException("Determinant operation is not supported for this type");
        }
    }
}
