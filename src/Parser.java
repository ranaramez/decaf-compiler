import java.io.IOException;
import java.util.ArrayList;


/*
 * class Parser
 * 
 * Parses the tokens returned from the lexical analyzer.
 * 
 * Update the parser implementation to parse the Decaf
 * language according to the grammar provided. 
 */
public class Parser {

	private static Lexer lexer; // lexical analyzer
	private Token token; // look ahead token
	private int lineNum = -2;
	
	public Parser(Lexer lex) {
		lexer = lex;
		lineNum = Lexer.lineNum;
	}
	
	private void printTokenSyntaxError(String expectedToken) throws SyntaxException{
		String s = "Syntax Error at line " + token.lineNum + " char " + token.charPos + ":\n";
		s += "Expected '" + expectedToken + "'\n";
		s += "Found '" + token.getLexeme() + "'\n";
		s += "In: " + Lexer.currentLine;
		throw new SyntaxException(s);
	}
	
	private void printGrammarSyntaxError(String expected) throws SyntaxException{
		String s = "Syntax Error at line " + token.lineNum + " char " + token.charPos + ":\n";
		s += "Cannot match '" + expected + " in:\n";
		s += Lexer.currentLine;
		throw new SyntaxException(s);
	}
	
	public static String getLine(int line) throws SemanticException, IOException{
		return lexer.getLine(line);
	}
	
	public ClassDecl parse() throws SyntaxException {
		
		token = lexer.nextToken();
		
		return ClassDecl();
	}
	
	private ClassDecl ClassDecl() throws SyntaxException {
		String tokenName = "";
		ClassDecl value = new ClassDecl();
		if(token.getLexeme().equals("class") ){
			match(Token.KW);
			tokenName = token.getLexeme();
			try{
				match(Token.ID);
			}catch (SyntaxException e) {
				printGrammarSyntaxError(token.getLexeme() + "' to a class name");
				
	        }
			match(Token.LB);
			value = new ClassDecl(tokenName, MethodDecls());
			match(Token.RB);
		}else{
			printTokenSyntaxError("class");		
		}
		return value;
	}
	
	private ArrayList<MethodDecl> MethodDecls() throws SyntaxException {
		ArrayList<MethodDecl> result = new ArrayList<MethodDecl>();
		String id = "";
		String type = "";
		while(token.getLexeme().equals("static")) {
			match(Token.KW);
			type = token.getLexeme();
			matchType();
			id = token.getLexeme();
			match(Token.ID);
			match(Token.LP);
			ProperFormalParams fp=FormalParams();
			lineNum = Lexer.lineNum;
			fp.lineNum = lineNum;
			// fp.charPos=token.charPos;
			match(Token.RP);
			int charPos = token.charPos;
			MethodDecl m = new MethodDecl(type, id, fp, Block());
			m.lineNum = lineNum;
			// m.charPos=token.charPos;
			result.add(m);
		}
		if(token.getTokenType() != Token.RB){
			printGrammarSyntaxError(token.getLexeme() + "' to a Method declaration or '}");
		}	
		return result;
	}
	private ProperFormalParams FormalParams() throws SyntaxException {
		ArrayList<FormalParam> params = new ArrayList<FormalParam>();
		if(Type()){
			FormalParam value = FormalParam();
			value.lineNum = Lexer.lineNum;
			params.add(value);
			while(token.getTokenType() != Token.LP && token.getTokenType() == Token.FA){
				match(Token.FA);
				value = FormalParam();
				value.lineNum = Lexer.lineNum;
				params.add(value);
			}
			// This condition for telling that the lexer is waiting for an FA token and not LP "missing FA"
			if(token.getTokenType() != Token.RP && token.getTokenType() != Token.FA && token.getTokenType() != Token.LB ){
				match(Token.FA);
			}
		}else if(token.getTokenType() != Token.RP){
			printGrammarSyntaxError(token.getLexeme() + "' to a Type");
		}
		return new ProperFormalParams(params);
	}

	/* Before calling Formal Param we make sure that we checked on the Type before calling it
	 * Because it assumes that Type = true and we are just moving to the next Token without checking*/
	private FormalParam FormalParam() throws SyntaxException {
		String id = "";
		String type = token.getLexeme();
		try{
			match(Token.KW);
		}catch (SyntaxException e) {
			printGrammarSyntaxError(token.getLexeme() + "' to a Type");
        }
		id = token.getLexeme();
		match(Token.ID);
		FormalParam value = new FormalParam(type, id);
		return value;
	}
	
	private Block Block() throws SyntaxException {
		match(Token.LB);
		Block result = new Block(Statements());
		match(Token.RB);
		return result;
	}

	private ArrayList<Statement> Statements() throws SyntaxException {
		ArrayList<Statement> result = new ArrayList<Statement>();
		while(Type() || token.getLexeme().equals("if") 
				|| token.getLexeme().equals("while")
				|| token.getLexeme().equals("return") 
				|| token.getTokenType() == Token.ID
				|| token.getTokenType() == Token.LB){
			result.add(Statement());
		}	
		return result;
	}
	
	private Statement Statement() throws SyntaxException {
		Statement result = null;
		if(Type())
			result = LocalVarDecl();
		else if(token.getLexeme().equals("if"))
			result = IfStmt();
		else if(token.getLexeme().equals("while"))
			result = WhileStmt();
		else if(token.getLexeme().equals("return"))
			result = ReturnStmt();
		else if(token.getTokenType() == Token.ID)
			result = AssignStmt();
		else if(token.getTokenType() == Token.LB)
			result = Block();
		return result;
	}


	private ReturnStmt ReturnStmt() throws SyntaxException
	{
		match(Token.KW);
		int ln = lexer.lineNum;
		int charPos = token.charPos;
		ReturnStmt value= new ReturnStmt(Expression());
		value.lineNum = ln;
		value.charPos = charPos;
		match(Token.SM);
		return value;
		
	}

	private WhileStmt WhileStmt() throws SyntaxException {
		match(Token.KW);
		match(Token.LP);
		int ln = lexer.lineNum;
		int charPos = token.charPos;
		Expression e = Expression();
		match(Token.RP);
		Statement stmt = Statement();
		if(stmt == null)
			printGrammarSyntaxError(token.getLexeme() + "' to a while statement");
		WhileStmt wStmt = new WhileStmt(e, stmt);
		wStmt.lineNum = ln;
		wStmt.charPos = charPos;
		return wStmt;

	}

	private IfStmt IfStmt() throws SyntaxException {
		IfStmt value = null;
		match(Token.KW);
		match(Token.LP);
		int ln = lexer.lineNum;
		int charPos = token.charPos;
		Expression e = Expression();
		match(Token.RP);
		Statement stmt = Statement();
		if(stmt == null)
			printGrammarSyntaxError(token.getLexeme() + "' to an If statement");
		if (token.getLexeme().equals("else")) {
			match(Token.KW);
			ElseStmt eStmt = new ElseStmt(Statement());
			value = new IfStmt(e, stmt, eStmt);
			value.lineNum = ln;
			value.charPos = charPos;
		} else{
			value = new IfStmt(e, stmt);
			value.lineNum = ln;
			value.charPos = charPos;
		}
		return value;
	}

	
	private AssignStmt AssignStmt() throws SyntaxException {
		String id = token.getLexeme();
		match(Token.ID);
//		if (token.getTokenType() == Token.AO)
		match(Token.AO);
		int ln = token.lineNum;
		int charPos = token.charPos;
		AssignStmt value = new AssignStmt(id, Expression());
		value.lineNum = ln;
		value.charPos = charPos;
		match(Token.SM);
		return value;
	}

	private LocalVarDecl LocalVarDecl() throws SyntaxException {
		String id = "";
		String type = token.getLexeme();
		matchType();
		id = token.getLexeme();
		match(Token.ID);
		LocalVarDecl value = new LocalVarDecl(type, id);
		value.lineNum = token.lineNum;
		value.charPos = token.charPos;
		match(Token.SM);
		return value;
	}

	
	
	private Expression Expression() throws SyntaxException {
		Expression expr = ConditionalAndExpr();
		while(token.getTokenType() == Token.LO){
			match(Token.LO);
			int ln = token.lineNum;
			int charPos = token.charPos;
			expr = new Expression(ConditionalAndExpr(), Expression.LO, expr);
			expr.lineNum = ln;
			expr.charPos = charPos;
		}
		return expr;
	}

	private ConditionalAndExpr ConditionalAndExpr() throws SyntaxException {
		ConditionalAndExpr expr = EqualityExpr();
		while(token.getTokenType() == Token.LA){
			match(Token.LA);
			int ln = token.lineNum;
			int charPos = token.charPos;
			expr = new ConditionalAndExpr(EqualityExpr(), ConditionalAndExpr.LA, expr);
			expr.lineNum = ln;
			expr.charPos = charPos;
		}
		return expr;
		
	}

	private EqualityExpr EqualityExpr() throws SyntaxException {
		EqualityExpr expr = AdditiveExpr();
		while(true){
			switch(token.getTokenType()){
			case Token.EQ:
				match(Token.EQ);
				int ln = token.lineNum;
				int charPos = token.charPos;
				expr = new EqualityExpr(AdditiveExpr(), EqualityExpr.EQ, expr);
				expr.lineNum = ln;
				expr.charPos = charPos;
				break;
			case Token.NE:
				match(Token.NE);
				int ln1 = token.lineNum;
				int charPos1 = token.charPos;
				expr = new EqualityExpr(AdditiveExpr(), EqualityExpr.NE, expr);
				expr.lineNum = ln1;
				expr.charPos = charPos1;
				break;
			case Token.AO:
				printGrammarSyntaxError(token.getLexeme() + "' to an expression \nexpected: '=='");
			default:
				return expr;		
			}
		}
	}

	private AdditiveExpr AdditiveExpr() throws SyntaxException {
		AdditiveExpr value = MultiplicativeExpr();
		while (true) {
			switch (token.getTokenType()) {
			case Token.MO:
				match(Token.MO);
				int ln = token.lineNum;
				int charPos = token.charPos;
				value = new AdditiveExpr(MultiplicativeExpr(), AdditiveExpr.MO, value);
				value.lineNum = ln;
				value.charPos = charPos;
				break;

			case Token.PO:
				match(Token.PO);
				int ln2 = token.lineNum;
				int charPos2 = token.charPos;
				value = new AdditiveExpr(MultiplicativeExpr(), AdditiveExpr.PO, value);
				value.lineNum = ln2;
				value.charPos = charPos2;
				break;

			default:
				return value;
			}
		}
		
	}

	private MultiplicativeExpr MultiplicativeExpr() throws SyntaxException {
		MultiplicativeExpr value = PrimaryExpr();
		while (true) {
			switch (token.getTokenType()) {
			case Token.TO:
				match(Token.TO);
				int ln = token.lineNum;
				int charPos = token.charPos;
				value = new MultiplicativeExpr(PrimaryExpr(), MultiplicativeExpr.MO, value);
				value.lineNum = ln;
				value.charPos = charPos;
				break;

			case Token.DO:
				match(Token.DO);
				int ln2 = token.lineNum;
				int charPos2 = token.charPos;
				value = new MultiplicativeExpr(PrimaryExpr(), MultiplicativeExpr.DO, value);
				value.lineNum = ln2;
				value.charPos = charPos2;
				break;
				
			case Token.MD:
				match(Token.MD);
				int ln3 = token.lineNum;
				int charPos3 = token.charPos;
				value = new MultiplicativeExpr(PrimaryExpr(), MultiplicativeExpr.MD, value);
				value.lineNum = ln3;
				value.charPos = charPos3;
				break;

			default:
				return value;
			}
		}
	}

	private PrimaryExpr PrimaryExpr() throws SyntaxException {
		PrimaryExpr value = null;
		String terminal = "";
		switch (token.getTokenType()) {
		case Token.NM:
			terminal = token.getLexeme();
			match(Token.NM);
			int ln = token.lineNum;
			int charPos = token.charPos;
			value = new PrimaryExpr(terminal, PrimaryExpr.NM, null, null);
			value.lineNum = ln;
			value.charPos = charPos;
			break;

		case Token.BL:
			terminal = token.getLexeme();
			match(Token.BL);
			int ln1 = token.lineNum;
			int charPos1 = token.charPos;
			value = new PrimaryExpr(terminal, PrimaryExpr.BL, null, null);
			value.lineNum = ln1;
			value.charPos = charPos1;
			break;
			
		case Token.ST:
			terminal = token.getLexeme();
			match(Token.ST);
			int ln2 = token.lineNum;
			int charPos2 = token.charPos;
			value = new PrimaryExpr(terminal, PrimaryExpr.ST, null, null);
			value.lineNum = ln2;
			value.charPos = charPos2;
			break;
			
		case Token.ID:
			terminal = token.getLexeme();
			match(Token.ID);
			int ln3 = token.lineNum;
			int charPos3 = token.charPos;
			if(token.getTokenType() == Token.LP){
				value = new PrimaryExpr("", 0, CallExpr(terminal), null);
				value.lineNum = ln3;
				value.charPos = charPos3;
			}else{
				value = new PrimaryExpr(terminal, PrimaryExpr.ID, null, null);
				value.lineNum = ln3;
				value.charPos = charPos3;
			}
			break;
			
		case Token.LP:
			match(Token.LP);
			int ln4 = token.lineNum;
			int charPos4 = token.charPos;
			value = new PrimaryExpr("", 0, null, Expression());
			value.lineNum = ln4;
			value.charPos = charPos4;
			match(Token.RP);
			break;
		default:
			printGrammarSyntaxError(token.getLexeme() + "' to an expression");
		}
		
		return value;

	}

	private CallExpr CallExpr(String id) throws SyntaxException {
		match(Token.LP);
		int ln = token.lineNum;
		int charPos = token.charPos;
		CallExpr expr = new CallExpr(id, ActualParams());
		expr.lineNum = ln;
		expr.charPos = charPos;
		match(Token.RP);
		return expr;
	}

	private ActualParams ActualParams() throws SyntaxException {
		ArrayList<Expression> exprs = new ArrayList<Expression>();
		int ln = token.lineNum;
		int charPos = token.charPos;
		if(token.getTokenType() == Token.RP)
			return new ActualParams(exprs,ln,charPos);
		exprs.add(Expression());
		while(token.getTokenType() == Token.FA){
			match(Token.FA);
			exprs.add(Expression());
		}
		return new ActualParams(exprs,ln,charPos);
	}

	/*This method is used for a certain KW match check because the default match implemented at the end
	 * will not satisfy the matching with a specific KW but it matches anything under KW
	 * Ex: I want to match int, float, boolean, String only in the KW token
	 * macth(Token.KW) will not throw exception for "class"
	 * matchType() will throw exception for "class", "static" and any other KW not here */
	private void matchType() throws SyntaxException {
		if (!token.getLexeme().equals("int")
				&& !token.getLexeme().equals("float")
				&& !token.getLexeme().equals("boolean")
				&& !token.getLexeme().equals("String"))
			printGrammarSyntaxError(token.getLexeme() + "' to a Type");
		else
			token = lexer.nextToken();
	}
	private boolean Type() {
		if (token.getLexeme().equals("int")
				|| token.getLexeme().equals("float")
				|| token.getLexeme().equals("boolean")
				|| token.getLexeme().equals("String"))
			return true;
		return false;
	}
	
	private void match(int t) throws SyntaxException {
		if (token.getTokenType() == t) {
			token = lexer.nextToken();
		} else {
			if(!Token.stringToken[t].equals("ID"))
				printTokenSyntaxError(Token.stringToken[t]);
			else
				printGrammarSyntaxError(token.getLexeme() + "' to an Identifier");
			throw new SyntaxException("Cannot Match Tokens");
		}
	}
}
