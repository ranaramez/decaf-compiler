import java.io.IOException;


public class LocalVarDecl extends Statement {
	public String type;
	public String id;
	public int lineNum;
	public int charPos;
	public LocalVarDecl(){	
	}
	
	public LocalVarDecl(String type, String id){
		this.type = type;
		this.id = id;
	}
	
	public String toString(){
		String ret = "LocalVarDecl "+ id + "\n";
		
		return ret;
		
	}
	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		if(a.contains(id))
		{
			String line=Parser.getLine(lineNum);
			throw new SemanticException("\nSymantic error at line "+lineNum+" char "+charPos +
					"\n LocalVariable  name is not unique "+this.id+" was declared before \n In: "+line );
			
			//throw new SemanticException("LocalVariable name is not unique");
		}
			
		a.add(new Entry(id, type));
	}
}
