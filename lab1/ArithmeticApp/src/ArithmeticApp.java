import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArithmeticApp {
    public static void main( String[] Args){
        String input;
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please enter expression: \n");
            input = scanner.nextLine().replaceAll("\\s", "");
        }
        String expr = input;
        Pattern p = Pattern.compile("^-?\\d*\\.?\\d*");
        ArrayList<String> expressionList = new ArrayList<>();
        while(!expr.isEmpty()){
            Matcher m = p.matcher(expr);
            if (m.find()) {
                expressionList.add(m.group(0));
                expr = expr.replaceFirst(m.group(0), "");
                if (!expr.isEmpty()) {
                    expressionList.add(expr.substring(0, 1));
                    expr = expr.substring(1, expr.length());
                }
            }
        }
        String[][] operatorSets = {{"*", "/"}, {"+", "-"}};
        for (String[] operatorSet : operatorSets) {
            for (int i = 0; i < expressionList.size(); i++) {
                String operator = expressionList.get(i);
                if (Objects.equals(operator, operatorSet[0]) || Objects.equals(operator, operatorSet[1])) {
                    double first = Double.parseDouble(expressionList.get(i - 1));
                    double second = Double.parseDouble(expressionList.get(i + 1));
                    double result;
                    switch (operator){
                        case "*": result = first * second; break;
                        case "/": result = first / second; break;
                        case "+": result = first + second; break;
                        case "-": result = first - second; break;
                        default: throw new RuntimeException("error in switch");
                    }
                    // remove the two numbers and opearator.
                    expressionList.subList(i-1, i+2).clear();
                    expressionList.add(i - 1, String.valueOf(result));
                    i--;
                }
            }
        }
        System.out.println("The value of expression " + input + " is: = " + expressionList.get(0));
    }
}
