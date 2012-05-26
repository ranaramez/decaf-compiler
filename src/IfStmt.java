import java.io.IOException;


public class IfStmt extends Statement {
	Expression e;
	Statement stmts;
	ElseStmt eStmts;
	public int lineNum;
	public int charPos;
	static int scope_level = 0;
	public IfStmt() {

	}

	public IfStmt(Expression e,Statement stmts) {
		this.e=e;
		this.stmts = stmts;
		
	}
	public IfStmt(Expression e,Statement stmts,ElseStmt eStmts) {
		this.e=e;
		this.stmts = stmts;
		this.eStmts = eStmts;
		
	}


	public String toString() {

		String ret = "IfStmt\n";
		String s ="";
		
		s += e.toString()+"\n"+ "then "+stmts.toString();
		if(eStmts!=null)
			s+=eStmts.toString();

		for (String st : s.split("\n"))
			ret += "| " + st + "\n";

		return ret;
	}
	
	public void resetScope(){
		scope_level = 0;
	}
	

	public void check() throws SemanticException, IOException {
		scope_level ++;
		SymbolTable a = SymbolTable.getInstance();
		String expr_type = e.checkType();
		if(!expr_type.equals("boolean"))
		{
			String line=Parser.getLine(lineNum);
			System.out.println("line num "+this.lineNum);
			throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos+
					"\n Can't evaluate expression type to Boolean in the If statement "+"\n Current Expression is of type "+expr_type+"\n In: "+line );
			//throw new SemanticException("Can't evaluate expression type to Boolean in the If statement");
		}
			
		a.openScope();
		stmts.check();
		Entry entry = null;
		Entry if_entry = null;
		Entry else_entry = null;
		if(a.contains("return"))
			entry = a.get("return");
		a.closeScope();
		a.openScope();
		if(eStmts != null){
			if(entry != null){
				entry.setId("if_return_"+scope_level);
				a.add(entry);
			}
			eStmts.check(entry, scope_level);
			if(a.contains("if_return_0") && a.contains("else_return_0")){
				if_entry = a.get("if_return_0");
				else_entry = a.get("else_return_0");
			}
		}
		a.closeScope();
		if(if_entry != null){
			if_entry.setId("if_return");
			else_entry.setId("else_return");
			a.add(if_entry);
			a.add(else_entry);
		}
	}

}
