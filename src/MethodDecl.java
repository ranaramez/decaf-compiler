import java.io.IOException;



public class MethodDecl {
	
	public String id;
	public String type;
	public ProperFormalParams params;
	public Block block;
	public int lineNum=-1;
	
	public Entry [] returned_params;
	public int charPos;
	
	public MethodDecl() {
	}
	
	public MethodDecl(String type, String id){
		this.id = id;
		this.type = type;
	}
	
	public MethodDecl(String type, String id, ProperFormalParams params){
		this.id = id;
		this.type = type;
		this.params = params;
	}
	
	public MethodDecl(String type, String id, ProperFormalParams params,Block block){
		this.id = id;
		this.type = type;
		this.params = params;
		this.block = block;
	}
	
	public void addBlock(Block block){
		this.block = block;
	}
	
	public String toString() {
		String ret = "MethodDecl " + id + "\n";
		String s = "";
		
		if(this.params != null){
			s += this.params.toString();
		}
		
		if(block != null){
			s += this.block.toString();
		}
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		return ret;
	}
	
	public void checkMethodNames() throws SemanticException, IOException{
		SymbolTable a = SymbolTable.getInstance();
		returned_params = null;
		if(a.contains(id))
		{
			String line=Parser.getLine(lineNum);
			charPos=line.indexOf(this.id);
			System.out.println(line);
			System.out.println("+++++"+line.indexOf(this.id));
			throw new SemanticException("\nSymantic error at line "+lineNum+" char "+(charPos+1)+"\n Method Name not unique " +
					"\n Method name "+this.id+" was declared before \n In: "+line );
			//throw new SemanticException("Method name is not unique");
			
		}
			
		if(params != null){
			returned_params = params.check(id);
			a.add(new Entry(id, type, returned_params));
		}
		else
			a.add(new Entry(id, type));	
	}
	
	public void check() throws SemanticException, IOException {
		SymbolTable a = SymbolTable.getInstance();
		a.openScope();
		a.add(new Entry("return_type", type));
		if(returned_params != null){
		for(int i=0; i<returned_params.length; i++)
			a.add(returned_params[i]);
		}
		block.check();
		if(!a.contains("return")){
			if((a.contains("if_return") && !a.contains("else_return")) 
				|| (!a.contains("if_return") && a.contains("else_return"))
				|| (!a.contains("if_return") && !a.contains("else_return"))){
				throw new SemanticException("Missing reachable return statement");
			}
		}
		a.closeScope();
		
	}
}
