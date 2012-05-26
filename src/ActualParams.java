import java.io.IOException;
import java.util.ArrayList;


public class ActualParams extends CallExpr {
//	public Expression expr;
//	public ActualParams actualParams;
	public ArrayList<Expression> exprs;
	int lineNum;
	int charPos;
	public ActualParams(){
	}
	
	public ActualParams(ArrayList<Expression> exprs){
		this.exprs = exprs;
		
	}
	
	public ActualParams(ArrayList<Expression> exprs, int ln, int charPos) {
		this.exprs = exprs;
		this.lineNum=ln;
		this.charPos=charPos;
	}

	public String toString(){
		String ret = "Actual param\n";
		String s = "";
		if(exprs != null){
			for(int i=0; i< exprs.size(); i++)
				s += exprs.get(i).toString();
		}
		
		for(String st: s.split("\n"))
			ret +=  st + "\n";
		return ret;
		
	}
	
	public void check(String method_name) throws SemanticException, IOException{
		
		SymbolTable a = SymbolTable.getInstance();
		Entry method = a.get(method_name);
//		if(method.getParams() == null)
		if(method.getParams().length != exprs.size())
		{
			String line=Parser.getLine(this.lineNum);
			throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos+
					"\n can't find a method with this name and params "  + " wrong number of arguments"+"\n In: "+line );
			//throw new SemanticException("can't find a method with this name and params .. wrong number of arguments");
		}
			
		else{
			Entry [] params = method.getParams();
			for(int i=0; i<exprs.size(); i++){
				if(!params[i].getType().equals(exprs.get(i).checkType()))
				{
					String line=Parser.getLine(this.lineNum);
					throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos+
							"\n method argument types doesn't match, expected " +params[i].getType() + " and found "+exprs.get(i).checkType()+"\n In: "+line );
					
					//throw new SemanticException("method argument types doesn't match");
				}
					
			}
		}
		
	}
}
