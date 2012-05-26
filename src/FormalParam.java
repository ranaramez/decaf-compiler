import java.io.IOException;


public class FormalParam extends ProperFormalParams {
	public String type;
	public String id;
	public int lineNum=-1;
	public FormalParam(){	
	}
	
	public FormalParam(String type, String id){
		this.type = type;
		this.id = id;
	}
	
	public String toString(){
		String ret = "Formal param " + id + "\n";
		return ret;
		
	}
	public Entry check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		if(a.contains(id))
		{
			String line=Parser.getLine(lineNum);
			throw new SemanticException("\nSymantic error at line "+lineNum+" char "+line.indexOf(this.id) +
					"\n Param name is not unique "+this.id+" was declared before \n In: "+line );
			//throw new SemanticException("Param name is not unique");
		}
			
		Entry result = new Entry (id,type);
		return result;
	}
}
