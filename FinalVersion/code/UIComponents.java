package com.example.test122;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UIComponents {

    public Object showTextOutput;

    // Show text output in a new stage, now displays DOT code
    public void showTextOutput(String content) {
        Stage textOutputStage = new Stage();
        textOutputStage.setTitle("Text Output");

        TextArea textArea = new TextArea(content);
        textArea.setWrapText(false);
        textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 600, 400);
        textOutputStage.setScene(scene);
        textOutputStage.show();
    }
    public void ShowDotFileOutput(String content){
        Stage textOutputStage = new Stage();
        textOutputStage.setTitle("Dot File");

        TextArea textArea = new TextArea(content);
        textArea.setWrapText(false);
        textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 600, 400);
        textOutputStage.setScene(scene);
        textOutputStage.show();
    }

}