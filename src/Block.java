import java.io.IOException;
import java.util.ArrayList;


public class Block extends Statement {
	
	public ArrayList<Statement> statements;
	
	public Block(){	
	}
	
	public Block(ArrayList<Statement> statements){
		this.statements = statements;
	}
	
	public String toString(){
		String ret = "Block\n";
		
		String s = "";
		if(this.statements != null){
//			s += "Statements\n";
			for(int i=0; i<this.statements.size(); i++)
				s += this.statements.get(i).toString();
		}
		
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public void check() throws SemanticException, IOException {
		for(int i=0; i<statements.size(); i++){
			statements.get(i).check();
		}	
	}
}
