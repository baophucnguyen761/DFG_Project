
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//for collecting nodes and other stuff of the java file
public class VarVisitor extends  VoidVisitorAdapter<Void> {
    private final String variableName;
    List<String> NE = new ArrayList<>();
    List<String> VA = new ArrayList<>();
    List<String> AE = new ArrayList<>();
    // List<String> NE=new ArrayList<>();
    int NEint = 0, VAint = 0, AEint = 0;
    Path path;

    public VarVisitor(String variableName) {
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
            Stringsale[0] = (tempData.getName().toString() + "|" + tempData.getRange().map(r -> r.begin.line).orElse(-1)+"| variable made");

            VA.add( Stringsale[0]);
            booleanCheck=true;

            System.out.println("-----");
            if(booleanCheck==true){
                VAint=VAint+1;
            }

        }

        super.visit(tempData, arg);
    }

    @Override
//NameExpr example: int x=a+3
    public void visit(NameExpr tempData,Void arg) {
        final int ID=1;
        boolean booleanCheck=false;
        final String[] Stringsale={""};
        if (tempData.getNameAsString().equals(variableName)) {
            System.out.println("variable used: " + tempData.getName());
            System.out.println("Line number: " + tempData.getRange().map(r -> r.begin.line).orElse(-1));
            tempData.findAncestor(Statement.class).ifPresent(parent -> {
                System.out.println("Full statement: " + parent.toString());
                Stringsale[0]=(tempData.getName().toString()+"|"+tempData.getRange().map(r -> r.begin.line).orElse(-1)+"|"+
                        parent.toString());
            });
            NE.add(Stringsale[0]);
            booleanCheck=true;//needed or causes array out of bounds
            System.out.println("-----");
            if(booleanCheck==true){
                NEint=NEint+1;
            }
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
                System.out.println(Stringsale[0]);
            });

            AE.add(Stringsale[0]);
            booleanCheck=true;
            if(booleanCheck==true){
                AEint=AEint+1;
            }
            System.out.println("-----");
        }
        super.visit(tempData,arg);
    }


    public void writeToFile(int ID_code,int count,String statement){//count what node for naming? IDK need sleep.
        try {
            //NameExpr example: int x=a+3  for ID_code 0
            if(ID_code==0) {
                //node NameExpr with
                FileWriter writer = new FileWriter(path.toFile());
            } else if (ID_code==1) {

            } else if (ID_code==2) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    //this method should one be call once per project
    public void MakeFile(){
        try {
            File file=new File("project.dot");//makes the file
            Path path1= Paths.get("project.dot");//get the path to the file
            FileWriter writer=new FileWriter(file);//write to the file made
            writer.write("digraph G {\n");//write content for a dot file
            writer.close();
            path=path1.toAbsolutePath();//set the path to the class path
            //may need to add same to main
        }catch (IOException e){
            throw new RuntimeException("Make file method");
        }
    }

}