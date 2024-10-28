//for testing and base command line interaction
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
//import com.github.javaparser.printer.Printer;

import java.io.*;
import java.util.Scanner;
//for run the java parser and .dot maker
public class DataFlowsGenerator {
    public static void main(String[]arg) throws IOException {
        //enter file path wit \\ .else conflict
        String code ="D://UserFile//software_class_2024//test//src//Student.java";
        String data = "name";
        Scanner input=new Scanner(System.in);

        System.out.println("make new project y or n");
        String answer = "y";//input.nextLine();

        if (answer.equals("y")) {
            /*add a project name and make folder*/
            System.out.println("if block");//check
            try (FileInputStream path = new FileInputStream(code)) {
                CompilationUnit com = StaticJavaParser.parse(path);
                VarVisitor one = new VarVisitor(data);
                one.visit(com, null);
                System.out.println("va ** "+one.VA.get(1)+" **");
                System.out.println("ne ** "+one.NE.get(1)+" **");
                System.out.println("ae ** "+one.AE.get(0)+" **");
                one.writeToFile(101);
            } catch (IOException e) {
                System.out.println("error");
            }

        }
        else {System.exit(0);}//placehold code fix later
    }//end of main
    //make this method print the dfd to text. need to test
    public void PrintDotFile(CompilationUnit com){
        try {
            DotPrinter printer = new DotPrinter(true);
            String dotRepresentation = printer.output(com);
            FileWriter writer = new FileWriter("example.dot");
            writer.write(dotRepresentation);
            writer.close();
        }
                    //// Print the AST in DOT format whole java file
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        //----------------

    }

}