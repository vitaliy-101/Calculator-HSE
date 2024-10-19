package org.example.operation_strategy;

import org.example.exceptions.MatrixOperationException;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class OperationManager {
    private final Map<String, OperationReader> operationReaderMap;

    public OperationManager(List<OperationReader> operationReaders) {
        operationReaderMap = new HashMap<>();
        operationReaders.forEach(operationReader -> operationReaderMap.put(operationReader.getType(), operationReader));
    }

    public Object createResult(String operationsType, Stack<Object> stack) throws MatrixOperationException, OperationsException {
        return operationReaderMap.get(operationsType).createResult(stack);
    }
}
