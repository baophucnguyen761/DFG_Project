package com.example.swe_gui;

import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class ProjectProcessor {

    private final FileService fileService;

    public ProjectProcessor(FileService fileService) {
        this.fileService = fileService;
    }

    public void processProject(String filePath, String outputType, Stage primaryStage) {
        if (outputType.equals("Text")) {
            String variableReport = "Still working";
            UIComponents.showTextOutput(variableReport);
        } else {
            System.out.println("Flow Chart view selected, but still working on it, no output will be displayed.");
        }
    }
}
