package org.example.operation_strategy.operations;

import org.example.Matrix;
import org.example.exceptions.MatrixOperationException;
import org.example.operation_strategy.OperationReader;
import org.example.operation_strategy.OperationsType;

import javax.management.OperationsException;
import java.util.Stack;

public class TransposeReader extends OperationReader {
    @Override
    public String getType() {
        return OperationsType.TRANSPOSE;
    }

    @Override
    public Object createResult(Stack<Object> stack) throws MatrixOperationException, OperationsException {
        initNumber(stack);
        if (first instanceof Matrix) {
            return ((Matrix) first).transpose();
        }
        else {
            throw new OperationsException("Transpose operation is not supported for this type");
        }
    }
}
