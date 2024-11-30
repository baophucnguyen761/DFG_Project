package com.example.swe_gui;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class demo {
    public static void main(String[]arg) {
        //enter file path wit \\ .else conflict
        String code = "D://UserFile//classes//software_class_2024//sw_project//testcode//StudentScoreProcessor.java";
        String data = "tempScore";
        Path temp=Paths.get(code);

        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(temp.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        //
        String fileText = fileContent.toString();
        String regex = "\\b" + Pattern.quote(data) + "\\b"; // \b ensures a whole word match
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileText);
        //
        if (matcher.find()) {
            // Word found, proceed with processing
            try (FileInputStream path = new FileInputStream(code)) {
                CompilationUnit com = StaticJavaParser.parse(path);
                VarVisitor one = new VarVisitor(data);
                one.visit(com, null);
                one.writeToDot(101);
            } catch (IOException e) {
                System.out.println("Error processing the file.");
            }
        } else {
            System.out.println("Word not found");
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