import java.io.IOException;


public class Expression {
	public ConditionalAndExpr cae;
	public int op;
	public Expression expr;
	public int lineNum;
	public int charPos;
	
	public static final int LO = 1;
	
	public Expression(){
	}
	
	public Expression(ConditionalAndExpr cae, int op, Expression expr){
		this.cae = cae;
		this.op = op;
		this.expr = expr;
	}
	
	public String toString(){
		String ret = "Expression\n";
		
		String s = "";
		
		if(expr != null){
			s += expr.toString();
			if(op == LO)
				s += "||\n";
		}
		s = s + cae.toString() + "\n";
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		
		return ret;
	}
	
	public String checkType() throws SemanticException, IOException{
		String type = cae.checkType();
		if(expr != null){
			String expr_type = expr.checkType();
			if(!type.equals("boolean") || !expr_type.equals("boolean"))
			{
				String line=Parser.getLine(lineNum);
				throw new SemanticException("\nSymantic error at line "+lineNum+" char "+charPos +
						"\n Expressions type doesn't match for conditionalExpression, "+" Expression type is "+expr_type+" and conditionalExpression is "+type+ "\n In: "+line );
				//throw new SemanticException("Expressions type doesn't match for conditionalExpression");
			}
				
			else
				type = "boolean";
		}
		return type;
	}
	
}
