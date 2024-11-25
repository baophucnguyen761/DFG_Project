package com.example.swe_gui;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProjectProcessor {

    private final FileService fileService;

    public ProjectProcessor(FileService fileService) {
        this.fileService = fileService;
    }

    public void processProject(String filePath, String outputType,String inputCodeString, Stage primaryStage) {
        ///
        String content;
        try (FileInputStream path = new FileInputStream(filePath)) {
            CompilationUnit com = StaticJavaParser.parse(path);
            VarVisitor visitor = new VarVisitor(inputCodeString);
            visitor.visit(com, null);
            visitor.writeToDot(101); // Adjusted the constant as per your needs
            if(outputType.equals("Text")) {
                content = Files.readString(visitor.getTxtPath());
                UIComponents uiComponents = new UIComponents();
                uiComponents.showTextOutput(content);
            }
            if(outputType.equals("Flow Chart")){
                content = Files.readString(visitor.getPathDot());
                UIComponents uiComponents = new UIComponents();
                uiComponents.ShowDotFileOutput(content);
            }

        } catch (IOException e) {
            fileService.showAlert("Error", "Error processing the file: " + e.getMessage());
            return;
        }


    }
}
