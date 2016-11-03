import java.util.Scanner;
public class Sum {
    public static void main(String[] Args) {
        // Allow user input
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter 2 integers that you want to sum up");
        System.out.println("First ");
        int first = input.nextInt();
        System.out.println("Second ");
        int second = input.nextInt();
        int sum = first + second;
        System.out.println("The sum of " + first + " and " + second + " is " + sum);
        input.close();
    }
}
