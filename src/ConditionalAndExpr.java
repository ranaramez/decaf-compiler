import java.io.IOException;


public class ConditionalAndExpr extends Expression {
	public EqualityExpr e_expr;
	public int op;
	public ConditionalAndExpr cae;
	int lineNum;
	int charPos;
	
	public static final int LA = 1;
	
	public ConditionalAndExpr(){
	}
	
	public ConditionalAndExpr(EqualityExpr e_expr, int op, ConditionalAndExpr cae){
		this.e_expr = e_expr;
		this.op = op;
		this.cae = cae;
	}
	
	public String toString(){
		String ret = "ConditionalAndExpr\n";
		
		String s = "";
		
		if(cae != null){
			s += cae.toString();
			if(op == LA)
				s += "&&\n";
		}
		s = s + e_expr.toString() + "\n";
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
		String type = e_expr.checkType();
		if(cae != null){
			String expr_type = cae.checkType();
			if(!type.equals("boolean") || !expr_type.equals("boolean"))
			{
				String line=Parser.getLine(this.lineNum);
				throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos +
						"\n Expressions type doesn't match for conditionalExpression, "+" Expression type is "+expr_type+" and conditionalExpression is "+type+"\n In: "+line );
				//throw new SemanticException("Expressions type doesn't match for conditionalExpression");
			}
				
			else
				type = "boolean";
		}
		return type;
	}
	
}
