/*
 *  Class Token
 *  
 *  Each token is represented by its type and value. 
 */

public class Token {
	
	// list of tokens
	static final int EOF = 0;
	static final int SM = 1;
	static final int PO = 2;
	static final int MO = 3;
	static final int TO = 4;
	static final int DO = 5;
	static final int NM = 6;
	static final int FA = 7;
	static final int LP = 8;
	static final int RP = 9;
	static final int LB = 10;
	static final int RB = 11;
	static final int MD = 12;
	static final int KW = 13;
	static final int BL =14;
	static final int ID = 15;
	static final int EQ = 16;
	static final int AO = 17;
	static final int NE = 18;
	static final int LO = 19;
	static final int LA = 20;
	static final int ST = 21;
	static final int ERROR = -1;
	
	static final String [] stringToken = {"EOF", ";", "+", "-","*","/", "NM", ",", 
		"(",")", "{", "}", "%", "KW", "BL", "ID", "==", "=", "!=", "||", "&&", "ST"};
	
	private int token; // Type of of token
	private String lexeme; // The lexeme
	public int lineNum;
	public int charPos;
//	public Token(int token, String lexeme) {
//		this.token = token;
//		this.lexeme = lexeme;
//	}


	public Token(int token, String lexeme,int lineNumber,int charPos) {
		this.token = token;
		this.lexeme = lexeme;
		this.lineNum=lineNumber;
		this.charPos=charPos;
	}

	// Returns the type of the token
	public int getTokenType() {
		return token;
	}

	// Returns the lexeme of the token
	public String getLexeme() {
		return lexeme;
	}

	// Returns a string representation of the token
	public String toString() {
		return token + "\t" + lexeme;
	}
}