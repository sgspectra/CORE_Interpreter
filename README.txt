To Compile:
Change directory to core_interpreter
$cd core_interpreter

Compile the following files using javac: Main.java,ParseTree.java,Lexscan.java
into the bin directory
$mkdir bin
$javac -d bin src/Main.java src/ParseTree.java src/Lexscan.java

To Run:
Change directory to bin
$cd bin

Call Main with 2 arguments, path to program and path to input
$java Main ../Test/p10.txt ../Test/input.txt

CORE Language (Context Free Grammar):
<prog> ::= program <decl-seq> begin <stmt-seq> end
<decl-seq> ::= <decl> | <decl> <decl-seq>
<stmt-seq> ::= <stmt> | <stmt> <stmt-seq>
<decl> ::= int <id-list> ;
<id-list> ::= id | id , <id-list>
<stmt> ::= <assign> | <if> | <loop> | <in> | <out> | <case>
<assign> ::= id := <expr> ;
<in> ::= input <id-list> ;
<out> ::= output <expr> ;
<case> ::= case id of <case-line> else <expr> end ;
<case-line> ::= const <const-list> : <expr> <case-line-follow>
<const-list> ::= <e> | , const <const-list>
<case-line-follow> ::= <e> | BAR <case-line>
<if> ::= if <cond> then <stmt-seq> endif ;
       | if <cond> then <stmt-seq> else <stmt-seq> endif ;
<loop> ::= while <cond> begin <stmt-seq> endwhile ;
<cond> ::= <cmpr> | ! ( <cond> ) | <cmpr> or <cond>
<cmpr> ::= <expr> = <expr> | <expr> < <expr> | <expr> <= <expr>
<expr> ::= <term> | <term> + <expr> | <term>  – <expr>
<term> ::= <factor> | <factor> * <term>
<factor> ::= const | id | ( <expr> )
<id> ::= <letter> | <id><letter> | <id><digit>
<letter> ::= A...Z | a...z
<const> ::= <digit> | <const><digit>
<digit> ::= 0...9