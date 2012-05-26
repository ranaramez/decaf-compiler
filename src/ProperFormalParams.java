import java.io.IOException;
import java.util.ArrayList;


public class ProperFormalParams {
	
//	public FormalParam fp;
//	public ProperFormalParams fps;
	public ArrayList<FormalParam> fps;
	public int lineNum=-1;
	public ProperFormalParams(){
		
	}
	
	public ProperFormalParams(ArrayList<FormalParam> fps){
		this.fps = fps;
	}
	
	public String toString(){
		String ret = "Formal params\n";
		String s = "";
		
		if(this.fps != null){
			for(int i=0; i< fps.size(); i++)
				s += fps.get(i).toString();
		}
		
		for(String st: s.split("\n"))
			ret += "| " + st + "\n";
		return ret;
		
	}
	
	public Entry [] check(String method_name) throws SemanticException, IOException {
		Entry [] result = new Entry[fps.size()];
		Entry temp = null;
		for(int i=0; i<fps.size(); i++){
			temp = fps.get(i).check();
			for(int j=0; j<i; j++){
				if(temp.id.equals(result[j].id))
				{
					String line=Parser.getLine(lineNum);
					throw new SemanticException("\nSymantic error at line "+lineNum+" char "+line.indexOf(temp.id) +
							"\n Param name is not unique "+temp.id+" was declared before \n In: "+line );
					//throw new SemanticException("Param name is not unique ... can't declare two params with the same name");
				}
					
			}
			result[i] = temp;
		}
		return result;
	}
	
}
