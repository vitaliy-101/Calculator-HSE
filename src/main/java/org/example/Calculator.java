package org.example;

import org.example.exceptions.MatrixOperationException;
import org.example.exceptions.NumberValidityException;
import org.example.exceptions.ParserException;
import org.example.operation_strategy.operations.*;
import org.example.operation_strategy.OperationManager;
import org.example.operation_strategy.OperationReader;

import javax.management.OperationsException;
import javax.management.openmbean.OpenDataException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    private ExpressionParser expressionParser;
    private static final MessageCreator MESSAGE_CREATOR = new MessageCreator();
    private Object result;
    private final static List<OperationReader> OPERATION_READERS = List.of(new AddReader(), new SubtractReader(), new DivideReader(),
            new MultiplyReader(), new TildaReader(), new TransposeReader(),
            new DeterminantReader(), new DegreeReader());

    public Calculator() throws ParserException, NumberValidityException {
        MESSAGE_CREATOR.sendMessage("""
                Вас приветствует матричный калькулятор. Он умеет выполнять некоторые операции с матрицами и\s
                комплексными числами. Операции Калькулятора (m - операция доступна для матриц, k - операция доступна для комплексных чисел):\s
                + : m k\s
                - : m k\s
                / : к\s
                * : m k\s
                ^ : m k (степень должна быть целым числом)\s
                d : m (детерминант, пример использования det(A+B), A и B - матрицы)\s
                t : m (транспонирования, пример t(A), A - матрица)\s
                """, MessageType.NEXT);
        MESSAGE_CREATOR.sendMessage("""
                Помните, что в выражении матрицы записываются буквами. Например A * 2i + B.\s
                После этого происходит ввод каждой матрицы.\s
                Пора начинать! Введите ваше выражение:
                """, MessageType.NEXT);
        Scanner in = new Scanner(System.in);
        while (true) {
            var expression = in.nextLine();
            try {
                this.expressionParser = new ExpressionParser(expression);
                this.result = calculate();
            }
            catch (Exception e) {
                MESSAGE_CREATOR.sendMessage("Упс, возникло исключание: " + e.getMessage() + "; Попробуйте снова!", MessageType.NEXT);
                MESSAGE_CREATOR.sendMessage("Введите выражение!", MessageType.NEXT);
                continue;
            }
            break;
        }
    }

    public void getResult() {
        System.out.println(result.toString());
    }

    public Object calculate() throws NumberValidityException, MatrixOperationException, OperationsException {
        var stack = new Stack<>();
        var postfixExpression = expressionParser.getPostfixExpression();
        var matricesFound = expressionParser.getMatricesFound();
        for (int index = 0; index < postfixExpression.length(); index++) {
            var element = String.valueOf(postfixExpression.charAt(index));
            if (element.matches("\\d+")) {
                var number = expressionParser.convertToNumberFromExpression(index, postfixExpression).toString();
                var complexNumber = parseCalculatorNumber(number);
                index += number.length() - 1;
                stack.push(complexNumber);
            }
            else if (element.equals("i")) {
                var complexNumber = new ComplexNumber(0, 1);
                stack.push(complexNumber);
            }
            else if (ExpressionParser.MATRIX_VARIABLES.contains(element)) {
                stack.push(matricesFound.get(element));
            }
            else if (ExpressionParser.OPERATION_PRIORITY.containsKey(element)) {
                var operationManager = new OperationManager(OPERATION_READERS);
                stack.push(operationManager.createResult(element, stack));
            }
        }
        return stack.pop();
    }

    private ComplexNumber parseCalculatorNumber(String number) throws NumberValidityException {
        var complexNumber = new ComplexNumber();
        if (number.contains("i")) {
            complexNumber.setImaginaryPart(Double.parseDouble(number.replace("i", "")));
        }
        else {
            complexNumber.setActualPart(Double.parseDouble(number));
        }
        return complexNumber;
    }
}
