import java.io.*;

public class Lexscan {
    private File file = null;
    private FileReader fr = null;
    private int ch,prev;

    public Lexscan(String inFile){
        try {
            this.file = new File(inFile);
            this.fr = new FileReader(file);
            this.ch = fr.read();
            this.prev = this.ch;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void advance(){
        try{
            //store the previous character and grab the next one
            prev = ch;
            ch = fr.read();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getNext(){
        //String to hold return value
        String out = "";
        //String to hold characters to stop on, may indicate a token
        String stops = ";,=!+*():<|";
        //consume any whitespace
        while(Character.isWhitespace((char) ch)){
            advance();
        }

        //concat output string until we reach whitespace or semi colon
        while(!(Character.isWhitespace((char) ch)) && (ch != -1) && (stops.indexOf((char) ch) == -1)){
            out += (char) ch;
            advance();
        }

        //Compare the values from the input to known tokens
        //Check for Comma
        if((out.length() == 0) && (char) ch == ','){
            out = "COMMA";
            advance();
        }
        //Check for left paren
        else if((out.length() == 0) && (char) ch == '('){
            out = "(";
            advance();
        }
        //Check for right paren
        else if((out.length() == 0) && (char) ch == ')'){
            out = ")";
            advance();
        }
        //Check for left bar
        else if((out.length() == 0) && (char) ch == '|'){
            out = "BAR";
            advance();
        }
        //Check for Times
        else if((out.length() == 0) && (char) ch == '*'){
            out = "*";
            advance();
        }
        //Check for minus
        else if((out.length() == 0) && (char) ch == '-'){
            out = "-";
            advance();
        }
        //Check for ex point
        else if((out.length() == 0) && (char) ch == '!'){
            out = "!";
            advance();
        }
        //Check for Equals
        else if((out.length() == 0) && (char) ch == '='){
            out = "=";
            advance();
        }
        //Check for Semicolon
        else if((out.length() == 0) && (char) ch == ';'){
            out = "SEMICOLON";
            advance();
        }
        //Check for Plus
        else if((out.length() == 0) && (char) ch == '+'){
            out = "+";
            advance();
        }
        //Check for Assign
        else if((out.length() == 0) && (char) ch == ':'){
            out = "COLON";
            advance();
            //Check the next character is =
            if((char) ch == '='){
                out = "ASSIGN";
                advance();
            }
        }
        //Check if less than is followed by equals
        else if((out.length() == 0) && (char) ch == '<'){
            out = "<";
            advance();
            //Check the next character is =
            if((char) ch == '='){
                out = "<=";
                advance();
            }
        }
        //check for program
        else if(out.equals("program")){
            out = "PROGRAM";
        }
        //check for int
        else if(out.equals("int")){
            out = "INT";
        }
        //check from begin
        else if(out.equals("begin")){
            out = "BEGIN";
        }
        //check from input
        else if(out.equals("input")){
            out = "INPUT";
        }
        //check from output
        else if(out.equals("output")){
            out = "OUTPUT";
        }
        //check from while
        else if(out.equals("while")){
            out = "WHILE";
        }
        //check from endwhile
        else if(out.equals("endwhile")){
            out = "ENDWHILE";
        }
        //check from if
        else if(out.equals("if")){
            out = "IF";
        }
        //check from then
        else if(out.equals("then")){
            out = "THEN";
        }
        //check from endif
        else if(out.equals("endif")){
            out = "ENDIF";
        }
        //check from or
        else if(out.equals("or")){
            out = "OR";
        }
        //check from end
        else if(out.equals("end")){
            out = "END";
        }
        else if(ch == -1){
            out = "EOF";
        }
        else{
            String tokenize = "ID[" + out + "]";
            out = tokenize;
        }
        return out;
    }
}
