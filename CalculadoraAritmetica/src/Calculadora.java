import java.util.Stack;
import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce una expresión aritmética (ej. 3 + 5 * 2 - 8): ");
        String input = scanner.nextLine();

        try {
            double resultado = evaluar(input);
            System.out.println("El resultado es: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al evaluar la expresión: " + e.getMessage());
        }

        scanner.close();
    }

    private static double evaluar(String expresion) {
        String postfija = convertirAPostfija(expresion);
        return evaluarPostfija(postfija);
    }

    private static String convertirAPostfija(String infija) {
        StringBuilder resultado = new StringBuilder();
        Stack<Character> pila = new Stack<>();
        String[] tokens = infija.split(" ");

        for (String token : tokens) {
            if (esNumero(token)) {
                resultado.append(token).append(" ");
            } else if (token.charAt(0) == '(') {
                pila.push('(');
            } else if (token.charAt(0) == ')') {
                while (!pila.isEmpty() && pila.peek() != '(') {
                    resultado.append(pila.pop()).append(" ");
                }
                pila.pop();
            } else {
                while (!pila.isEmpty() && prioridad(token.charAt(0)) <= prioridad(pila.peek())) {
                    resultado.append(pila.pop()).append(" ");
                }
                pila.push(token.charAt(0));
            }
        }

        while (!pila.isEmpty()) {
            resultado.append(pila.pop()).append(" ");
        }

        return resultado.toString().trim();
    }

    private static boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int prioridad(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    private static double evaluarPostfija(String postfija) {
        Stack<Double> pila = new Stack<>();
        String[] tokens = postfija.split(" ");

        for (String token : tokens) {
            if (esNumero(token)) {
                pila.push(Double.parseDouble(token));
            } else {
                double b = pila.pop();
                double a = pila.pop();
                double resultado = 0;

                switch (token.charAt(0)) {
                    case '+':
                        resultado = a + b;
                        break;
                    case '-':
                        resultado = a - b;
                        break;
                    case '*':
                        resultado = a * b;
                        break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("División por cero");
                        resultado = a / b;
                        break;
                    case '^':
                        resultado = Math.pow(a, b);
                        break;
                }

                pila.push(resultado);
            }
        }

        return pila.pop();
    }
}
