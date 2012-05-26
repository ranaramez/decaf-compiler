import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Set;


/* A simple symbol table for semantic analysis
 * 
 */
public class SymbolTable {
	// The Hash Table used to build the Symbol Table
	private Hashtable<String, Entry> st;
	
	// The level of the scope of the current instance
	private int level;
	
	// The current instance of the class
	private static SymbolTable inst;
	
	/*
	 * The constructor is set to private
	 * to prevent the creation of more than
	 * one instance of the class. 
	 */
	private SymbolTable() {
		st = new Hashtable<String, Entry>();
		level = 0;
	}
	
	/* 
	 * This method is used to get the current instance
	 * of the SymbolTable.
	 */
	public static SymbolTable getInstance() {
		if(inst==null)
			inst = new SymbolTable();
		
		return inst;
	}
	
	/*
	 * This method is used to add entries to the table
	 */
	public void add(Entry e) {
		e.level = level;
		st.put(e.id, e);
	}
	
	/*
	 * This method is used to get entries from the table
	 */
	public Entry get(String k) {
		return st.get(k);
	}
	
	/*
	 * This method checks if a certain identifier
	 * is already in the symbol table.
	 */
	public boolean contains(String id) {
		return st.containsKey(id);
	}
	
	/*
	 * This method opens a new scope in the hashtable
	 */
	public void openScope() {
		level++;
	}
	
	
	/*
	 * This methods closes the current scope and
	 * removes all the entries that were created
	 * created during that scope.
	 */
	public void closeScope() {
		
		Collection<Entry> ce = st.values();
		ArrayList<String> ls = new ArrayList<String>();
		
		for(Entry e: ce)
			if(e.level == level)
				ls.add(e.id);
				
		for(String s: ls)
			st.remove(s);
		
		level--;
	}
	
	public static void main(String[] args) {
		SymbolTable a = SymbolTable.getInstance();
		a.openScope();
		a.add(new Entry("X"));
		Entry [] params = new Entry [2];
		params[0] = new Entry("x", "int");
		params[1] = new Entry("y", "boolean");
		a.add(new Entry("isPrime", "boolean", params));
		a.add(new Entry("isPrime", "String", params));
		a.add(params[0]);
		a.add(params[1]);

		System.out.println(a.contains("isPrime"));
		a.get("isPrime").id = "isprime";
		
		System.out.println("prime  " + a.get("isPrime").type);
//		System.out.println(a.get("isPrime").getParams()[0].id);
//		System.out.println(a.get("isPrime").getParams()[1].id);
		String x = "3";
		System.out.println((x.contains(".")));
		System.out.println(a.contains("X"));
		a.openScope();
		System.out.println(a.contains("X"));
		a.add(new Entry("RANA", "helo"));
		System.out.println(a.get("RANA").getType());
		a.closeScope();
		System.out.println(a.contains("RANA"));
		System.out.println(a.contains("Y"));
		System.out.println(a.contains("X"));
		a.closeScope();
		
		System.out.println(a.contains("X"));
	}
}
