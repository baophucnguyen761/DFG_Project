
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//for collecting nodes and other stuff of the java file
public class    VarVisitor extends  VoidVisitorAdapter<Void> {
    private final String variableName;
    List<String> NE = new ArrayList<>(); Set<String> NEnodes=new HashSet<>();
    List<String> VA = new ArrayList<>();Set<String> VAnodes=new HashSet<>();
    List<String> AE = new ArrayList<>();Set<String> AEnodes=new HashSet<>();
    // List<String> NE=new ArrayList<>();
    int NEint = 0, VDint = 0, AEint = 0;
    Path path;//dot file

    public VarVisitor(String variableName) {
        MakeFile();
        this.variableName = variableName;
        NE.add("blank");
        VA.add("blank");  //(need to fix)do this in main and check if empty add a blank data
        AE.add("blank");
    }
    @Override
// Example: int x = 14
    public void visit(VariableDeclarator tempData, Void arg) {
        int ID_code=0;boolean booleanCheck=false;
        final String[] Stringsale = {""}; // Initialize the Stringsale array

        if (tempData.getNameAsString().equals(variableName)) {
            System.out.println("variable made: " + tempData.getName());
            System.out.println("Line number: " + tempData.getRange().map(r -> r.begin.line).orElse(-1));
            // Save to list and file
            Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1)+"|variable made");

            VA.add(Stringsale[0]);
            booleanCheck=true;

            System.out.println("-----");
            if(booleanCheck==true){
                VDint = VDint +1;
               writeToFile(ID_code);
            }

        }

        super.visit(tempData, arg);
    }

    @Override
//NameExpr example: int x=a+3
    public void visit(NameExpr tempData,Void arg) {
        final int ID_code=1;
        boolean booleanCheck=false;
        final String[] Stringsale={""};
        if (tempData.getNameAsString().equals(variableName)) {
            System.out.println("variable used: " + tempData.getName());
            System.out.println("Line number: " + tempData.getRange().map(r -> r.begin.line).orElse(-1));
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                System.out.println("Full statement: " + parent.toString());
                Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1) + "|" +
                        parent.toString());
            });

                NE.add(Stringsale[0]);
            booleanCheck = true;//needed or causes array out of bounds

            if (booleanCheck == true) {
                NEint = NEint + 1;
                writeToFile(ID_code);

            }

            System.out.println("-----");
        }
        super.visit(tempData,arg);
    }

    @Override
    //AssignExprr example a=5
    public void visit(AssignExpr tempData, Void arg) {
        final int ID=2;
        boolean booleanCheck=false;
        final  String [] Stringsale={""};
        Expression temp=tempData.getTarget();
        if (tempData.getTarget().isNameExpr() && ((NameExpr) tempData.getTarget()).getNameAsString().equals(variableName)) {
            System.out.println("variable assigned: " + temp);
            System.out.println("Line number: " + tempData.getRange().map(r -> r.begin.line).orElse(-1));
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                System.out.println("Full statement: " + parent.toString());
                Stringsale[0]= tempData.getTarget().toString()+"|"+tempData.getRange().map(r -> r.begin.line).orElse(-1)+"|"+
                        parent.toString();
            });


                AE.add(Stringsale[0]);
                booleanCheck = true;
                if (booleanCheck == true) {
                    AEint = AEint + 1;

                }

            System.out.println("-----");
        }
        super.visit(tempData,arg);
    }


    public void writeToFile(int ID_code){//add a count.for what maybe naming? IDK need sleep.
        //statement is the full line of code
        try {

            if(ID_code==0) {
                // Example: int x = 14

                String item=VA.get(1);//should be one but can change late
                //System.out.println(item+"888888");
                String[] parts=item.split("\\|");
                String line=parts[1],code=parts[2];
                FileWriter writer = new FileWriter(path.toFile(),true);
                writer.write("VariableDeclare [ label=\"" + line + "," + code + "\" ];\n");
                writer.close();//need to call either close or flush after writing to file

            } else if (ID_code==1) {
               int i=1;
                while (i<=NEint) {

                    String item = NE.get(i);//should be one but can change late
                    String NodeTemp = "NEnode" + i;
                    NEnodes.add(NodeTemp);//add to the set
                    String[] parts = item.split("\\|");
                    String line = parts[1], code = parts[2];
                    FileWriter writer = new FileWriter(path.toFile(), true);
                    writer.write(NodeTemp + " [ label=\"" + line + "," + code + "\" ];\n");
                    writer.close();//need to call either close or flush after writing to file
                    i++;

            }

            } else if (ID_code==2) {

            } else if (ID_code==100) {
                //going to connect the nodes
                FileWriter writer=new FileWriter(path.toFile(),true);

            } else if (ID_code==101) {
                //end the file and file is made
                FileWriter writer = new FileWriter(path.toFile(),true);
                writer.write("}\n");
                writer.close();//need to call either close or flush after writing to file
                killdup();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    //this method should one be call once per project
    //must be call in method
    public void MakeFile(){
        try {
            File file=new File("project.txt");//makes the file//using a txt as a test
            Path path1= Paths.get("project.txt");//get the path to the file
            FileWriter writer=new FileWriter(file);//write to the file made
            writer.write("digraph G {\n");//write content for a dot file
            writer.write("node [ shape=\"rectangle\"\n");
            writer.write("       color=\"green\"\n");
            writer.write("       style=\"filled\"]\n");
            writer.close();
            path=path1.toAbsolutePath();//set the path to the class path
            //may need to add same to main
        }catch (IOException e){
            throw new RuntimeException("Make file method ,error");
        }
    }
    public void killdup(){
        /*use to go back into file to kill all dups. IDk why mutiple dups was being write made if statment in differnt part
        * of the code to kill dups before they made it to the file, but didn't work think WriteToFile was being call mutiple times
        * in the same code block */
        try{
            List<String> lines = Files.readAllLines(path);
            Set<String> uniqueLines = new LinkedHashSet<>(lines);
            Files.write(path, uniqueLines);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}