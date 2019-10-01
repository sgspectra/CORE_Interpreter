
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

public class Main {
    /*CORE Interpreter Project
     *CSE 3341
     *Author: Andrew Storch
     */
    public static void main(String[] args) {
        //get the file from the arguments
        String filename = args[0];
        System.out.println("File Name: " + filename);
        Lexscan s = new Lexscan(filename);
        //get file with input values
        String inputValues = args[1];
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
