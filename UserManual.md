# Data Flow Generator - User Manual

## **Overview**

**Data Flow Generator** is a JavaFX-based application designed to help developers visualize and analyze data flow in their Java code. 
The tool generates and displays flowcharts and text-based representations of variable data flows. 
It supports both new and existing projects, allowing users to process and visualize their code efficiently.

---

## **Features**

1. **Create a New Project**  
   Users can create a new project by selecting the input Java file and specifying a variable name to track.

2. **Open an Existing Project**  
   Users can open an existing project by selecting a project folder and viewing related data flow files.

3. **Choose Output Format**  
   Users can select to view the output in either **Text** format (as raw data flow information) or **Flow Chart** format (as a graphical representation).

4. **Generate and Visualize Flowcharts**  
   Flowcharts are generated from `.dot` files and rendered into PNG images for easy viewing.

---

## **Installation**

1. **Clone or Download the Project**  
   Clone the repository or download the source code from [GitHub repository link].

2. **Install Dependencies**  
   - **Graphviz**: This project uses Graphviz for generating flowchart images. Ensure Graphviz is installed and available in your system's PATH.  
   - You can download it from [Graphviz GitHub](https://github.com/nidi3/graphviz-java).
   - **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
   - You can download it from [Javafx website](https://openjfx.io/openjfx-docs/#install-java).

3. **Run the Application**  
   You can run the application by executing the `Diagram.java` file.

---

## **How to Use**

### 1. **Launch the Application**  
Run the application by executing the `Diagram` class. The window will have the following components:

- **Input File Path**: A text field where you specify the path to your Java source file.
- **Previous Project**: A field for selecting an existing project if you're working with an older project.
- **Project Type**: A dropdown to select either **New Project** or **Old Project**.
- **View Output As**: A dropdown to choose between **Text** or **Flow Chart** output.

### 2. **Select Project Type**

- **New Project**:  
   Choose this option if you're starting a new project. Provide the path to the Java file you want to analyze and enter the variable name to track (e.g., `data`).

- **Old Project**:  
   Select this option to work with an existing project. Click the "Browse" button and select the folder containing your old project files.

### 3. **Proceed with Project Processing**  
Once you've set up the input fields, click **Proceed**.  
- If creating a new project, the application will process the Java file and generate output in your chosen format.  
- If using an old project, it will process the project folder and generate the relevant output.

---

## **Working with Flowcharts**

When selecting **Flow Chart** as the output type:

1. **View Flowchart**  
   The application will read the `.dot` file associated with the project and render it into a PNG image.
   This flowchart will be displayed in a new window for easy analysis.

3. **Save Flowchart**  
   Users can save the flowchart as an image file (e.g., PNG) for later reference or sharing.

---

## **Supported File Formats**

- **Java Files (`*.java`)**: Input files containing the Java code to be analyzed.
- **Text Files (`*.txt`)**: Contain the raw data flow information, which can be viewed directly in the application.
- **Graphviz DOT Files (`*.dot`)**: Define the flowchart structure, which can be rendered as an image.
- **Image Files (`*.png`)**: Flowchart images generated from `.dot` files.

---

## **FAQ**

**Q1: How do I open an old project?**  
Click the "Browse" button next to the "Previous Project" field and select the folder containing your old project files.

**Q2: Can I edit the flowchart?**  
The flowchart is generated as an image (PNG) and cannot be edited directly. 
To modify the data flow, you need to update the underlying code and regenerate the flowchart.

**Q3: How can I ensure Graphviz is installed?**  
Ensure that the Graphviz executable is in your system's PATH. You can download it from the [Graphviz github]( https://github.com/nidi3/graphviz-java).

---

## **Conclusion**

The **Data Flow Generator** application is a powerful tool for visualizing data flow in Java projects. 
Whether creating a new project or analyzing an existing one, this tool simplifies generating flowcharts and textual data flow representations for improved understanding and analysis.

