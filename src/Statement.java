import java.io.IOException;


public class Statement {
	public Block block;
	public LocalVarDecl localVar;
	public AssignStmt assign;
	public IfStmt ifstmt;
	public WhileStmt whilestmt;
	public ReturnStmt returnstmt;
	
	
	public Statement(){
	}
	
	public Statement(Block block, LocalVarDecl localVar, AssignStmt assign, 
			IfStmt ifstmt, WhileStmt whilestmt, ReturnStmt returnstmt){
		this.block = block;
		this.localVar = localVar;
		this.assign = assign;
		this.ifstmt = ifstmt;
		this.whilestmt = whilestmt;
		this.returnstmt = returnstmt;
	}
	
	public String toString(){
		String ret = "Statement\n";
		
		String s = "";
		
		if(this.block != null)
			s += this.block.toString();
		else if(this.localVar != null)
			s += this.localVar.toString();
		else if(this.assign != null)
			s += this.assign.toString();
		else if(this.ifstmt != null)
			s += this.ifstmt.toString();
		else if(this.whilestmt != null)
			s += this.whilestmt.toString();
		else
			s += this.returnstmt.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public void check() throws SemanticException, IOException {
		if(this.block != null)
			this.block.check();
		else if(this.localVar != null)
			this.localVar.check();
		else if(this.assign != null)
			this.assign.check();
		else if(this.ifstmt != null)
			this.ifstmt.check();
		else if(this.whilestmt != null)
			this.whilestmt.check();
		else
			this.returnstmt.check();	
	}
}
