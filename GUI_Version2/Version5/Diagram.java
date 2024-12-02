import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Diagram extends Application {
    private final FileService fileService = new FileService();
    private final TextField previousProjectField = new TextField();
    private final TextField inputFilePathField = new TextField();
    private final TextField inputCodeString = new TextField();
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
        Button browseInputButton = createBrowseButton(primaryStage, inputFilePathField);

        // Previous Project
        Label previousProjectLabel = new Label("Previous Project:");

        // Code Word input
        Label CodeWord = new Label("Input variable name:");
        inputCodeString.setPromptText("data");
        inputCodeString.setPrefWidth(100);

        // Create "Browse" button for Previous Project
        Button browsePreviousButton = new Button("Browse");
        browsePreviousButton.setOnAction(e -> showProjectListDialog(primaryStage));  // Pass primaryStage here

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

        proceedButton.setOnAction(e -> proceed(primaryStage));  // Pass primaryStage to proceed method

        // Add components to GridPane
        grid.add(inputFilePathLabel, 0, 0);
        grid.add(inputFilePathField, 1, 0);
        grid.add(browseInputButton, 2, 0);

        grid.add(previousProjectLabel, 0, 4);
        grid.add(browsePreviousButton, 1, 4);

        grid.add(projectTypeLabel, 0, 2);
        grid.add(projectTypeComboBox, 1, 2);

        grid.add(viewOutputLabel, 0, 3);
        grid.add(viewOutputComboBox, 1, 3);

        // Add variable name input field
        grid.add(CodeWord, 0, 1);
        grid.add(inputCodeString, 1, 1);

        grid.add(proceedButton, 1, 5);

        // Create Scene and show stage
        Scene scene = new Scene(grid, 530, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to proceed with project processing
    private void proceed(Stage primaryStage) {
        String filePath = inputFilePathField.getText();
        String codeString = inputCodeString.getText();
        String codeType = projectTypeComboBox.getValue();
        String outputType = viewOutputComboBox.getValue();

        // Validate file path and code string
        if (codeString == null || codeString.trim().isEmpty()) {
            fileService.showAlert("Error", "Variable name cannot be empty.");
            return;
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            fileService.showAlert("Error", "File path cannot be empty.");
            return;
        }

        // Proceed with project processing
        if (codeType.equals("New Project")) {
            projectProcessor.processProject(filePath, outputType, codeString, null);
            fileService.saveProject(filePath);
        } else if (codeType.equals("Old Project")) {
            String previousProject = previousProjectField.getText();
            if (previousProject == null || previousProject.trim().isEmpty()) {
                fileService.showAlert("Error", "Please select a previous project.");
                return;
            }
            projectProcessor.processProject(previousProject, outputType, codeString, null);
        }

    }

    // Method to create a "Browse" button that opens a FileChooser
    private Button createBrowseButton(Stage primaryStage, TextField targetField) {
        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                targetField.setText(selectedFile.getAbsolutePath());
            }
        });
        return browseButton;
    }

    // Method to handle file selection (including .dot files)
    private void showProjectListDialog(Stage primaryStage) {
        // Open folder selection dialog
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Old Project Folder");

        // Open the dialog and get the folder path
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            // Get all files in the selected directory
            File[] files = selectedDirectory.listFiles();

            if (files != null && files.length > 0) {
                // Create a list to store all file names
                List<String> fileNames = Arrays.stream(files)
                        .map(File::getName)  // Get the name of each file
                        .toList();

                // Create a dialog to display the files
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Select File to View");
                dialog.setHeaderText("Files in " + selectedDirectory.getName());

                // Create ListView to show the files
                ListView<String> listView = new ListView<>();
                listView.getItems().addAll(fileNames);

                // Add listener to ListView for file selection
                dialog.getDialogPane().setContent(listView);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);



                // Handle the selection of a file
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        String selectedFileName = listView.getSelectionModel().getSelectedItem();
                        if (selectedFileName != null) {
                            // Get the full path of the selected file
                            File selectedFile = new File(selectedDirectory, selectedFileName);

                            // Check the file extension and display accordingly
                            if (selectedFileName.endsWith(".txt")) {
                                // Display the .txt content
                                displayTextFile(selectedFile);
                            } else if (selectedFileName.endsWith(".png") || selectedFileName.endsWith(".jpg")) {
                                // Display the flowchart image
                                displayFlowChart(selectedFile);
                            } else if (selectedFileName.endsWith(".dot")) {
                                // Process and display the .dot file
                                displayDotFile(selectedFile);
                            } else {
                                // If it's any other file type, show an alert
                                fileService.showAlert("File Type Not Supported", "This file type is not supported for display.");
                            }
                        }
                    }
                    return null;
                });

                dialog.showAndWait();
            } else {
                fileService.showAlert("Empty Folder", "The selected folder is empty.");
            }
        }
    }

    // Display the text content (e.g., from the .txt file)
    private void displayTextFile(File file) {
        try {
            // Read the content of the .txt file
            List<String> lines = Files.readAllLines(file.toPath());
            String content = String.join("\n", lines);  // Join lines to create the file content as a single string

            // Show the content in a dialog or a separate window
            TextArea textArea = new TextArea(content); // Create a TextArea to display the content
            textArea.setEditable(false);  // Make it read-only
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Text File Content");
            alert.getDialogPane().setContent(textArea); // Set the TextArea as the content of the alert
            alert.showAndWait();  // Show the alert dialog

        } catch (IOException e) {
            e.printStackTrace();
            fileService.showAlert("Error", "Failed to read the text file.");
        }
    }


    // Display the contents of a .dot file (as text or convert to image)
    private void displayDotFile(File file) {
        try {
            // Read the content of the .dot file
            String dotContent = new String(Files.readAllBytes(file.toPath()));

            // Show the .dot file content in a TextArea (for visualization)
            TextArea textArea = new TextArea(dotContent);
            textArea.setEditable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DOT File Content");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            fileService.showAlert("Error", "Failed to read the .dot file.");
        }
    }
  
    private void displayFlowChart(File file) {
        // Create ImageView and set the image
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);

        // Set ImageView properties
        imageView.setPreserveRatio(true);  // Maintain aspect ratio
        imageView.setFitWidth(400);
        imageView.setFitHeight(600);// Resize image to fit the width (can be adjusted)

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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
