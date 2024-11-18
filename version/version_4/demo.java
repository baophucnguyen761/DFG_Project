import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class demo {
    public static void main(String[]arg){
        //enter file path wit \\ .else conflict
        //"D:\UserFile\classes\software_class_2024\sw_project\testcode\StudentScoreProcessor.java"
        String code ="D://UserFile//classes//software_class_2024//sw_project//testcode//StudentScoreProcessor.java";
        String data = "tempScore";
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
                //System.out.println("va ** "+one.VA.get(0)+" **");
                //System.out.println("ne ** "+one.NE.get(0)+" **");
                //System.out.println("ae ** "+one.AE.get(1)+" **");//add the sinka and soucre
                //System.out.println("ss ** "+one.SS1.get(1)+" **");
               // System.out.println("ss ** "+one.SS2.get(1)+" **");
                one.writeToDot(101);

            } catch (IOException e) {
                System.out.println("error");
            }
        }
        else {System.exit(0);}//placehold code fix later


    }
}
