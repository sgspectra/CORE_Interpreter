import java.io.*;

public class old_scn {

    FileReader fr = null;
    File file = null;
    String firstToken = "";

    //Constructor for old_scn
    public old_scn(String fileName){
        try {
            this.file = new File(fileName);
            this.fr = new FileReader(file);
            this.firstToken = readToWhite();
        }catch (IOException e){
            System.out.println("File IO Error");
            e.printStackTrace();
        }
    }

    /*method to take characters from file up to next whitespace and return as string*/
    /*public String getWord(){
        String word = "";
        try{
            //Get next character in stream
            int nextCh = fr.read();
            //get all characters until next white space
            while(!(Character.isWhitespace((char) nextCh)) && nextCh != -1){
                word += (char) nextCh;
                nextCh = fr.read();
            }
            if(nextCh == -1){
                word = "EOF";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return word;
    }*/

    //Method to read characters until next whitespace
    //returns string "EOF" if at end of file
    private String readToWhite(){
        String word = "";
        try{
            //Get next character in stream
            int nextCh = fr.read();
            //consume leading whitespace
            nextCh = consume((char) nextCh);
            //get all characters until next white space
            while(!(Character.isWhitespace((char) nextCh)) && nextCh != -1){
                word += (char) nextCh;
                nextCh = fr.read();
            }
            if(nextCh == -1){
                word = "EOF";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return word;
    }

    //Method to read characters until next semicolon
    //returns string "EOF" if at end of file
    private String readToSemi(){
        String word = "";
        try{
            //Get next character in stream
            int nextCh = fr.read();
            //Consume leading whitespace
            nextCh = consume((char) nextCh);
            //get all characters until next white space
            while(((char) nextCh != ';') && nextCh != -1){
                word += (char) nextCh;
                nextCh = fr.read();
            }
            if(nextCh == -1){
                word = "EOF";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return word;
    }

    //Method to Consume Whitespace
    private int consume(int space){
        while(Character.isWhitespace((char) space)){
            try {
                space = fr.read();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return space;
    }

    /*method to match a string to a known token */
    /*public String matchToken(String test){
        //String to compare input string against tokens
        String token = "";
        while(!(test.equals("EOF"))) {
            if (test.equals("program")) {
                token = "PROGRAM";
                System.out.println(token);
                test = readToWhite();
            } else if (test.equals("int")) {
                token = "INT";
                System.out.println(token);
                test = readToSemi();
            } else if (test.equals("begin")) {
                token = "BEGIN";
                System.out.println(token);
                test = readToWhite();
            } else if (test.equals("end")) {
                token = "END";
                System.out.println(token);
                test = readToWhite();
            } else if (test.equals("input")){
                token = "INPUT";
                System.out.println(token);
                test = readToWhite();
            }else {
                token = "not a valid token: " + test;
                System.out.println(token);
                test = readToWhite();
            }
            matchToken(test);
        }
        return token;
    }*/
}
