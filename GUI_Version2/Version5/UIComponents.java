package com.example.test122;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UIComponents {

    // Display text file content
    public void showTextFile(File textFile) {
        try {
            String content = Files.readString(textFile.toPath());
            showTextOutput(content, "Text File Content");
        } catch (IOException e) {
            showError("Error", "Failed to read text file: " + e.getMessage());
        }
    }

    // Display DOT file content
    public void showDotFile(File dotFile) {
        try {
            String content = Files.readString(dotFile.toPath());
            showTextOutput(content, "DOT File Content");
        } catch (IOException e) {
            showError("Error", "Failed to read DOT file: " + e.getMessage());
        }
    }

    // Render DOT file to PNG and display the image
    public void renderDotToImage(File dotFile) {
        String outputImagePath = dotFile.getParent() + "/" + dotFile.getName().replace(".dot", ".png");
        try {
            String dotContent = new String(Files.readAllBytes(dotFile.toPath()));

            MutableGraph graph = new Parser().read(dotContent);  // Using Graphviz's Parser to read the .dot content
            Graphviz.fromGraph(graph)
                    .render(Format.PNG)
                    .toFile(new File(outputImagePath));

            // Show the generated flowchart image
            displayFlowChart(new File(outputImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to render DOT file to image: " + e.getMessage());
        }
    }

    // Display the generated flowchart image
    private void displayFlowChart(File file) {
        if (!file.exists()) {
            showError("Error", "Flowchart image file not found: " + file.getAbsolutePath());
            return;
        }

        try {
            // Create ImageView and set the image
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);

            // Set ImageView properties
            imageView.setPreserveRatio(true);  // Maintain aspect ratio
            imageView.setFitWidth(400);
            imageView.setFitHeight(600);  // Resize image to fit the width (can be adjusted)

            // Create a new Stage to display the image
            Stage imageStage = new Stage();
            imageStage.setTitle("Flowchart Image");

            // Use a StackPane to center the ImageView
            StackPane root = new StackPane();
            root.getChildren().add(imageView);

            // Create a scene with the image and set it to the stage
            Scene imageScene = new Scene(root, 800, 600);  // Set initial size
            imageStage.setScene(imageScene);

            // Make the window resizable
            imageStage.setResizable(true);

            // Show the stage with the image
            imageStage.show();
        } catch (Exception e) {
            showError("Error", "Failed to display flowchart image: " + e.getMessage());
        }
    }

    // Helper method to display text content
    private void showTextOutput(String content, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);

        TextArea textArea = new TextArea(content);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to show error messages
    private void showError(String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);

        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 400, 200);
        stage.setScene(scene);
        stage.show();
    }
}
