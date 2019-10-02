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
$java Main ../Test/P10.txt ../Test/input.txt

