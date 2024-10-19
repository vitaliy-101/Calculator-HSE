package org.example;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class ExpressionConverter {

    // Словарь для хранения приоритетов операций
    private final Map<Character, Integer> operationPriority = new HashMap<>();

    public ExpressionConverter() {
        // Пример инициализации: задайте приоритет для операций
        operationPriority.put('+', 1);
        operationPriority.put('-', 1);
        operationPriority.put('*', 2);
        operationPriority.put('/', 2);
        // Добавьте другие операции, если необходимо
    }

    /**
     * Преобразует инфиксное выражение в постфиксное.
     *
     * @param infixExpr Инфиксное выражение
     * @return Постфиксное выражение
     */
    private String toPostfix(String infixExpr) {
        // Выходная строка, содержащая постфиксную запись
        StringBuilder postfixExpr = new StringBuilder();
        // Инициализация стека, содержащего операторы в виде символов
        Stack<Character> stack = new Stack<>();

        // Перебираем строку
        for (int i = 0; i < infixExpr.length(); i++) {
            // Текущий символ
            char c = infixExpr.charAt(i);

            // Если символ - цифра
            if (Character.isDigit(c)) {
                // Парсим его, передав строку и текущую позицию, и заносим в выходную строку
                String number = getStringNumber(infixExpr, new int[]{i});
                postfixExpr.append(number).append(" ");
                i += number.length() - 1; // Обновляем индекс после парсинга числа
            }
            // Если открывающаяся скобка
            else if (c == '(') {
                // Заносим её в стек
                stack.push(c);
            }
            // Если закрывающая скобка
            else if (c == ')') {
                // Заносим в выходную строку из стека всё вплоть до открывающей скобки
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfixExpr.append(stack.pop());
                }
                // Удаляем открывающуюся скобку из стека
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            }
            // Проверяем, содержится ли символ в списке операторов
            else if (operationPriority.containsKey(c)) {
                // Если да, то сначала проверяем
                char op = c;
                // Является ли оператор унарным символом
                if (op == '-' && (i == 0 || (i > 0 && operationPriority.containsKey(infixExpr.charAt(i - 1))))) {
                    // Если да - преобразуем его в тильду
                    op = '~';
                }

                // Заносим в выходную строку все операторы из стека, имеющие более высокий приоритет
                while (!stack.isEmpty() && (operationPriority.get(stack.peek()) >= operationPriority.get(op))) {
                    postfixExpr.append(stack.pop());
                }
                // Заносим в стек оператор
                stack.push(op);
            }
        }
        // Заносим все оставшиеся операторы из стека в выходную строку
        while (!stack.isEmpty()) {
            postfixExpr.append(stack.pop());
        }

        // Возвращаем выражение в постфиксной записи
        return postfixExpr.toString().trim(); // Убираем лишние пробелы
    }

    // Метод для получения строкового представления числа
    private String getStringNumber(String expr, int[] pos) {
        StringBuilder strNumber = new StringBuilder();
        for (; pos[0] < expr.length(); pos[0]++) {
            char num = expr.charAt(pos[0]);
            if (Character.isDigit(num)) {
                strNumber.append(num);
            } else {
                pos[0]--; // Возврат на предыдущий символ
                break;
            }
        }
        return strNumber.toString();
    }
}
