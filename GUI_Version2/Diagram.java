package com.example.swe_gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Diagram extends Application {
    private final FileService fileService = new FileService();
    private final TextField previousProjectField = new TextField();
    private final TextField inputFilePathField = new TextField();
    private final ProjectProcessor projectProcessor = new ProjectProcessor(fileService);

    // Add ComboBoxes as instance variables
    private ComboBox<String> projectTypeComboBox;
    private ComboBox<String> viewOutputComboBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Flow Generator");

        // Create GridPane for layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Input File Path
        Label inputFilePathLabel = new Label("Input File Path:");
        inputFilePathField.setPromptText("Enter file path");
        inputFilePathField.setPrefWidth(300);

        // Create "Browse" button for Input File Path
        Button browseInputButton = createBrowseButton(primaryStage, inputFilePathField, "Java Files", "*.java");

        // Previous Project
        Label previousProjectLabel = new Label("Previous Project:");
        previousProjectField.setPromptText("Select old project");
        previousProjectField.setPrefWidth(250);

        // Create "Browse" button for Previous Project
        Button browsePreviousButton = new Button("Browse");
        browsePreviousButton.setOnAction(e -> showProjectListDialog());

        // Select Project Type
        Label projectTypeLabel = new Label("Select Project Type:");
        projectTypeComboBox = new ComboBox<>();
        projectTypeComboBox.getItems().addAll("New Project", "Old Project");
        projectTypeComboBox.setValue("New Project");

        // View Output As
        Label viewOutputLabel = new Label("View Output As:");
        viewOutputComboBox = new ComboBox<>();
        viewOutputComboBox.getItems().addAll("Text", "Flow Chart");
        viewOutputComboBox.setValue("Text");

        // Proceed Button
        Button proceedButton = new Button("Proceed");
        proceedButton.setOnAction(e -> proceed());

        // Add components to GridPane
        grid.add(inputFilePathLabel, 0, 0);
        grid.add(inputFilePathField, 1, 0);
        grid.add(browseInputButton, 2, 0);

        grid.add(previousProjectLabel, 0, 1);
        grid.add(previousProjectField, 1, 1);
        grid.add(browsePreviousButton, 2, 1);

        grid.add(projectTypeLabel, 0, 2);
        grid.add(projectTypeComboBox, 1, 2);

        grid.add(viewOutputLabel, 0, 3);
        grid.add(viewOutputComboBox, 1, 3);

        grid.add(proceedButton, 1, 4);

        // Create Scene and show stage
        Scene scene = new Scene(grid, 530, 210);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Custom proceed method as per your specifications
    private void proceed() {
        String filePath = inputFilePathField.getText();
        String codeType = projectTypeComboBox.getValue();
        String outputType = viewOutputComboBox.getValue();

        if (codeType.equals("New Project")) {
            if (filePath.isEmpty()) {
                fileService.showAlert("Error", "Please enter the file path New Project.");
                return;
            }
            projectProcessor.processProject(filePath, outputType, null);
            fileService.saveProject(filePath);
        } else {
            String previousProject = previousProjectField.getText();
            if (previousProject == null || previousProject.isEmpty()) {
                fileService.showAlert("Error", "Please enter the file path Old Project.");
                return;
            }
            projectProcessor.processProject(previousProject, outputType, null);
        }
    }

    // Method to create a "Browse" button that opens a FileChooser
    private Button createBrowseButton(Stage primaryStage, TextField targetField, String fileTypeDescription, String fileExtension) {
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileTypeDescription, fileExtension));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                targetField.setText(selectedFile.getAbsolutePath());
            }
        });
        return browseButton;
    }

    // Method to show a dialog with a list of previous projects
    private void showProjectListDialog() {
        List<String> savedProjects = fileService.loadPreviousProjects();
        if (savedProjects.isEmpty()) {
            fileService.showAlert("No Previous Projects", "No previous projects available.");
            return;
        }

        // Create a dialog for selecting a previous project
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Select Previous Project");
        dialog.setHeaderText("Your Previous File Projects");
        dialog.getDialogPane().lookup(".header-panel").setStyle("-fx-font-weight: bold;");

        // Create a TextField for project name search
        TextField searchField = new TextField();
        searchField.setPromptText("Enter project name");
        searchField.setPrefWidth(500);

        // Create a ListView to display the projects
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(savedProjects);
        listView.setPrefSize(500, 300);

        // Add a listener to the search field to filter the ListView based on input
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            for (String project : savedProjects) {
                if (project.toLowerCase().contains(newValue.toLowerCase())) {
                    listView.getItems().add(project);
                }
            }
        });

        // Create a VBox to hold the search field and list view
        VBox vbox = new VBox(10, searchField, listView);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().setPrefSize(520, 400);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Set the result converter to return the selected item on OK, or null on Cancel
        dialog.setResultConverter(dialogButton ->
                dialogButton == ButtonType.OK ? listView.getSelectionModel().getSelectedItem() : null
        );

        // Show the dialog and set the selected project to the text field if available
        dialog.showAndWait().ifPresent(selectedProject -> previousProjectField.setText(selectedProject));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
