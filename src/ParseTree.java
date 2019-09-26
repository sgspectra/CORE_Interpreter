import java.util.HashMap;
import java.util.*;
import java.io.*;

public class ParseTree {
    //Parse tree will contain one instance of class program as root
    private Program p;
    //ArrayList of Tokens
    private ArrayList<String> s;
    //File Reader to access values
    private File fileName;
    private Scanner fs;
    //Variable to hold token from Scanner
    private String currentToken;
    //Int to hold position in ArrayList
    private int loc;
    //Symbol Table
    HashMap<String, Integer> symbolTable = new HashMap();

    public ParseTree(ArrayList<String> in, String inputValuesFile){
        s = in;
        loc = 0;
        p = new Program();
        fileName = new File(inputValuesFile);
        try {
            fs = new Scanner(fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
        currentToken = "";
    }

    public void parse(){
        p.parse();
    }
    public void print(){
        p.print();
    }
    public void exec(){
        p.exec();
    }


    private class Program{
        private DecSeq d1;
        private StmtSeq s1;

        public Program(){
            d1 = null;
            s1 = null;
        }

        public void parse(){
            currentToken = s.get(loc);
            //Check if first token is PROGRAM
            if (currentToken.equals("PROGRAM")){
                //Print token to stdout
                //TODO remove print stmnt
                System.out.println(currentToken);
                //create declaration statement and parse it
                d1 = new DecSeq();
                d1.parse();
            }else{
                System.out.println("ERROR: Expecting keyword PROGRAM");
            }
            //Check if the next token available is BEGIN
            loc++;
            currentToken = s.get(loc);
            if (currentToken.equals("BEGIN")){
                //Print token to stdout
                System.out.println(currentToken);
                //create statement sequence and parse it
                s1 = new StmtSeq();
                s1.parse();
            }else{
                System.out.println("ERROR: Expecting keyword BEGIN");
            }
            //Check for end token
            loc++;
            currentToken = s.get(loc);
            if (!currentToken.equals("END")){
                System.out.println("ERROR: Expecting keyword END");
            }
        }

        public void exec(){
            d1.exec();
            s1.exec();
        }

        public void print(){
            System.out.println("program");
            d1.print();
            System.out.println("begin");
            s1.print();
            System.out.println("end");
        }
    }

    private class DecSeq{
        private Decl d1;
        private DecSeq d2;

        //constructor
        public DecSeq(){
            d1 = null;
            d2 = null;
        }


        public void parse(){
            d1 = new Decl();
            d1.parse();
            if (!s.get(loc +1).equals("BEGIN")){
                d2 = new DecSeq();
                d2.parse();
            }
        }
        public void exec(){
            d1.exec();
            if (d2 != null){
                d2.exec();
            }
        }
        public void print(){
            d1.print();
            if (d2 != null){
                d2.print();
            }
        }
    }


    private class StmtSeq{
        private Stmt s1;
        private StmtSeq s2;

        //constructor
        public StmtSeq(){
            s1 = null;
            s2 = null;
        }

        public void parse(){
            s1 = new Stmt();
            s1.parse();
            //if the current token is not END parse another StmtSeq
            if (!s.get(loc + 1).equals("END")){
                s2 = new StmtSeq();
                s2.parse();
            }
        }
        public void exec(){
            s1.exec();
            if (s2 != null){
                s2.exec();
            }
        }
        public void print(){
            s1.print();
            if (s2 != null){
                s2.print();
            }
        }
    }

    private class Decl{
        private IdList i1;

        //constructor
        public Decl(){
            i1 = null;
        }

        public void parse(){
            //check for keyword INT
            loc++;
            currentToken = s.get(loc);
            if (currentToken.equals("INT")){
                i1 = new IdList("DEC");
                i1.parse();
            }
            //consume the semicolon
            loc++;
            currentToken = s.get(loc);

        }
        public void exec(){
            i1.exec();
        }
        public void print(){
            System.out.println("int");
            i1.print();
        }
    }

    //TODO Stmt
    private class Stmt{
        //TODO add other statement types
        private int altNo;
        private Assign a1;
        private In i1;
        private Out o1;

        public Stmt(){
            altNo = -1;
            a1 = null;
            i1 = null;
            o1 = null;
        }
        //TODO Parse
        public void parse(){
            loc++;
            currentToken = s.get(loc);
            if(currentToken.indexOf('I') == 0 && currentToken.indexOf('D') == 1){
                altNo = 1;
                a1 = new Assign();
                a1.parse();
            }else if(currentToken.equals("INPUT")){
                altNo = 2;
                i1 = new In();
                i1.parse();
            }else if(currentToken.equals("OUTPUT")){
                altNo = 3;
                o1 = new Out();
                o1.parse();
            }else{
                System.out.println("ERROR: Invalid Statement Token");
            }
        }
        public void exec(){
            switch (altNo){
                case 1:
                    a1.exec();
                    break;
                case 2:
                    i1.exec();
                    break;
                case 3:
                    o1.exec();
                    break;
            }
        }
        //TODO Print
        public void print(){
            switch (altNo){
                case 1:
                    a1.print();
                    break;
                case 2:
                    i1.print();
                    break;
                case 3:
                    o1.print();
                    break;
            }
        }
    }

    private class Assign{
        private Id i1;
        private Expr e1;

        //constructor
        public Assign(){
            i1 = null;
            e1 = null;
        }

        public void parse(){
            //current token should still be id token
            i1 = new Id("ASSIGN");
            i1.parse();
            // consume the assign token
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("ASSIGN")){
                System.out.println("ERROR: Expected Assign Token");
            }
            e1 = new Expr();
            e1.parse();
            //consume the semilcolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
            }
        }
        public void exec(){
            i1.exec();
            e1.exec();
        }
        public void print(){
            i1.print();
            System.out.println(":=");
            e1.print();
            System.out.println(";");
        }
    }

    private class In{
        private IdList i1;

        public void parse(){
            i1 = new IdList("INPUT");
            i1.parse();
            //consume the semicolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
            }
        }
        public void exec(){
            i1.exec();
        }
        public void print(){
            System.out.println("input");
            i1.print();
            System.out.println(";");
        }
    }

    private class Out{
        private Expr e1;

        public Out(){
            e1 = null;
        }

        public void parse(){
            //at this point the current token should be OUTPUT
            //move it to the expression
            e1 = new Expr();
            e1.parse();
            //consume the semicolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
            }
        }
        public void exec(){
            System.out.println(e1.exec());
        }
        public void print(){
            System.out.println("output");
            e1.print();
            System.out.println(";");
        }
    }

    private class Expr{
        //0- not used, 1- plus, 2- minus
        private int plusMinus;
        private Term t1;
        private Expr e1;

        public Expr(){
            t1 = null;
            e1 = null;
            plusMinus = 0;
        }


        public void parse(){
            //first parse the term
            t1 = new Term();
            t1.parse();
            //looks at next token
            if(s.get(loc+1).equals("+")){
                loc++;
                currentToken = s.get(loc);
                e1 = new Expr();
                e1.parse();
                plusMinus = 1;
            }else if(s.get(loc+1).equals("-")){
                loc++;
                currentToken = s.get(loc);
                e1 = new Expr();
                e1.parse();
                plusMinus = 2;
            }
        }
        public int exec(){
            int returnVal;
            returnVal = t1.exec();
            if((e1 != null) && (plusMinus == 1)){
                returnVal = returnVal + e1.exec();
            }else if((e1 != null) && (plusMinus == 2)){
                returnVal = returnVal - e1.exec();
            }
            return returnVal;
        }
        public void print(){
            t1.print();
            if((e1 != null) && plusMinus == 1){
                System.out.println("+");
                e1.print();
            }
            if((e1 != null) && plusMinus == 2){
                System.out.println("-");
                e1.print();
            }
        }
    }

    private class Term{
        private Factor f1;
        private Term t1;

        public Term(){
            f1 = null;
            t1 = null;
        }

        public void parse(){
            f1 = new Factor();
            f1.parse();
            //if the next token is a * then there is another term
            if(s.get(loc + 1).equals("*")){
                loc++;
                currentToken = s.get(loc);
                t1 = new Term();
                t1.parse();
            }
        }
        public int exec(){
            int returnVal;
            returnVal = f1.exec();
            if(t1 != null){
                returnVal = returnVal * t1.exec();
            }
            return returnVal;
        }
        public void print(){
            f1.print();
            if(t1 != null){
                System.out.println("*");
                t1.print();
            }

        }
    }

    private class Factor{
        private Const c1;
        private Id i1;
        private Expr e1;
        private int opt;

        public void parse(){
            loc++;
            currentToken = s.get(loc);
            if(currentToken.substring(0,5).equals("CONST")){
                opt = 1;
                c1 = new Const();
                c1.parse();
            }else if(currentToken.substring(0,2).equals("ID")){
                opt = 2;
                i1 = new Id("FACTOR");
                i1.parse();
            }else{
                opt = 3;
                //consume paren
                loc++;
                currentToken = s.get(loc);
                e1 = new Expr();
                e1.parse();
                //consume paren
                loc++;
                currentToken = s.get(loc);
            }

        }
        public int exec(){
            int returnVal;
            if(opt == 1){
               returnVal = c1.exec();
            }else if(opt == 2){
                returnVal = i1.exec();
            }else{
                returnVal = e1.exec();
            }
            return returnVal;
        }
        public void print(){
            if(opt == 1){
                c1.print();
            }else if(opt == 2){
                i1.print();
            }else{
                e1.print();
            }
        }
    }

    private class Const{

        public void parse(){

        }
        public int exec(){
            return 0;
        }
        public void print(){

        }
    }

    private class IdList{
        private Id i1;
        private IdList i2;
        private String caller;

        public IdList(String call){
            i1 = null;
            i2 = null;
            caller = call;
        }

        public void parse(){
            //grab the first id and parse it
            loc++;
            currentToken = s.get(loc);
            //check that it is an id token
            if(currentToken.indexOf('I')== 0 & currentToken.indexOf('D') == 1){
                i1 = new Id(caller);
                i1.parse();
            }else{
                System.out.println("ERROR: Expected ID token");
            }
            //grab the next token and check if it is a comma
            loc++;
            currentToken = s.get(loc);
            if (currentToken.equals("COMMA")){
                i2 = new IdList(caller);
                i2.parse();
            }else{
                loc--;
                currentToken = s.get(loc);
            }
        }

        public void exec(){
            i1.exec();
            if (i2 != null){
                i2.exec();
            }
        }

        public void print(){
            i1.print();
            if (i2 != null){
                System.out.println("COMMA");
                i2.print();
            }
        }
    }

    private class Id{
        private String name;
        private String caller;

        public Id(String call){
            name = null;
            caller = call;
        }
        public void parse(){
            //the parser currentToken value should hold the ID token at this point
            //use this value to get the name of the ID
            name = currentToken.substring(3,currentToken.length()-1);
            //TODO remove this print
            System.out.println("ID[" + name + "]");
            //add to the symbol table if its not there
            if(!symbolTable.containsKey(name)) {
                symbolTable.put(name, null);
            }
        }
        public int exec(){
            int returnVal = 0;
            if(caller.equals("INPUT")){
                symbolTable.put(name,fs.nextInt());
            }else if(caller.equals("DEC")){
                //TODO Declerations were messing this up so this is here to catch calls to ID from decl
            }else{
                returnVal = symbolTable.get(name);
            }
            return returnVal;
        }
        public void print(){
            System.out.println(name);
        }
    }
}
