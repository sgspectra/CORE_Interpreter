import java.util.Scanner;

public class Main {
    /*CORE Interpreter Project
     *CSE 3341
     *Author: Andrew Storch
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please enter the name of the inputFile:");
        String filename = keyboard.nextLine();
        System.out.println("File Name: " + filename);
        Lexscan s = new Lexscan(filename);
        String test = s.getNext();
        while(!(test.equals("EOF"))){
            System.out.println(test);
            test = s.getNext();
        }
    }
}
