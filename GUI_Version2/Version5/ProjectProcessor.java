package com.example.test122;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ProjectProcessor {

    private final FileService fileService;

    public ProjectProcessor(FileService fileService) {
        this.fileService = fileService;
    }

    public void processProject(String filePath, String outputType, String inputCodeString, Stage primaryStage) {
        if (inputCodeString == null || inputCodeString.trim().isEmpty()) {
            fileService.showAlert("Error", "Variable name cannot be empty.");
            return;
        }

        try (FileInputStream path = new FileInputStream(filePath)) {
            CompilationUnit com = StaticJavaParser.parse(path);
            VarVisitor visitor = new VarVisitor(inputCodeString);
            visitor.visit(com, null);

            if (!visitor.isVariableFound()) {
                fileService.showAlert("Error", "Variable '" + inputCodeString + "' not found in the Java file.");
                return;
            }

            visitor.writeToDot(101);

            // Get file paths for .txt and .dot outputs
            File txtFile = visitor.getTxtPath().toFile();
            File dotFile = visitor.getPathDot().toFile();

            // UI Components instance
            UIComponents uiComponents = new UIComponents();

            // Display output based on user selection
            if (outputType.equals("Text")) {
                if (!txtFile.exists()) {
                    fileService.showAlert("Error", "Text file not found: " + txtFile.getAbsolutePath());
                    return;
                }
                uiComponents.showTextFile(txtFile);//change name
            } else if (outputType.equals("Flow Chart")) {
                if (!dotFile.exists()) {
                    fileService.showAlert("Error", "DOT file not found: " + dotFile.getAbsolutePath());
                    return;
                }
                uiComponents.showDotFile(dotFile);// change name

                // Render and display flowchart
                uiComponents.renderDotToImage(dotFile);//change name
            }
        } catch (IOException e) {
            fileService.showAlert("Error", "Error processing the file: " + e.getMessage());
        }
    }
}
