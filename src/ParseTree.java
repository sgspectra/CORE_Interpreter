public class ParseTree {
    //Parse tree will contain one instance of class program as root
    private Program p;
    //Scanner that will supply tokens
    private Lexscan s;
    //Variable to hold token from Scanner
    private String currentToken;

    public ParseTree(Lexscan scannerIn){
        s = scannerIn;
        p = new Program();
        currentToken = "";
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
                System.out.println(currentToken);
                //create declaration statement and parse it
                d1 = new DecSeq();
                d1.parse();
            }else{
                System.out.println("ERROR: Expecting keyword PROGRAM");
            }
            //Check if the next token available is BEGIN
            currentToken = s.getNext();
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

    //TODO Id
    private class Id{
        private String name;
        private int value;

        public Id(){
            name = currentToken.substring(3, currentToken.length()-2);
        }

        public void setValue(int val){
            value = val;
        }

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

}
