import java.io.IOException;


public class PrimaryExpr extends MultiplicativeExpr {
	
	public String terminal;
	public int terminal_type;
	public CallExpr ce;
	public Expression expr;
	int lineNum;
	int charPos;
	public static final int NM = 1;
	public static final int BL = 2;
	public static final int ST = 3;
	public static final int ID = 4;
	
	public PrimaryExpr(){
	}
	
	public PrimaryExpr(String terminal, int terminal_type, CallExpr ce, Expression expr){
		this.terminal = terminal;
		this.terminal_type = terminal_type;
		this.ce = ce;
		this.expr = expr;
	}
	
	public String toString(){
		String ret = "";
		
		String s = "";
		if(terminal_type == NM || terminal_type == BL || terminal_type == ST || terminal_type == ID) 
			s = s + terminal + "\n";
		else if(ce != null)
			s += ce.toString();
		else if(expr != null)
			s += expr.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
		
		String type = "";
		SymbolTable a = SymbolTable.getInstance();
		if(terminal_type == NM){
			if(terminal.contains("."))
				type = "float";
			else
				type = "int";
		}else if(terminal_type == BL)
			type ="boolean";
		else if(terminal_type == ST)
			type = "String";
		else if(terminal_type == ID){
			if(!a.contains(terminal))
			{
				String line=Parser.getLine(this.lineNum);
				throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+(line.indexOf(terminal)+1)+
						"\n variable " + terminal + " is not defined"+"\n In: "+line );
				//throw new SemanticException("variable " + terminal + " is not defined");
			}
				
			type = a.get(terminal).getType();
		}
		else if(expr != null)
			try {
				type = expr.checkType();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(ce != null)
			try {
				type = ce.checkType();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			throw new SemanticException("Can't find a type for expression in Primary expression");
		return type;
	}
}
