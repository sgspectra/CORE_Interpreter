import java.util.*;
import java.io.*;

public class ParseTree {
    //Parse tree will contain one instance of class program as root
    private Program p;
    //ArrayList of Tokens
    private ArrayList<String> s;
    private Scanner fs;
    //Variable to hold token from Scanner
    private String currentToken;
    //Int to hold position in ArrayList
    private int loc;
    //Symbol Table
    private HashMap<String, Integer> symbolTable = new HashMap<>();
    //Indent
    private String indent = "";

    public ParseTree(ArrayList<String> in, String inputValuesFile){
        s = in;
        loc = 0;
        p = new Program();
        //File Reader to access values
        File fileName = new File(inputValuesFile);
        try {
            fs = new Scanner(fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
        currentToken = "";
    }

    private void ind(){
        indent = indent.concat("  ");
        System.out.print(indent);
    }
    private void unind(){
        if(indent.length()>0){
            indent = indent.substring(2);
        }
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
                //create declaration statement and parse it
                d1 = new DecSeq();
                d1.parse();
            }else{
                System.out.println("ERROR: Expecting keyword PROGRAM");
                System.exit(0);
            }
            //Check if the next token available is BEGIN
            loc++;
            currentToken = s.get(loc);
            if (currentToken.equals("BEGIN")){
                //create statement sequence and parse it
                s1 = new StmtSeq();
                s1.parse();
            }else{
                System.out.println("ERROR: Expecting keyword BEGIN");
                System.exit(0);
            }
            //current token should be at END token
            loc++;
            currentToken = s.get(loc);
            if (!currentToken.equals("END")){
                System.out.println("ERROR: Expecting keyword END");
                System.exit(0);
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
            //make sure not at end of token stream
            if(loc+1 >= s.size()){
                System.out.println("ERROR: Unexpected end to token stream");
                System.exit(0);
            }
            //if the next token is not END parse another StmtSeq
            if (!(s.get(loc+1).equals("END") || s.get(loc+1).equals("ENDIF") || s.get(loc+1).equals("ENDWHILE") || s.get(loc+1).equals("ELSE"))){
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
            ind();
            System.out.print("int ");
            i1.print();
            System.out.println(";");
            unind();
        }
    }

    private class Stmt{
        private int altNo;
        private Assign a1;
        private In i1;
        private Out o1;
        private If if1;
        private Loop l1;
        private Case c1;

        public Stmt(){
            altNo = -1;
            a1 = null;
            i1 = null;
            o1 = null;
            if1= null;
            l1 = null;
            c1 = null;
        }
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
            }else if(currentToken.equals("IF")){
                altNo = 4;
                if1 = new If();
                if1.parse();
            }else if(currentToken.equals("WHILE")){
                altNo = 5;
                l1 = new Loop();
                l1.parse();
            }else if(currentToken.equals("CASE")){
                altNo = 6;
                c1 = new Case();
                c1.parse();
            }

            else{
                System.out.println("ERROR: Invalid Statement Token");
                System.exit(0);
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
                case 4:
                    if1.exec();
                    break;
                case 5:
                    l1.exec();
                    break;
                case 6:
                    c1.exec();
                    break;
            }
        }
        public void print(){
            switch (altNo){
                case 1:
                    ind();
                    a1.print();
                    unind();
                    break;
                case 2:
                    ind();
                    i1.print();
                    unind();
                    break;
                case 3:
                    ind();
                    o1.print();
                    unind();
                    break;
                case 4:
                    ind();
                    if1.print();
                    unind();
                    break;
                case 5:
                    ind();
                    l1.print();
                    unind();
                    break;
                case 6:
                    ind();
                    c1.print();
                    unind();
                    break;
            }
        }
    }

    private class Assign{
        private Id i1;
        private Expr e1;
        private String idName;

        //constructor
        public Assign(){
            i1 = null;
            e1 = null;
        }

        public void parse(){
            //current token should still be id token
            //grab name
            idName = currentToken.substring(3, currentToken.length()-1);
            i1 = new Id("ASSIGN");
            i1.parse();
            // consume the assign token
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("ASSIGN")){
                System.out.println("ERROR: Expected Assign Token");
                System.exit(0);
            }
            //pass the expression the ID being  assigned
            e1 = new Expr(s.get(loc-1));
            e1.parse();
            //consume the semicolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
                System.exit(0);
            }
        }
        public void exec(){
            i1.exec();
            symbolTable.put(idName, e1.exec());
        }
        public void print(){
            i1.print();
            System.out.print(":=");
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
                System.exit(0);
            }
        }
        public void exec(){
            i1.exec();
        }
        public void print(){
            System.out.print("input ");
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
            e1 = new Expr("");
            e1.parse();
            //consume the semicolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
                System.exit(0);
            }
        }
        public void exec(){
            System.out.println(e1.exec());
        }
        public void print(){
            System.out.print("output ");
            e1.print();
            System.out.println(";");
        }
    }

    private class Expr{
        //0- not used, 1- plus, 2- minus
        private int plusMinus;
        private Term t1;
        private Expr e1;
        private String id;

        public Expr(String assId){
            t1 = null;
            e1 = null;
            plusMinus = 0;
            id = assId;
        }


        public void parse(){
            //if the next token here is a paren then we know we have a special case
            //first parse the term
            t1 = new Term(id);
            t1.parse();
            //looks at next token
            if (s.get(loc + 1).equals("+")) {
                loc++;
                currentToken = s.get(loc);
                e1 = new Expr("");
                e1.parse();
                plusMinus = 1;
            } else if (s.get(loc + 1).equals("-")) {
                loc++;
                currentToken = s.get(loc);
                e1 = new Expr("");
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
                System.out.print("+");
                e1.print();
            }
            if((e1 != null) && plusMinus == 2){
                System.out.print("-");
                e1.print();
            }
        }
    }

    private class Term{
        private Factor f1;
        private Term t1;
        private String id;

        public Term(String assId){
            f1 = null;
            t1 = null;
            id = assId;
        }

        public void parse(){
            f1 = new Factor(id);
            f1.parse();
            //if the next token is a * then there is another term
            if(s.get(loc + 1).equals("*")){
                loc++;
                currentToken = s.get(loc);
                t1 = new Term("");
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
                System.out.print("*");
                t1.print();
            }

        }
    }

    private class Factor{
        private Const c1;
        private String assignmentId;
        private Id i1;
        private Expr e1;
        private int opt;

        public Factor(String assId){
            c1 = null;
            assignmentId = assId;
            i1 = null;
            e1 = null;
        }

        public void parse(){
            loc++;
            currentToken = s.get(loc);
            if(currentToken.length()>2) {
                if(currentToken.substring(0, 5).equals("CONST")) {
                    opt = 1;
                    c1 = new Const(assignmentId);
                    c1.parse();
                }else if (currentToken.substring(0, 2).equals("ID")) {
                    opt = 2;
                    i1 = new Id("FACTOR");
                    i1.parse();
                }
            }else{
                opt = 3;
                e1 = new Expr("");
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
                System.out.print("(");
                e1.print();
                System.out.print(")");
            }
        }
    }

    private class Const{
        private int value;
        private String assignmentId;

        public Const(String assId){
            assignmentId = assId;
        }

        public void parse(){
            //current token should be the const token so we can grab the substring that
            //represents the value of the actual const
            value = Integer.parseInt(currentToken.substring(6,currentToken.length()-1));
        }
        public int exec(){
            //look for the ID in the symbol table and update if it is being assigned
            if(!assignmentId.equals("")) {
                if (symbolTable.containsKey(assignmentId.substring(3, assignmentId.length() - 1))) {
                    symbolTable.put(assignmentId.substring(3, assignmentId.length() - 1), value);
                } else {
                    System.out.println("Error ID " + assignmentId + " does not exist in symbol table");
                    System.exit(0);
                }
            }
            return value;
        }
        public void print(){
            System.out.print(value);
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
                System.exit(0);
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
                System.out.print(",");
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
            //add to the symbol table if its not there
            if((!symbolTable.containsKey(name)) && caller.equals("DEC")) {
                symbolTable.put(name, null);
            }
            else if((symbolTable.containsKey(name)) && caller.equals(("DEC"))){
                System.out.println("ERROR: Variable declared multiple times");
                System.exit(0);
            }
        }
        public int exec(){
            int returnVal = 0;
            switch (caller) {
                case "INPUT":
                    if (!fs.hasNextInt()) {
                        System.out.println("ERROR: invalid input");
                        System.exit(0);
                    }
                    symbolTable.put(name, fs.nextInt());
                    break;
                case "DEC":
                    //Declarations were messing this up so this is here to catch calls to ID from decl
                    break;
                case "ASSIGN":
                    //if being called in assign do nothing?
                    break;
                case "CASE":
                    //if being called by CASE
                    break;
                default:
                    if (!symbolTable.containsKey(name)) {
                        System.out.println("ERROR: " + name + " was not declared.");
                        System.exit(0);
                    }
                    if (symbolTable.get(name) == null) {
                        System.out.println("ERROR: " + name + " was not initialized.");
                        System.exit(0);
                    }
                    returnVal = symbolTable.get(name);
                    break;
            }
            return returnVal;
        }
        public void print(){
            System.out.print(name);
        }
    }

    private class If{
        private Cond c1;
        private StmtSeq s1;
        private StmtSeq s2;

        public If(){
            c1 = null;
            s1 = null;
            s2 = null;
        }

        private void parse(){
            //current token should be IF here during parse
            //parse the condition
            c1 = new Cond();
            c1.parse();
            //move to "THEN" token
            loc++;
            currentToken = s.get(loc);
            s1 = new StmtSeq();
            s1.parse();
            //get next token and check it is either endif or else
            loc++;
            currentToken = s.get(loc);
            if(currentToken.equals("ENDIF")){
                //grab the semicolon
                loc++;
                currentToken = s.get(loc);
            }else if(currentToken.equals("ELSE")){
                s2 = new StmtSeq();
                s2.parse();
                //check for endif
                loc++;
                currentToken = s.get(loc);
                if(!currentToken.equals("ENDIF")){
                    System.out.println("ERROR: Expected ENDWHILE token.");
                    System.exit(0);
                }
                //grab semicolon
                loc++;
                currentToken = s.get(loc);
            }
        }
        private void exec(){
            if(c1.exec()){
                s1.exec();
            }else{
                if(s2 != null){
                    s2.exec();
                }
            }

        }
        private void print(){
            System.out.print("if ");
            c1.print();
            System.out.println(" then");
            s1.print();
            if(s2 != null){
                System.out.print(indent);
                System.out.println("else");
                s2.print();
            }
            System.out.print(indent);
            System.out.println("endif;");
        }
    }

    private class Cond{
        private Cmpr cmpr1;
        private Cond cond1;
        private int opt;

        public Cond(){
            cmpr1 = null;
            cond1 = null;
        }

        private void parse(){
            //make sure not at end of token stream
            if(loc+1 >= s.size()){
                System.out.println("ERROR: Unexpected end to token stream");
                System.exit(0);
            }
            //right now token should still be if, look ahead one to see if there is a ! token
            if(!s.get(loc+1).equals("!")){
                opt = 1;
                //if it isn't we can grab first comp
                cmpr1 = new Cmpr();
                cmpr1.parse();
                //make sure not at end of token stream
                if(loc+1 >= s.size()){
                    System.out.println("ERROR: Unexpected end to token stream");
                    System.exit(0);
                }
                //check to see if the next token is or
                if(s.get(loc+1).equals("OR")){
                    opt = 2;
                    //move to or as currentToken
                    loc++;
                    currentToken = s.get(loc);
                    //if it is or we need to parse another cond
                    cond1 = new Cond();
                    cond1.parse();
                }
            }else{
                //if it does equal ! consume
                opt = 3;
                loc++;
                currentToken = s.get(loc);
                //check for paren
                loc++;
                currentToken = s.get(loc);
                if(!currentToken.equals("(")){
                    System.out.println("ERROR: Expected Open Paren");
                    System.exit(0);
                }
                cond1 = new Cond();
                cond1.parse();
                //check for paren
                loc++;
                currentToken = s.get(loc);
                if(!currentToken.equals(")")){
                    System.out.println("ERROR: Expected Close Paren");
                    System.exit(0);
                }
            }
        }
        private Boolean exec(){
            Boolean retVal;
            switch (opt){
                case 1:
                    retVal =  cmpr1.exec();
                    break;
                case 2:
                    retVal = (cmpr1.exec() || cond1.exec());
                    break;
                case 3:
                    retVal = !cond1.exec();
                    break;
                default:
                    System.out.println("Warning: COND took default in case switch");
                    retVal = false;
            }
            return retVal;
        }
        private void print(){
            switch (opt){
                case 1:
                    cmpr1.print();
                    break;
                case 2:
                    cmpr1.print();
                    System.out.print(" or ");
                    cond1.print();
                    break;
                case 3:
                    System.out.print("!");
                    System.out.print("(");
                    cond1.print();
                    System.out.print(")");
                    break;
                default:
                    System.out.println("Warning: COND took default in case switch (print)");
            }
        }
    }

    private class Cmpr{
        private Expr e1;
        private Expr e2;
        private int opt;

        public Cmpr(){
            e1 = null;
            e2 = null;
        }

        private void parse(){
            //grab the first expression and parse
            e1 = new Expr("");
            e1.parse();
            //check what the next token is
            loc++;
            currentToken = s.get(loc);
            switch (currentToken) {
                case "=":
                    opt = 1;
                    break;
                case "<":
                    opt = 2;
                    break;
                case "<=":
                    opt = 3;
                    break;
                default:
                    System.out.println("ERROR: Invalid CMPR, expected =, <, <=");
                    System.exit(0);
            }
            e2 = new Expr("");
            e2.parse();
        }
        private Boolean exec(){
            boolean retVal;
            switch (opt){
                case 1:
                    retVal = e1.exec() == e2.exec();
                    break;
                case 2:
                    retVal = e1.exec() < e2.exec();
                    break;
                case 3:
                    retVal = e1.exec() <= e2.exec();
                    break;
                default:
                    System.out.println("Warning: CMPR took default case is switch (exec)");
                    retVal = false;
                    break;
            }
            return retVal;
        }
        private void print(){
            switch (opt){
                case 1:
                    e1.print();
                    System.out.print("=");
                    e2.print();
                    break;
                case 2:
                    e1.print();
                    System.out.print("<");
                    e2.print();
                    break;
                case 3:
                    e1.print();
                    System.out.print("<=");
                    e2.print();
                    break;
                default:
                    System.out.println("Warning: CMPR took default case is switch (print)");
                    break;
            }
        }
    }

    private class Loop{
        private Cond cond1;
        private StmtSeq s1;

        public Loop(){
            cond1 = null;
            s1 = null;
        }

        private void parse(){
            //current token should be WHILE
            cond1 = new Cond();
            cond1.parse();
            //check for begin
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("BEGIN")){
                System.out.println("ERROR: Expected BEGIN");
                System.exit(0);
            }
            s1 = new StmtSeq();
            s1.parse();
            //grab endwhile and check
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("ENDWHILE")){
                System.out.println("ERROR: Expected ENDWHILE");
                System.exit(0);
            }
            //grab the semicolon and check it
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
                System.exit(0);
            }
        }
        private void exec(){
            while(cond1.exec()){
                s1.exec();
            }
        }
        private void print(){
            System.out.print("while ");
            cond1.print();
            System.out.println(" begin");
            s1.print();
            System.out.print("  endwhile");
            System.out.println(";");
        }
    }

    private class Case{
        private CaseLine caseLine1;
        private Expr e1;
        private Id id1;

        public Case(){
            caseLine1 = null;
            e1 = null;
            id1 = null;
        }

        private void parse(){
            //at this point the current token should be CASE
            //grab next token and check ID
            loc++;
            currentToken = s.get(loc);
            id1 = new Id("CASE");
            id1.parse();
            //grab next token and check OF
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("OF")){
                System.out.println("ERROR: Expected token OF");
                System.exit(0);
            }
            caseLine1 = new CaseLine();
            caseLine1.parse();
            //check for else
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("ELSE")){
                System.out.println("ERROR: Expected token ELSE");
                System.exit(0);
            }
            e1 = new Expr("");
            e1.parse();
            //check for END
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("END")){
                System.out.println("ERROR: Expected token END");
                System.exit(0);
            }
            //grab the semicolon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("SEMICOLON")){
                System.out.println("ERROR: Expected ;");
                System.exit(0);
            }
        }
        private void exec(){
            String caseId = id1.name;
            //pass the id name into exec for caseline
            if(!(caseLine1.exec(caseId))){
                symbolTable.put(caseId, e1.exec());
            }
        }
        private void print(){
            System.out.print("case ");
            id1.print();
            System.out.println(" of");
            ind();
            caseLine1.print();
            unind();
            System.out.println();
            System.out.print(indent);
            System.out.println("else");
            ind();
            e1.print();
            unind();
            System.out.println();
            System.out.print(indent);
            System.out.print("end");
            System.out.println(";");
            unind();
        }
    }

    private class CaseLine{
        private Const const1;
        private ConstList constList1;
        private Expr e1;
        private CaseLineFollow clf1;
        private int valToMatch;

        public CaseLine(){
            const1 = null;
            constList1 = null;
            e1 = null;
            clf1 = null;
        }

        private void parse(){
            //create the first constant in the list
            //move token to the constant
            loc++;
            currentToken = s.get(loc);
            const1 = new Const("");
            const1.parse();
            //next grab the const list, this could be empty set so leave as NULL if no values?
            constList1 = new ConstList();
            constList1.parse();
            //check for the colon
            loc++;
            currentToken = s.get(loc);
            if(!currentToken.equals("COLON")){
                System.out.println("ERROR: Expected token :");
                System.exit(0);
            }
            //create and parse the expression
            e1 = new Expr("");
            e1.parse();
            //create and parse the case-line-follow, might be empty, leave as null if empty
            clf1 = new CaseLineFollow();
            clf1.parse();
        }
        /* @param vtm vtm is the id that you want to match in the case*/
        private boolean exec(String vtm){
            boolean retVal = false;
            if(const1.exec() == symbolTable.get(vtm)){
                //change the value of vtm in the symbol table
                symbolTable.put(vtm, e1.exec());
                retVal = true;
            }
            if(constList1 != null){
                if(constList1.exec(vtm)){
                    //set the value of vtm
                    symbolTable.put(vtm, e1.exec());
                    retVal = true;
                }
            }
            if(clf1.exec(vtm)){
                retVal = true;
            }
            return retVal;
        }
        private void print(){
            const1.print();
            constList1.print();
            System.out.print(" : ");
            e1.print();
            clf1.print();
        }
    }

    private class ConstList{
        private Const c1;
        private ConstList cl1;

        public ConstList(){
            c1 = null;
            cl1 = null;
        }

        private void parse(){
            //make sure not at end of token stream
            if(loc+1 >= s.size()){
                System.out.println("ERROR: Unexpected end to token stream");
                System.exit(0);
            }
            //check to see if the next value is a comma
            if(s.get(loc+1).equals("COMMA")){
                //if it is comma consume the comma
                loc++;
                currentToken = s.get(loc);
                if(!currentToken.equals("COMMA")){
                    System.out.println("ERROR: Expected token ,");
                    System.exit(0);
                }
                //move to next const
                loc++;
                currentToken = s.get(loc);
                c1 = new Const("");
                c1.parse();
                cl1 = new ConstList();
                cl1.parse();
            }
            //if it wasn't a comma leave all the local variable for ConstList Null
        }
        private boolean exec(String vtm){
            boolean retVal = false;
            if(c1 != null) {
                if (c1.exec() == symbolTable.get(vtm)) {
                    retVal = true;
                }
            }
            if (cl1 != null){
                return cl1.exec(vtm);
            }
            return retVal;
        }
        private void print(){
            if(c1 != null){
                System.out.print(",");
                c1.print();
                cl1.print();
            }
        }
    }

    private class CaseLineFollow{
        private Expr e1;
        private CaseLine cl1;

        private void parse(){
            //make sure not at end of token stream
            if(loc+1 >= s.size()){
                System.out.println("ERROR: Unexpected end to token stream");
                System.exit(0);
            }
            //look ahead for BAR
            if(s.get(loc+1).equals("BAR")){
                //consume BAR
                loc++;
                currentToken = s.get(loc);
                cl1 = new CaseLine();
                cl1.parse();
            }

        }
        private boolean exec(String vtm){
            boolean retVal = false;
            if(cl1 != null){
                retVal = cl1.exec(vtm);
            }
            return retVal;
        }
        private void print(){
            if(cl1 != null){
                System.out.println();
                System.out.print("    | ");
                cl1.print();
            }
        }
    }

}
