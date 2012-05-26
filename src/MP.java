import java.io.IOException;

/*
 * Class MP
 * 
 * Parses a specified input file and prints whether the
 * file was successfully printed or not.
 * 
 * Input file can be given as command line argument.
 * If no arguments are given, a hard coded file name
 * will be used.
 * 
 */
public class MP {
	public static void main(String[] args) throws IOException{
		String inFile = "Sample.in";

		if (args.length > 1) {
			inFile = args[0];
		}

		Lexer lexer = new Lexer(inFile);

		Parser parser = new Parser(lexer);
		
		
		ClassDecl cd;
		try {
			cd = parser.parse();
//			System.out.println(cd);
			
			cd.check();
			
			System.out.println("Semantic Analysis Completed with No Errors.");
		} catch (SyntaxException e) {
			e.printStackTrace();
		} catch (SemanticException e) {
			e.printStackTrace();
		}
	}
}
