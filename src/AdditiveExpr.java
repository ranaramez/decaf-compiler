import java.io.IOException;


public class AdditiveExpr extends EqualityExpr {
	public MultiplicativeExpr m_expr;
	public int op;
	public AdditiveExpr a_expr;
	int lineNum;
	int charPos;
	public static final int PO = 1;
	public static final int MO = 2;
	
	public AdditiveExpr() {
		
	}
	
	public AdditiveExpr(MultiplicativeExpr m_expr, int op, AdditiveExpr a_expr) {
		this.m_expr = m_expr;
		this.op = op;
		this.a_expr = a_expr;
	}
	
	public String toString(){
		String ret = "AdditiveExpr\n";
		
		String s = "";
		
		if(a_expr !=null) {
			s += a_expr.toString();
			if(op == PO)
				s += "+\n";
			else
				s += "-\n";
		}
		
		s += m_expr.toString();
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
	
		
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
		String type = m_expr.checkType();
		if(a_expr != null){
			String expr_type = a_expr.checkType();
			if(!type.equals(expr_type) || (!type.equals("int") && !type.equals("float")))
			{
				String line=Parser.getLine(this.lineNum);
				throw new SemanticException("\nSymantic error at line "+this.lineNum+" char "+this.charPos +
						"\n Expressions type doesn't match for conditionalExpression, "+" Expression type is "+expr_type+" and conditionalExpression is "+type+"\n In: "+line );
				//throw new SemanticException("Expressions type doesn't match for conditionalExpression");
			}
				
			else
				type = expr_type;
		}
		return type;
	}
	
}
