import java.io.IOException;


public class MultiplicativeExpr extends AdditiveExpr {
	public PrimaryExpr p_expr;
	public int op;
	public MultiplicativeExpr m_expr;
	int lineNum;
	int charPos;
	public static final int TO = 1;
	public static final int DO = 2;
	public static final int MD = 3;
	
	public MultiplicativeExpr(){
	}
	
	public MultiplicativeExpr(PrimaryExpr p_expr, int op, MultiplicativeExpr m_expr){
		this.p_expr = p_expr;
		this.op = op;
		this.m_expr = m_expr;
	}
	
	public String toString(){
		String ret = "MultiplicativeExpr\n";
		
		String s = "";
		if(m_expr!=null) {
			s += m_expr.toString();
			if(op == TO)
				s += "*\n";
			else if(op == DO)
				s += "/\n";
			else
				s += "%\n";
		}
		
		s += p_expr.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
		String type = p_expr.checkType();
		if(m_expr != null){
			String expr_type = m_expr.checkType();
			if(!type.equals(expr_type) || (!type.equals("int") && !type.equals("float")))
			{
				String line=Parser.getLine(this.lineNum);
				throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos +
						"\n Expressions type doesn't match for MultiplicativeExpression, "+" Expression type is "+expr_type+" and conditionalExpression is "+type+"\n In: "+line );
				//throw new SemanticException("Expressions type doesn't match for MultiplicativeExpression");
			}
				
			else
				type = expr_type;
		}
		return type;
	}
}
