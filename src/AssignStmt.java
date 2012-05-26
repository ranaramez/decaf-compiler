import java.io.IOException;


public class AssignStmt extends Statement {
	public String id;
	public Expression e;
	public int lineNum;
	public int charPos;
	public AssignStmt ()
	{
		
	}
	public AssignStmt (String id, Expression e)
	{
		this.id=id;
		this.e=e;
		
	}
	public String toString() {
		
		String ret = "AssignStmt " + id + "\n";
		
		String s = "";
		s+=e.toString();
		for(String st: s.split("\n"))
			ret += " | " + st + "\n";
		
		return ret;
	}
	
	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		if(a.contains(id)){
			String assign_type = a.get(id).getType();
			String expr_type = e.checkType();
			if(!assign_type.equals(expr_type)){
				if(!(assign_type.equals("int") && expr_type.equals("float")) 
						&& !(assign_type.equals("float") && expr_type.equals("int")))
				{
					String line=Parser.getLine(lineNum);
					throw new SemanticException("\nSymantic error at line "+lineNum+" char "+charPos +
							"\n Assign statement types doesn't match, "+this.id+" doesn't match the expression\n In: "+line );
					//throw new SemanticException("Assign statement types doesn't match");
				}
					
			}
		}
		else
			throw new SemanticException("Variable is not declared");
	}


}
