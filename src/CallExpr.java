import java.io.IOException;


public class CallExpr extends Expression {
	public String id;
	public ActualParams actualParams;
	int lineNum;
	int charPos;
	public CallExpr(){
	}
	
	public CallExpr(String id, ActualParams actualParams){
		this.id = id;
		this.actualParams = actualParams;
	}
	
	public String toString(){
		String ret = "CallExpr " + id + "\n" ;
		String s = actualParams.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
			
		String type = "";
		SymbolTable a = SymbolTable.getInstance();
		if(!a.contains(id))
		{
			String line=Parser.getLine(this.lineNum);
			throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+(line.indexOf(id)+1)+
					"\n variable " + id + " is not defined"+"\n In: "+line );
			//throw new SemanticException("Method call is not defined .. can't find a method with this name");
		}
			
		else{
			actualParams.check(id);
			type = a.get(id).getType();
		}
		return type;
			
	}
}
