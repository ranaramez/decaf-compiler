import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Class Lexer
 * 
 * Update the method nextToken() such to the provided
 * specifications of the Decaf Programming Language.
 * 
 * You are not allowed to use any built it in tokenizer
 * in Java. You are only allowed to scan the input file
 * one character at a time.
 */

public class Lexer {

	private BufferedReader reader; // Reader
	private char curr; // The current character being scanned
	private static final char EOF = (char) (-1);
	boolean decimalFlag = false;
	boolean stringAlphabetFlag = false;
	boolean validDecimal = true;
	String stringBuffer = "";
	public BufferedReader lineReader;
	public static int lineNum = 1;
	public static int charPos = 0;
	public static String currentLine = "";
	public static String fileName="";

	// End of file character

	public Lexer(String file) {
		try {
			
			reader = new BufferedReader(new FileReader(file));
			lineReader = new BufferedReader(new FileReader(file));
			fileName=file;
			currentLine = getLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Read the first character
		curr = read();
	}

	private char read() {
		try {
			charPos++;
			return (char) (reader.read());
		} catch (IOException e) {
			e.printStackTrace();
			return EOF;
		}
	}

	public String getLine() {
		String result = "";

		try {
			result = lineReader.readLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	public String getLine(int lineNum) throws IOException
	{
		lineReader=new BufferedReader(new FileReader(fileName));
		
		String result="";
		for(int i=1;i<=lineNum;i++)
		{
			result=lineReader.readLine();
		}
		
		return result;
	}
	// Checks if a character is a digit
	private boolean isNumeric(char c) {
		if ((c >= '0' && c <= '9'))
			return true;

		return false;
	}

	private boolean isAlphabetic(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {

			stringAlphabetFlag = true;
			return true;
		}

		return false;
	}

	private boolean isdecimal(char c) {
		if (c == '.' && decimalFlag == false) {
			decimalFlag = true;

			return true;
		}
		return false;
	}

	private boolean isKeyWord(String word) {
		if (word.equals("class") || word.equals("else") || word.equals("if")
				|| word.equals("int") || word.equals("float")
				|| word.equals("String") || word.equals("return")
				|| word.equals("static") || word.equals("while")
				|| word.equals("boolean"))
			return true;

		return false;
	}

	private boolean isBoolean(String word) {
		if (word.equals("true") || word.equals("false"))
			return true;
		return false;
	}

	public Token nextToken() {

		int state = 1; // Initial state
		int numBuffer = -1; // A buffer for number literals
		String and = "";
		String or = "";
		String equal = "";
		String notEqual = "";

		double numberCount = 1;

		while (true) {
			if (curr == EOF) {
				try {

					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new Token(0, "", lineNum, charPos);
			}

			switch (state) {
			// Controller
			case 1:
				// System.out.println("case1");
				switch (curr) {
				case ' ': // Whitespaces
				case '\b':
				case '\f':
				case '\r':
				case '\t':
					curr = read();
					continue;

				case '\n':
					lineNum++;
					charPos = 0;
					curr = read();
					currentLine = getLine();
					continue;
				case ';':
					curr = read();
					return new Token(1, ";", lineNum, charPos-1);
				case ',':
					curr = read();
					return new Token(7, ",", lineNum, charPos-1);
				case '(':
					curr = read();
					return new Token(8, "(", lineNum, charPos-1);
				case ')':
					curr = read();
					return new Token(9, ")", lineNum, charPos-1);
				case '{':
					curr = read();
					return new Token(10, "{", lineNum, charPos-1);
				case '}':
					curr = read();
					return new Token(11, "}", lineNum, charPos-1);

				case '+':
					curr = read();
					return new Token(2, "+", lineNum, charPos-1);

				case '-':
					curr = read();
					return new Token(3, "-", lineNum, charPos-1);

				case '*':
					curr = read();
					return new Token(4, "*", lineNum, charPos-1);

					// case '/':
					// curr = read();
					// return new Token(5, "/");
				case '%':
					curr = read();
					return new Token(12, "%", lineNum, charPos-1);
				case '.':

					System.out.println(currentLine + "\n\nWarrning at line "
							+ lineNum + " char " + (charPos)
							+ ":\n A dot with no number before \n In: "
							+ currentLine + '\n');
					curr = read();
					continue;
					// return new Token(-1, "  Invalid Input: " +
					// temp,lineNum,charPos);
				case '=':
					state = 4;
					continue;
				case '!':
					state = 5;
					continue;
				case '|':
					state = 6;
					continue;
				case '&':
					state = 7;
					continue;
				case '"':
					state = 8;
					continue;
				case '/':
					state = 10;
					continue;

				default:
					state = 2;
					// System.out.println("case");
					continue;

				}

				// Integer - Start
			case 2:
				// System.out.println(curr);
				if (isNumeric(curr)) {
					// numBuffer = -1; // Reset the buffer.
					decimalFlag = false;
					validDecimal = true;
					numberCount = 1;
					numBuffer = 0;
					numBuffer += (curr - '0');
					state = 3;
					curr = read();
					if (curr == EOF) {
						return new Token(6, "" + numBuffer, lineNum, charPos-(""+numBuffer).length());

					}
				} else {
					if (isAlphabetic(curr) && numBuffer == -1) {
						stringBuffer += curr;
						state = 33;
						curr = read();

					}

					else {
						char temp = curr;
						curr = read();
						state = 1;
						System.out.println(temp);
						System.out.println(currentLine
								+ "\n\nWarrning at line " + lineNum + " char "
								+ (charPos-1)+ ":\n Invalid input \n In: "
								+ currentLine + '\n');

						return new Token(6, "" + numBuffer, lineNum, charPos-1);

						// return new Token(-1, "  Invalid Input: " +
						// temp,lineNum,charPos);

					}

				}
				continue;

				// Number - Body
			case 3:

				if (isNumeric(curr)) {
					if (decimalFlag == true)
						numberCount *= 10;
					numBuffer *= 10;
					numBuffer += (curr - '0');

					curr = read();
					if (curr == EOF) {
						if (numBuffer > -1) {
							if (decimalFlag == true) {
								if (numberCount > 1) {
									System.out.println("here3");
									decimalFlag = false;
									int tempBuffer = numBuffer;
									numBuffer = -1;
									// System.out.println("numberCount2  "+numberCount);

									return new Token(6, "" + tempBuffer
											/ numberCount, lineNum, charPos-(""+tempBuffer
											/ numberCount).length());
								} else {
									int tempBuffer = numBuffer;
									numBuffer = -1;
									// System.out.println("numberCount2  "+numberCount);
									System.out.println(currentLine
											+ "\n\nWarrning at line " + lineNum
											+ " char " + (charPos-(""+tempBuffer).length())
											+ ":\n Dot after a number \n In: "
											+ currentLine + '\n');
									return new Token(6, "" + tempBuffer,
											lineNum, charPos-(""+tempBuffer).length());
								}

							}

							else if (decimalFlag == false) {
								int tempBuffer = numBuffer;
								numBuffer = -1;
								return new Token(6, "" + tempBuffer, lineNum,
										charPos-(""+tempBuffer).length());
							}
						}

					}
					continue;
				}
				if (isdecimal(curr)) {
					// System.out.println("decimal" + curr);
					if (numBuffer > -1) {
						curr = read();
					}

				} else {
					if (numBuffer > -1) {

						if (decimalFlag == true) {
							if (numberCount > 1) {
								// System.out.println("here3");
								decimalFlag = false;
								int tempBuffer = numBuffer;
								numBuffer = -1;
								// System.out.println("numberCount2  "+numberCount);

								return new Token(6, "" + tempBuffer
										/ numberCount, lineNum, charPos-(""+tempBuffer
										/ numberCount).length());
							} else {
								int tempBuffer = numBuffer;
								numBuffer = -1;
								// System.out.println("numberCount2  "+numberCount);
								System.out.println(currentLine
										+ "\n\nWarrning at line " + lineNum
										+ " char " + (charPos-(""+tempBuffer).length())
										+ ":\n Dot after a number \n In: "
										+ currentLine + '\n');
								return new Token(6, "" + tempBuffer, lineNum,
										charPos-(""+tempBuffer).length());
							}

						}

						else if (decimalFlag == false) {
							int tempBuffer = numBuffer;
							numBuffer = -1;
							return new Token(6, "" + tempBuffer, lineNum,
									charPos-( ""+tempBuffer).length());
						}

					}
				}
				continue;
				// Identifier - Body

			case 33:
				if ((isAlphabetic(curr) || isNumeric(curr)) && curr != EOF) {
					stringBuffer += curr;

					curr = read();

					if (curr == EOF) {
						String temp = stringBuffer;
						stringBuffer = "";

						if (isKeyWord(temp)) {

							return new Token(13, "" + temp, lineNum, charPos-(""+temp).length());
						}
						if (isBoolean(temp))
							return new Token(14, "" + temp, lineNum, charPos-(""+temp).length());
						return new Token(15, "" + temp, lineNum, charPos-(""+temp).length());
					}
				} else {
					String temp = stringBuffer;
					stringBuffer = "";
					if (isKeyWord(temp)) {

						return new Token(13, "" + temp, lineNum, charPos-(""+temp).length());
					}
					if (isBoolean(temp))
						return new Token(14, "" + temp, lineNum, charPos-(""+temp).length());
					return new Token(15, "" + temp, lineNum, charPos-(""+temp).length());

				}
				continue;

			case 4:
				equal += curr;
				curr = read();
				if (curr == '=') {
					equal += curr;
					curr = read();
					return new Token(16, "" + equal, lineNum, charPos-(""+equal).length());
				} else {
					return new Token(17, "" + equal, lineNum, charPos-(""+equal).length());
				}
			case 5:
				notEqual += curr;
				curr = read();
				if (curr == '=') {
					notEqual += curr;
					curr = read();
					return new Token(18, "" + notEqual, lineNum, charPos-(""+notEqual).length());
				} else {
					System.out.println("case5");
					System.out.println(currentLine + "\n\nWarrning at line "
							+ lineNum + " char " + (charPos-(""+notEqual).length())
							+ ":\n ! converted to != \n In: " + currentLine
							+ '\n');
					return new Token(18, "" + notEqual, lineNum, charPos-(""+notEqual).length());
					// return new Token(-1, "Invalid input: " + notEqual);
				}
			case 6:
				or += curr;
				curr = read();
				if (curr == '|') {
					or += curr;
					curr = read();
					return new Token(19, "" + or, lineNum, charPos-(""+or).length());
				} else {
					System.out.println("case6");
					System.out.println(currentLine + "\n\nWarrning at line "
							+ lineNum + " char " + (charPos-(""+or).length())
							+ ":\n | converted to || \n In: " + currentLine
							+ '\n');
					return new Token(19, "" + or, lineNum, charPos-(""+or).length());
					// return new Token(-1, "Invalid input: " + or);
				}
			case 7:
				and += curr;
				curr = read();
				if (curr == '&') {
					and += curr;
					curr = read();
					return new Token(20, "" + and, lineNum, charPos-(""+and).length());
				} else {
					System.out.println("case7");
					System.out.println(currentLine + "\n\nWarrning at line "
							+ lineNum + " char " + (charPos-(""+and).length())
							+ ":\n & converted to && \n In: " + currentLine
							+ '\n');
					return new Token(20, "" + and, lineNum, charPos-(""+and).length());
					// return new Token(-1, "Invalid input: " + and);
				}
			case 8:
				stringBuffer = "";
				stringBuffer += curr;
				state = 9;
				curr = read();
				continue;

			case 9:
				if (curr != '"' && curr != '\n') {
					stringBuffer += curr;
					curr = read();
					if (curr == EOF) {
						System.out
								.println(currentLine
										+ "\n\nWarrning at line "
										+ lineNum
										+ " char "
										+ (charPos-("").length())
										+ ":\n missing \" at the end of the string\n In: "
										+ currentLine + '\n');
						return new Token(21, "", lineNum, charPos-("").length());
						// return new Token(-1,
						// "Invalid String Literal",lineNum,charPos);
					}
				}

				else {
					if (curr == '\n') {
						int tempLineNum = lineNum;
						String currString = currentLine;
						int charPostTemp = charPos;

						curr = read();
						stringBuffer += "\"";
						String temp = stringBuffer;
						stringBuffer = "";
						System.out
								.println(currentLine
										+ "\n\nWarrning at line "
										+ tempLineNum
										+ " char "
										+ (charPostTemp-(""+stringBuffer).length())
										+ ":\n missing \" at the end of the string\n In: "
										+ currString + '\n');
						
							lineNum++;
							currentLine = getLine();
						
						return new Token(21, "" + stringBuffer, tempLineNum,
								charPostTemp-(""+stringBuffer).length());
						
					}

					stringBuffer += curr;
					// curr = read();
					if (curr != '\n') {
						curr = read();
						String temp = stringBuffer;
						stringBuffer = "";
						return new Token(21, "" + temp, lineNum, charPos-(""+temp).length());

					}

					

				}
				continue;
			case 10:
				curr = read();
				if (curr == '/') {
					state = 11;
					curr = read();
					continue;
				}
				if (curr == '*') {
					state = 12;
					curr = read();
					continue;
				} else
					return new Token(5, "/", lineNum, charPos-("/").length());
			case 11:
				// curr=read();
				if (curr == '\n') {
					curr = read();
					lineNum++;
					currentLine = getLine();
					state = 1;
				}
			case 12:
				if (curr != '*')
					curr = read();
				else {
					curr = read();
					if (curr == '/') {
						curr = read();
						state = 1;
					}
				}
				continue;
			}
		}
	}
}