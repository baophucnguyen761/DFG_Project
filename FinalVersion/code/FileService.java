package com.example.test122;

import javafx.scene.control.Alert;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private static final String PROJECT_FILE = "savedProjects.txt";

    public void saveProject(String projectPath) {
        if (!loadPreviousProjects().contains(projectPath)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECT_FILE, true))) {
                writer.write(projectPath);
                writer.newLine();
            } catch (IOException ex) {
                showAlert("Error", "Error saving project: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public List<String> loadPreviousProjects() {
        List<String> projects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROJECT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                projects.add(line);
            }
        } catch (IOException ex) {
            // File not found or empty; do nothing
        }
        return projects;
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
