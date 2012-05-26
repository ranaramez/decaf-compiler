import java.io.IOException;
import java.util.ArrayList;

/*
 * This class should be the root of your parse tree.
 * 
 * Update the class with the variables and constructors
 * you see required.
 * 
 * Update the toString method to print the tree.
 * 
 * Update the check method such that it checks the
 * tree for semantic errors.
 */

public class ClassDecl {
	public String id;
	ArrayList<MethodDecl> methods;
	
	public ClassDecl() {
	}
	
	public ClassDecl(String id) {
		this.id = id;
	}
	public ClassDecl(String id, ArrayList<MethodDecl> methods){
		this.id = id;
		this.methods = methods;
	}
	
	public void AddToMethods(MethodDecl method){
		this.methods.add(method);
	}
	
	public String toString() {
		
		String ret = "ClassDecl " + id + "\n";
		
		String s = "";
		for(int i=0; i< this.methods.size(); i++ ){
			s += this.methods.get(i).toString();
		}
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		a.openScope();
		a.add(new Entry(id));
		if(methods != null){
			for(int i=0; i<methods.size(); i++)
				methods.get(i).checkMethodNames();
			for(int i=0; i<methods.size(); i++)
				methods.get(i).check();
		}
	}
}
