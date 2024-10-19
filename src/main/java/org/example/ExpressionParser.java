package org.example;

import org.example.exceptions.MatrixOperationException;
import org.example.exceptions.NumberValidityException;
import org.example.exceptions.ParserException;

import java.util.*;
import java.util.regex.Pattern;



public class ExpressionParser {
    private String infixExpression;
    private String postfixExpression;
    private static final MessageCreator MESSAGE_CREATOR = new MessageCreator();
    private Map<String, Matrix> matricesFound;
    public static final Map<String, Integer> OPERATION_PRIORITY;
    public static final List<String> MATRIX_VARIABLES;

    static {
        OPERATION_PRIORITY = new HashMap<>();
        OPERATION_PRIORITY.put("(", 0);
        OPERATION_PRIORITY.put("+", 1);
        OPERATION_PRIORITY.put("-", 1);
        OPERATION_PRIORITY.put("*", 2);
        OPERATION_PRIORITY.put("/", 2);
        OPERATION_PRIORITY.put("^", 3);
        OPERATION_PRIORITY.put("d", 4);
        OPERATION_PRIORITY.put("t", 4);
        OPERATION_PRIORITY.put("~", 5);

        MATRIX_VARIABLES = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            MATRIX_VARIABLES.add(String.valueOf(letter));
        }
    }

    public ExpressionParser(String expression) throws NumberValidityException, ParserException {
        expression = expression.replace(" ", "");
        matricesFound = new HashMap<>();
        infixExpression = expression;
        postfixExpression = convertToPostfixExpression(infixExpression);
        parseMatrixExpressions();
    }

    private String convertToPostfixExpression(String infixExpression) throws NumberValidityException, ParserException {
        var postfixExpression = new StringBuilder();
        var stack = new Stack<String>();
        for (int index = 0; index < infixExpression.length(); index ++) {
            var element = String.valueOf(infixExpression.charAt(index));
            if (element.matches("\\d+")) {
                var number = convertToNumberFromExpression(index, infixExpression);
                postfixExpression.append(number).append(" ");
                index += number.length() - 1;
            }
            else if (element.equals("i")) {
                postfixExpression.append("i").append(" ");
            }
            else if (element.equals("(")) {
                stack.push(element);
            }
            else if (element.equals(")")) {
                transferFromStackToPostfix(stack, postfixExpression);
            }
            else if (OPERATION_PRIORITY.containsKey(element)) {
                element = convertToTilda(element, index);
                transferFromStackToPostfix(stack, postfixExpression, element);
            }

            else if (Pattern.matches("[A-Z]", element)) {
                matricesFound.put(element, new Matrix());
                postfixExpression.append(element).append(" ");
            }
            else {
                throw new ParserException("The input string is not correct!");
            }
        }
        return getPostfixExpressionResult(stack, postfixExpression);
    }

    private String getPostfixExpressionResult(Stack<String> stack, StringBuilder postfixExpression) {
        while (!stack.isEmpty()) {
            postfixExpression.append(stack.pop());
        }
        return postfixExpression.toString();
    }

    private String convertToTilda(String element, int index) {
        if (element.equals("-") && (index == 0 ||
                (index > 1 && OPERATION_PRIORITY.containsKey(String.valueOf(infixExpression.charAt(index - 1))))))
            element = "~";
        return element;
    }

    private void transferFromStackToPostfix(Stack<String> stack, StringBuilder postfixExpression) {
        while (!stack.isEmpty() && !stack.peek().equals("("))
            postfixExpression.append(stack.pop());
        stack.pop();
    }

    private void transferFromStackToPostfix(Stack<String> stack, StringBuilder postfixExpression,
                                            String operation) {
        while (!stack.isEmpty() && (OPERATION_PRIORITY.get(stack.peek()) >= OPERATION_PRIORITY.get(operation)))
            postfixExpression.append(stack.pop());
        stack.push(operation);
    }

    public StringBuilder convertToNumberFromExpression(int startIndex, String expression) throws NumberValidityException {
        var subString = new StringBuilder();
        var element = expression.charAt(startIndex);
        while (Character.isDigit(element) || element == '.' || element == 'i') {
            subString.append(element);
            startIndex += 1;
            if (startIndex >= expression.length()) {
                break;
            }
            element = expression.charAt(startIndex);
        }
        validateReceivedNumber(subString.toString());
        return subString;
    }

    private void validateReceivedNumber(String subString) throws NumberValidityException {
        var pattern = Pattern.compile("^[+-]?([0-9]+(\\.[0-9]+)?i?|[0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]*i?)$");
        if (!pattern.matcher(subString).matches() || subString.startsWith("0") && !subString.contains(".")) {
            throw new NumberValidityException("The entered number is not correct");
        }
    }

    public void parseMatrixExpressions() {
        Scanner in = new Scanner(System.in);
        for (var key : matricesFound.keySet()) {
            var matrix = matricesFound.get(key);
            readMatrixSize(matrix, key, in);
            readMatrixValues(matrix, in);
            matricesFound.put(key, matrix);
        }
    }

    private void readMatrixValues(Matrix matrix, Scanner in) {
        for (int i = 0; i < matrix.getRowsSize(); i ++) {
            var newMatrixRow = new ArrayList<ComplexNumber>();
            in.nextLine();
            while (true) {
                try {
                    MESSAGE_CREATOR.sendMessage("Введите числа строки " + (i + 1) + " матрицы через пробел: ", MessageType.THERE);
                    readValues(matrix, newMatrixRow, in);
                }
                catch (Exception e) {
                    MESSAGE_CREATOR.sendMessage("Вы ввели некорректные данные! Попробуйте снова!", MessageType.NEXT);
                    continue;
                }
                break;
            }
            matrix.addRow(newMatrixRow);
        }
    }

    private void readValues(Matrix matrix, ArrayList<ComplexNumber> newMatrixRow, Scanner in) throws ParserException {
        var numberList = getInputRowNumbers(matrix, in);
        var regexReal = "^-?\\d+(\\.\\d+)?$";
        var regexComplex = "^[+-]?\\d*\\.?\\d*[+-]\\d*\\.?\\d*i$|^([+-]?\\d*\\.?\\d*)?i$|^[+-]?\\d*\\.?\\d*$";
        for (String number : numberList) {
            var validNumber = new ComplexNumber();
            if (number.matches(regexReal)) {
                setNonComplexNumberValue(validNumber, number);
            }
            else if (number.matches(regexComplex)) {
                setComplexNumberValue(validNumber, number);
            }
            else {
                throw new ParserException("Input values are not correct");
            }
            newMatrixRow.add(validNumber);
        }
    }

    private void setComplexNumberValue(ComplexNumber validNumber, String number) {
        int indexOfI = number.indexOf('i');
        if (indexOfI == 0) {
            validNumber.setImaginaryPart(1);
        }
        else {
            int plusIndex = number.indexOf('+', 1);
            int minusIndex = number.indexOf('-', 1);
            int signIndex = getSignIndex(plusIndex, minusIndex);
            if (signIndex == -1) {
                number = number.replace("i", "");
                validNumber.setImaginaryPart(Double.parseDouble(number));
                return;
            }
            parseValidComplexNumber(number, signIndex, indexOfI, validNumber);
        }
    }

    private void parseValidComplexNumber(String number, int signIndex,
                                         int indexOfI, ComplexNumber validNumber) {
        var actualPart = number.substring(0, signIndex);
        var imaginaryPart = number.substring(signIndex, indexOfI);
        if (!actualPart.isEmpty()) {
            validNumber.setActualPart(Double.parseDouble(actualPart));
        }
        if (imaginaryPart.equals("-") || imaginaryPart.equals("+")) {
            imaginaryPart = imaginaryPart.charAt(0) + "1";
        }
        validNumber.setImaginaryPart(Double.parseDouble(imaginaryPart));
    }

    private int getSignIndex(int plusIndex, int minusIndex) {
        int signIndex = -1;
        // Определяем, где находится знак + или - для мнимой части
        if (plusIndex != -1 && (minusIndex == -1 || plusIndex < minusIndex)) {
            signIndex = plusIndex;
        } else if (minusIndex != -1) {
            signIndex = minusIndex;
        }
        return signIndex;
    }

    private void setNonComplexNumberValue(ComplexNumber validNumber, String number) {
        validNumber.setActualPart(Double.parseDouble(number));
    }

    private List<String> getInputRowNumbers(Matrix matrix, Scanner in) throws ParserException {
        var rowExpression = in.nextLine();
        var numberList = List.of(rowExpression.split(" "));
        if (numberList.size() != matrix.getColumnsSize()) {
            throw new ParserException("Count of numbers does not equal column size");
        }
        return numberList;
    }

    private void readMatrixSize(Matrix matrix, String matrixName, Scanner in) {
        MESSAGE_CREATOR.sendMessage("Введите матрицу " + matrixName + ":\n" + "Введите размеры матрицы:", MessageType.NEXT);
        while (true) {
            try {
                readNumbersOfColumnsAndRows(matrix, in);
            }
            catch (Exception e) {
                MESSAGE_CREATOR.sendMessage("Вы ввели некорректные данные! Попробуйте снова!", MessageType.NEXT);
                continue;
            }
            break;
        }
    }

    private void readNumbersOfColumnsAndRows(Matrix matrix, Scanner in) throws ParserException {
        MESSAGE_CREATOR.sendMessage("Количеcтво столбцов: ", MessageType.THERE);
        var columnSize = in.next();
        MESSAGE_CREATOR.sendMessage("Количеcтво строк: ", MessageType.THERE);
        var rowsSize = in.next();
        var numberRegex = "^-?\\d+$";
        if (columnSize.matches(numberRegex) && rowsSize.matches(numberRegex)) {
            if (Integer.parseInt(columnSize) < 1 || Integer.parseInt(rowsSize) < 1) {
                throw new ParserException("Size of column and size of rows are not correct");
            }
            matrix.setColumnsSize(Integer.parseInt(columnSize));
            matrix.setRowsSize(Integer.parseInt(rowsSize));
        }
        else {
            throw new ParserException("Size of column and size of rows are not correct");
        }
    }

    public static Map<String, Integer> getOperationPriority() {
        return OPERATION_PRIORITY;
    }

    public static List<String> getMatrixVariables() {
        return MATRIX_VARIABLES;
    }

    public String getInfixExpression() {
        return infixExpression;
    }

public void setInfixExpression(String infixExpression) {
        this.infixExpression = infixExpression;
    }

    public String getPostfixExpression() {
        return postfixExpression;
    }

    public void setPostfixExpression(String postfixExpression) {
        this.postfixExpression = postfixExpression;
    }

    public Map<String, Matrix> getMatricesFound() {
        return matricesFound;
    }

    public void setMatricesFound(Map<String, Matrix> matricesFound) {
        this.matricesFound = matricesFound;
    }
}
