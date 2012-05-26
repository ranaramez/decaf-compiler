import java.io.IOException;


public class WhileStmt extends Statement{
	Expression e;
	Statement stmt;
	public int lineNum;
	public int charPos;
	public WhileStmt(Expression e, Statement stmt) {
		this.e=e;
		this.stmt=stmt;
	}
	public String toString() {
		
		String ret = "While \n";
		String  s = "";
		
		s += e.toString()+"\n"+stmt.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		String expr_type = e.checkType();
		if(!expr_type.equals("boolean"))
		{
			String line=Parser.getLine(lineNum);

			throw new SemanticException("\nSymantic error at line "+lineNum+" char "+charPos+
					"\n Can't evaluate expression type to Boolean in the While statement "+"\n Current Expression is of type "+expr_type+"\n In: "+line );
			//throw new SemanticException("Can't evaluate expression type to Boolean in the While statement");
		}
			
		if(stmt != null){
			a.openScope();
			stmt.check();
			a.closeScope();
		}
			
	}
}
