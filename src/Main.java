
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

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
        System.out.println("Input name of second input file: (input values");
        String inputValues = keyboard.nextLine();
        ArrayList<String> tokens = new ArrayList<>();
        String test = s.getNext();
        while(!(test.equals("EOF"))){
            tokens.add(test);
            test = s.getNext();
        }
        Iterator it = tokens.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
        ParseTree p = new ParseTree(tokens, inputValues);
        p.parse();
        p.print();
        p.exec();

    }
}
