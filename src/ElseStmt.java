import java.io.IOException;


public class ElseStmt extends IfStmt {
	Statement stmt;
	
	public ElseStmt()
	{
		
	}
	public ElseStmt(Statement stmt)
	{
		this.stmt=stmt;
	}
	public String toString() {
		
		String ret = "else \n";
		
		String s =stmt.toString();
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public void check(Entry ent, int scope) throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		a.openScope();
		stmt.check();
		Entry else_entry = null;
		if(a.contains("return") && a.contains("if_return_"+ scope)){
			else_entry = a.get("return");
			else_entry.setId("else_return_" + (scope -1));
		}
		a.closeScope();
		if(else_entry != null)
			a.add(else_entry);
		if(scope == 1){
			this.resetScope();
		}
	}

}
