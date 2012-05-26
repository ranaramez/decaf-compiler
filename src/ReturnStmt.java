import java.io.IOException;

public class ReturnStmt extends Statement {
	Expression e;
	int lineNum = -1;
	int charPos;

	public ReturnStmt(Expression e) {
		this.e = e;
	}

	public String toString() {

		String ret = "Return\n";

		String s = e.toString();

		for (String st : s.split("\n"))
			ret += "| " + st + "\n";

		return ret;
	}

	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		String return_type = e.checkType();
		if(!a.get("return_type").type.equals(return_type) 
				&& !(a.get("return_type").type.equals("int") && return_type.equals("float"))
				&& !(a.get("return_type").type.equals("float") && return_type.equals("int")))
		{
			String line=Parser.getLine(lineNum);

			throw new SemanticException("\nSymantic error at line "+lineNum+" char "+charPos+
					"\nReturn type doesn't match with method return type"+"\n Current Return type is  "+a.get("return_type").type+" and the method Return type is "+return_type+"\n In: "+line );
			//throw new SemanticException("Return type doesn't match with method return type");
		}
				
		a.add(new Entry("return", return_type));
	}

}
