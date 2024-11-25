package com.example.swe_gui;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class VarVisitor extends VoidVisitorAdapter<Void>{
    private final String variableName;
    List<String> NE = new ArrayList<>();
    List<String> NEnodes=new ArrayList<>();//set //forgot that you cannot access set by element.
    List<String> VA = new ArrayList<>();
    List<String> VAnodes=new ArrayList<>();//set
    List<String> AE = new ArrayList<>();
    List<String> AEnodes=new ArrayList<>();//set
    List<String> SS1=new ArrayList<>();List<String> SS2=new ArrayList<>();
    List<String> SS1nodes=new ArrayList<>();;List<String> SS2nodes=new ArrayList<>();
    String [] source={".nextLine()",".nextInt()","nextDouble()",
            "nextBoolean()","nextFloat()","System.in","BufferedReader.readLine","FileInputStream","BufferReader","" +
            "File.ReadAllLines"};//,"this"
    String [] sinks={"System.out.print","return","FileOutputStream","BufferedWriter","File.write"};

    int NEint = 0, VDint = 0, AEint = 0,SS1int=0,SS2int=0,Lineint=0;
    Path pathDot;//dot file
    Path pathTxt;//txt file
    Path Folderpath;

    public VarVisitor(String variableName) {
        Folderpath=Paths.get(variableName);
        this.variableName = variableName;
        MakeFile();
        NE.add("blank");
        VA.add("blank");  //(need to fix)do this in main and check if empty add a blank data
        AE.add("blank");
        SS1.add("blank");
        SS2.add("blank");
        MakeFile();
    }
    @Override
// Example: int x = 14
    public void visit(VariableDeclarator tempData, Void arg) {
        int ID_code=0;boolean booleanCheck=false;
        final String[] Stringsale = {""}; // Initialize the Stringsale array
        if (tempData.getNameAsString().equals(variableName)) {
            // Save to list and file
            Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|variable made");
                writeToTxt("Variable made "+Stringsale[0]);
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
        final int ID_code=1;
        boolean booleanCheck=false;
        boolean loopcheck=false;
        final String[] Stringsale={""};
        if (tempData.getNameAsString().equals(variableName)) {
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|" +
                        parent.toString());
            });
            loopcheck=SScheck(Stringsale[0]);
            if (loopcheck==false) {
                NE.add(Stringsale[0]);
                booleanCheck = true;//needed or causes array out of bounds
                if (booleanCheck == true) {
                    NEint = NEint + 1;
                    writeToDot(ID_code);
                    writeToTxt(Stringsale[0]);

                }
            }
        }
        super.visit(tempData,arg);
    }
    @Override
    //AssignExprr example a=5
    public void visit(AssignExpr tempData, Void arg) {
        final int ID_code=2;
        boolean booleanCheck=false;
        final  String [] Stringsale={""};
        Expression temp=tempData.getTarget();
        if (tempData.getTarget().isNameExpr() && ((NameExpr) tempData.getTarget()).getNameAsString().equals(variableName)) {
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                Stringsale[0]= tempData.getTarget().toString()+"|"+tempData.getRange().map(r -> r.begin.line).orElse(-1)+"|"+
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


        super.visit(tempData,arg);
    }

    public void writeToDot(int ID_code) {
        try {
            if (ID_code == 0) {
                String item = VA.get(1);//should be one but can change late
                String NodeTemp = "VAnode" + 1;
                VAnodes.add(NodeTemp);
                writeFormatDot(item,NodeTemp);
            } else if (ID_code == 1) {
                    String item = NE.get(NEint);
                    String NodeTemp = "NEnode" + NEint;
                    NEnodes.add(NodeTemp);
                    writeFormatDot(item,NodeTemp);
            } else if (ID_code == 2) {
                    String item = AE.get(AEint);//should be one but can change late
                    String NodeTemp = "AEnode" + AEint;
                    AEnodes.add(NodeTemp);//add to the set
                    writeFormatDot(item,NodeTemp);
            } else if (ID_code == 5) {
                    String item = SS1.get(SS1int);
                    String NodeTemp = "S1node" + SS1int;
                    SS1nodes.add(NodeTemp);
                    writeFormatDot(item,NodeTemp);
            } else if (ID_code == 6) {
                //add the sinks and soucre nodes to this
                    String item = SS2.get(SS2int);
                    String NodeTemp = "S2node" + SS2int;
                    SS2nodes.add(NodeTemp);
                    writeFormatDot(item,NodeTemp);
            } else if (ID_code == 101) {
                //end the file and file is made
                connectFour();
                FileWriter writer = new FileWriter(pathDot.toFile(), true);
                writer.write("}\n");
                writer.close();
                killdup(pathDot);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //this method should one be call once per project
    //must be call in method
    public void writeToTxt(String temp) {
        try {
            FileWriter writer = new FileWriter(pathTxt.toFile(), true);
            writer.write(temp+"\n");
            writer.write("---------------"+"new line found"+Lineint+"---------------"+"\n");
            Lineint++;
           // killdup(pathTxt); // need to fix either kill the dup or stop them form get to the file
            writer.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void writeFormatDot(String item,String NodeTemp) throws IOException {
        String [] parts=item.split("\\|");
        String line=parts[1];
        FileWriter writer = new FileWriter(pathDot.toFile(), true);
        writer.write(NodeTemp + " [ label=\"" + line + "," +  "\" ];\n");
        writer.close();
    }
    public void MakeFile(){
        try {
            //making folder
                Files.createDirectories(Folderpath);
            //making dot
            File file=new File(Folderpath.toString(),"project.dot");
            Path path1= Paths.get(Folderpath.toString(),"project.dot");
            FileWriter writer=new FileWriter(file);
            pathDot =path1.toAbsolutePath();
            writer.write("digraph G {\n");
            writer.write("node [ oval=\"rectangle\"\n");
            writer.write("       color=\"deepskyblue4\"\n");
            writer.write("       style=\"filled\"]\n");
            writer.close();
            //making txt
            File file2 =new File(Folderpath.toString(),"project_txt.txt");
            Path path2=Paths.get(Folderpath.toString(),"project_txt.txt");
            writer=new FileWriter(file2);//this wipe the old file
            writer.write("txt file for the project"+"\n");
            pathTxt=path2.toAbsolutePath();
        }catch (IOException e){
            throw new RuntimeException("Make file method ,error");
        }
    }
    public void killdup(Path temp){
        /*delete all dups in the dot file, need to fix it so we can use in txt*/
        try{
            List<String> lines = Files.readAllLines(temp);
            Set<String> uniqueLines = new LinkedHashSet<>(lines);
            Files.write(temp, uniqueLines);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public boolean SScheck(String Stringsale){
        for(int i=0;i<source.length;i++) {
            if (Stringsale.contains(source[i])) {
                SS1.add(Stringsale);
                SS1int++;
                writeToDot(5);
                writeToTxt(Stringsale);
                return true;
            }
        }

        for (int i=0; i<sinks.length;i++){
            if (Stringsale.contains(sinks[i])){
                SS2.add(Stringsale);
                SS2int++;
                writeToDot(6);
                writeToTxt(Stringsale);
                return true;
            }
        }
        return false;
    }
    public void connectFour() throws IOException {
        FileWriter writer = new FileWriter(pathDot.toFile(), true);
        if(!(SS1nodes.isEmpty() && VAnodes.isEmpty())){
            writer.write(SS1nodes.get(0)+"->"+VAnodes.get(0)+"\n");
            for (int i =0;i<SS2nodes.size();i++){
                writer.write(VAnodes.get(0)+"->"+SS2nodes.get(i)+"\n");
                //just do a line of the nodes as they are found
            }

        }
        writer.close();///make sure this is move to both of method when
    }//still working on. 11/18/2024
    public Path getTxtPath(){
        return pathTxt.toAbsolutePath();
    }
    public Path getPathDot(){
        return pathDot.toAbsolutePath();
    }
}