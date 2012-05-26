import java.io.IOException;


public class EqualityExpr extends ConditionalAndExpr {
	public AdditiveExpr a_expr;
	public int op;
	public EqualityExpr e_expr;
	int lineNum;
	int charPos; 
	
	public static final int EQ = 1;
	public static final int NE = 2;
	
	public EqualityExpr(){
	}
	
	public EqualityExpr(AdditiveExpr a_expr, int op, EqualityExpr e_expr){
		this.a_expr = a_expr;
		this.op = op;
		this.e_expr = e_expr;
	}
	
	public String toString(){
		String ret = "EqualityExpr\n";
		
		String s = "";
		
		if(e_expr != null){
			s += e_expr.toString();
			if(op == EQ)
				s += "==\n";
			if(op == NE)
				s += "!=\n";
		}
		s = s + a_expr.toString() + "\n";
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;

	}
	
	public String checkType() throws SemanticException, IOException{
		String type = a_expr.checkType();
		if(e_expr != null){
			String expr_type = e_expr.checkType();
			if(!type.equals(expr_type))
			{
				String line=Parser.getLine(this.lineNum);
				throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos +
						"\n Expressions type doesn't match for EqualityExpression, "+" Expression type is "+expr_type+" and EqualityExpression is "+type+ "\n In: "+line );
				//throw new SemanticException("Expressions type doesn't match for EqualityExpression");
			}
				
			else
				type = "boolean";
		}
		return type;
	}
}
