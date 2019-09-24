import java.util.HashMap;
import java.util.*;
import java.io.*;

public class ParseTree {
    //Parse tree will contain one instance of class program as root
    private Program p;
    //Scanner that will supply tokens
    private Lexscan s;
    //File Reader to access values
    private File fileName;
    private Scanner fs;
    //Variable to hold token from Scanner
    private String currentToken;
    //Symbol Table
    HashMap<String, Integer> symbolTable = new HashMap();

    public ParseTree(Lexscan scannerIn, String inputValuesFile){
        s = scannerIn;
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


    private class Program{
        private DecSeq d1;
        private StmtSeq s1;

        public Program(){
            d1 = null;
            s1 = null;
        }

        public void parse(){
            currentToken = s.getNext();
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
            currentToken = s.getNext();
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
            //if the current token is not BEGIN parse another decseq
            if (!currentToken.equals("BEGIN")){
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
            if (!currentToken.equals("END")){
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
            currentToken = s.getNext();
            if (currentToken.equals("INT")){
                i1 = new IdList();
                i1.parse();
            }

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
            currentToken = s.getNext();
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
            i1 = new Id();
            i1.parse();
            // consume the assign token
            currentToken = s.getNext();
            if(!currentToken.equals("ASSIGN")){
                System.out.println("ERROR: Expected Assign Token");
            }
            e1 = new Expr();
            e1.parse();
            //consume the semilcolon
            currentToken = s.getNext();
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
        private Id i1;

        public void parse(){
            //at this point the current token should be INPUT
            //move it to the ID
            currentToken = s.getNext();
            i1 = new Id();
            i1.parse();
            //consume the semicolon
            currentToken = s.getNext();
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
            }
        }
        public void exec(){
            i1.input(fs.nextInt());
        }
        public void print(){
            System.out.println("input");
            i1.print();
            System.out.println(";");
        }
    }

    //TODO Out
    private class Out{
        //TODO Parse
        public void parse(){

        }
        //TODO Exec
        public void exec(){

        }
        //TODO Print
        public void print(){

        }
    }

    //TODO Expr
    private class Expr{
        //TODO Parse
        public void parse(){

        }
        //TODO Exec
        public void exec(){

        }
        //TODO Print
        public void print(){

        }
    }


    private class IdList{
        private Id i1;
        private IdList i2;

        public IdList(){
            i1 = null;
            i2 = null;
        }

        public void parse(){
            //grab the first id and parse it
            currentToken = s.getNext();
            //check that it is an id token
            if(currentToken.indexOf('I')== 0 & currentToken.indexOf('D') == 1){
                i1 = new Id();
                i1.parse();
            }else{
                System.out.println("ERROR: Expected ID token");
            }
            //grab the next token and check if it is a comma
            currentToken = s.getNext();
            if (currentToken.equals("COMMA")){
                i2 = new IdList();
                i2.parse();
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

        public Id(){
            name = null;
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
            return symbolTable.get(name);
        }
        public void input(int in){
            symbolTable.put(name, in);
        }
        public void print(){
            System.out.println(currentToken);
        }
    }
}
