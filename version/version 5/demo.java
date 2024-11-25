package com.example.swe_gui;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class demo {
    public static void main(String[]arg) {
        //enter file path wit \\ .else conflict
        String code = "D://UserFile//classes//software_class_2024//sw_project//testcode//StudentScoreProcessor.java";
        String data = "tempScore";
        try (FileInputStream path = new FileInputStream(code)) {
            CompilationUnit com = StaticJavaParser.parse(path);
            VarVisitor one = new VarVisitor(data);
            one.visit(com, null);
            one.writeToDot(101);

        } catch (IOException e) {
            System.out.println("error");
        }
    }
}
//make file and call Var Vist
            /*
            try (FileInputStream path = new FileInputStream(inputFilePathField) {
                CompilationUnit com = StaticJavaParser.parse(path);
                String temp=inputCodeString.getText();
                VarVisitor one2 = new VarVisitor(temp);
                one.writeToDot(101);

            } catch (IOException e) {
                System.out.println("error");
            }
            */