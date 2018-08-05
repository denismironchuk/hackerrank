import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class SimpleParser {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String expression = br.readLine();

        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char val = expression.charAt(i);
            switch (val) {
                case '+':
                    if (operators.empty()) {
                        operators.push(val);
                        break;
                    }
                    char op = operators.peek();
                    if (op == '*') {
                        operators.pop();
                        numbers.push(numbers.pop() * numbers.pop());
                    } else if (op == '+') {
                        operators.pop();
                        numbers.push(numbers.pop() + numbers.pop());
                    } else if (op == '-') {
                        operators.pop();
                        numbers.push(-numbers.pop() + numbers.pop());
                    }
                    operators.push(val);
                    break;
                case '-':
                    if (operators.empty()) {
                        operators.push(val);
                        break;
                    }
                    char op2 = operators.peek();
                    if (op2 == '*') {
                        operators.pop();
                        numbers.push(numbers.pop() * numbers.pop());
                    } else if (op2 == '+') {
                        operators.pop();
                        numbers.push(numbers.pop() + numbers.pop());
                    } else if (op2 == '-') {
                        operators.pop();
                        numbers.push(-numbers.pop() + numbers.pop());
                    }
                    operators.push(val);
                    break;
                case '*':
                    if (operators.empty()) {
                        operators.push(val);
                        break;
                    }
                    char op3 = operators.peek();
                    if (op3 == '*') {
                        operators.pop();
                        numbers.push(numbers.pop() * numbers.pop());
                    }
                    operators.push(val);
                    break;
                case '(':
                    operators.push(val);
                    break;
                case ')':
                    char op4 = operators.pop();
                    while (op4 != '(') {
                        if (op4 == '*') {
                            numbers.push(numbers.pop() * numbers.pop());
                        } else if (op4 == '+') {
                            numbers.push(numbers.pop() + numbers.pop());
                        } else if (op4 == '-') {
                            numbers.push(-numbers.pop() + numbers.pop());
                        }
                        op4 = operators.pop();
                    }
                    break;
                default:
                    numbers.push(Integer.parseInt(String.valueOf(val)));
                    break;
            }
        }

        System.out.println();
    }
}
