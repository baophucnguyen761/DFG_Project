package com.example.test122;


import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.*;
import java.util.stream.Stream;

public class VarVisitor extends VoidVisitorAdapter<Void> {
    private final String variableName;
    List<String> NE = new ArrayList<>();
    List<String> VA = new ArrayList<>();
    List<String> AE = new ArrayList<>();
    List<String> SS1 = new ArrayList<>();
    List<String> SS2 = new ArrayList<>();
    List<String> allNode = new ArrayList<>();
    String[] source = {".nextLine()", ".nextInt()", "nextDouble()",
            "nextBoolean()", "nextFloat()", "System.in", "BufferedReader.readLine", "FileInputStream", "BufferReader", "" +
            "File.ReadAllLines"};//,"this"
    String[] sinks = {"System.out.print", "return", "FileOutputStream", "BufferedWriter", "File.write"};

    int NEint = 0, VDint = 0, AEint = 0, SS1int = 0, SS2int = 0, Lineint = 0;
    Path pathDot;//dot file
    Path pathTxt;//txt file
    Path Folderpath;

    public VarVisitor(String variableName) {
        Folderpath = Paths.get(variableName);
        this.variableName = variableName;
        NE.add("blank");
        VA.add("blank");
        AE.add("blank");
        SS1.add("blank");
        SS2.add("blank");
        MakeFile();
    }

    @Override
// Example: int x = 14
    public void visit(VariableDeclarator tempData, Void arg) {
        int ID_code = 0;
        boolean booleanCheck = false;
        final String[] Stringsale = {""}; // Initialize the Stringsale array
        if (tempData.getNameAsString().equals(variableName)) {
            // Save to list and file
            Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|variable made");
            writeToTxt("Variable made " + Stringsale[0]);
            VA.add(Stringsale[0]);
            booleanCheck = true;
            if (booleanCheck == true) {
                VDint = VDint + 1;
                writeToDot(ID_code);
                super.visit(tempData, arg);
            }
        }
    }

    @Override
//NameExpr example: int x=a+3
    public void visit(NameExpr tempData, Void arg) {
        final int ID_code = 1;
        boolean booleanCheck = false;
        boolean loopcheck = false;
        final String[] Stringsale = {""};
        if (tempData.getNameAsString().equals(variableName)) {
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|" +
                        parent.toString());
            });
            loopcheck = SScheck(Stringsale[0]);
            if (loopcheck == false) {
                NE.add(Stringsale[0]);
                booleanCheck = true;//needed or causes array out of bounds
                if (booleanCheck == true) {
                    NEint = NEint + 1;
                    writeToDot(ID_code);
                    writeToTxt(Stringsale[0]);

                }
            }
        }
        super.visit(tempData, arg);
    }

    @Override
    //AssignExprr example a=5
    public void visit(AssignExpr tempData, Void arg) {
        final int ID_code = 2;
        boolean booleanCheck = false;
        final String[] Stringsale = {""};
        // Expression temp=tempData.getTarget();
        if (tempData.getTarget().isNameExpr() && ((NameExpr) tempData.getTarget()).getNameAsString().equals(variableName)) {
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                //  System.out.println(parent);
                Stringsale[0] = tempData.getTarget().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|" +
                        parent.toString();
            });
            AE.add(Stringsale[0]);
            booleanCheck = true;
            if (booleanCheck == true) {
                AEint = AEint + 1;
                writeToDot(ID_code);
                writeToTxt(Stringsale[0]);

            }
        }


        super.visit(tempData, arg);
    }

    public void writeToDot(int ID_code) {
        try {
            if (ID_code == 0) {
                String item = VA.get(1);//should be one but can change late
                String NodeTemp = "VAnode" + 1;
                allNode.add(NodeTemp);
                writeFormatDot(item, NodeTemp);
            } else if (ID_code == 1) {
                String item = NE.get(NEint);
                String NodeTemp = "NEnode" + NEint;
                allNode.add(NodeTemp);
                writeFormatDot(item, NodeTemp);
            } else if (ID_code == 2) {
                String item = AE.get(AEint);//should be one but can change late
                String NodeTemp = "AEnode" + AEint;
                allNode.add(NodeTemp);//add to the set
                writeFormatDot(item, NodeTemp);
            } else if (ID_code == 5) {
                String item = SS1.get(SS1int);
                String NodeTemp = "S1node" + SS1int;
                allNode.add(NodeTemp);
                writeFormatDot(item, NodeTemp);
            } else if (ID_code == 6) {
                //add the sinks and soucre nodes to this
                String item = SS2.get(SS2int);
                String NodeTemp = "S2node" + SS2int;
                allNode.add(NodeTemp);
                writeFormatDot(item, NodeTemp);
            } else if (ID_code == 101) {
                //end the file and file is made
                if ( CheckFiles()==true) {
                    connectFour();
                    FileWriter writer = new FileWriter(pathDot.toFile(), true);
                    writer.write("}\n");
                    writer.close();
                    killdup(pathDot);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //this method should one be call once per project
    public void writeToTxt(String temp) {
        try (FileWriter writer = new FileWriter(pathTxt.toFile(), true)) {
            writer.write(temp + "\n");
            writer.write("---------------" + "new line found" + Lineint + "---------------" + "\n");
            Lineint++;
        } catch (IOException e) {
            throw new RuntimeException("Error writing to TXT file: " + e.getMessage());
        }
    }

    public void writeFormatDot(String item, String NodeTemp) {
        String[] parts = item.split("\\|");
        String line = parts[1];
        try (FileWriter writer = new FileWriter(pathDot.toFile(), true)) {
            writer.write(NodeTemp + " [ label=\"" + line + "\" ];\n");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to DOT file: " + e.getMessage());
        }
    }




    public void MakeFile() {
        try {
            //making folder
            Files.createDirectories(Folderpath);

            //making dot
            File file = new File(Folderpath.toString(), "project.dot");
            Path path1 = Paths.get(Folderpath.toString(), "project.dot");
            FileWriter writer = new FileWriter(file);
            pathDot = path1.toAbsolutePath();
            writer.write("digraph G {\n");
            writer.write("node [ oval=\"rectangle\"\n");
            writer.write("       color=\"deepskyblue4\"\n");
            writer.write("       style=\"filled\"]\n");
            writer.close();
            //making txt
            File file2 = new File(Folderpath.toString(), "project.txt");
            Path path2 = Paths.get(Folderpath.toString(), "project.txt");
            writer = new FileWriter(file2);//this wipe the old file
            writer.write("txt file for the project" + "\n");
            pathTxt = path2.toAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Make file method ,error");
        }
    }

    public void killdup(Path temp) {
        /*delete all dups in the dot file, need to fix it so we can use in txt*/
        try {
            List<String> lines = Files.readAllLines(temp);
            Set<String> uniqueLines = new LinkedHashSet<>(lines);
            Files.write(temp, uniqueLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean SScheck(String Stringsale) {
        for (int i = 0; i < source.length; i++) {
            if (Stringsale.contains(source[i])) {
                SS1.add(Stringsale);
                SS1int++;
                writeToDot(5);
                writeToTxt(Stringsale);
                return true;
            }
        }

        for (int i = 0; i < sinks.length; i++) {
            if (Stringsale.contains(sinks[i])) {
                SS2.add(Stringsale);
                SS2int++;
                writeToDot(6);
                writeToTxt(Stringsale);
                return true;
            }
        }
        return false;
    }

    public void connectFour() {
        if (allNode.size() < 2) {
            System.out.println("Not enough nodes to connect.");
            return;
        }
        try (FileWriter writer = new FileWriter(pathDot.toFile(), true)) {
            for (int i = 0; i < allNode.size() - 1; i++) {
                writer.write(allNode.get(i) + " -> " + allNode.get(i + 1) + ";\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing connections to DOT file: " + e.getMessage());
        }
    }
//still working on. 11/18/2024

    public Path getTxtPath() {
        return pathTxt.toAbsolutePath();
    }

    public Path getPathDot() {
        return pathDot.toAbsolutePath();
    }
    public boolean CheckFiles() {
        if (allNode.isEmpty()) {
            try {
                // Delete files if they exist
                deleteFile(pathDot);
                deleteFile(pathTxt);

                // Delete the folder if it’s empty
                if (Folderpath.toFile().list().length == 0) {
                    boolean deleted = Folderpath.toFile().delete();
                    if (deleted) {
                        System.out.println("Folder deleted successfully.");
                    } else {
                        System.out.println("Failed to delete folder: " + Folderpath.toAbsolutePath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
        return true;
    }

    public void logFileStatus() {
        File txtFile = pathTxt.toFile();
        File dotFile = pathDot.toFile();

        System.out.println("TXT File exists: " + txtFile.exists());
        System.out.println("DOT File exists: " + dotFile.exists());

        if (txtFile.exists()) {
            System.out.println("TXT File writable: " + txtFile.canWrite());
        }
        if (dotFile.exists()) {
            System.out.println("DOT File writable: " + dotFile.canWrite());
        }
    }


    private void deleteFile(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println(file.getAbsolutePath() + " deleted successfully.");
            } else {
                System.out.println("Failed to delete: " + file.getAbsolutePath());
            }
        } else {
            System.out.println("File not found: " + file.getAbsolutePath());
        }
    }
}
