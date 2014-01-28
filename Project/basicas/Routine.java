package basicas;

import java.util.StringTokenizer;

public class Routine {

	private String name;
	private Types type;
	private Routine[] calledBy;
	private Routine[] callFor;
	private String source;
	private String packageName;
	
	public enum Types{
		PROCEDURE,FUNCTION
	}
	

	public String getName() {
		return name;
	}


	public Types getType() {
		return type;
	}


	public Routine[] getCalledBy() {
		return calledBy;
	}
	
	public String toString(){
		return this.getName();
	}


	public Routine[] getCallFor() {
		return callFor;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setType(Types type) {
		this.type = type;
	}
	
	public void setType(String s){
		if( s != null ){
			if( s.equalsIgnoreCase("procedure")){
				this.type = Types.PROCEDURE;
			}else{
				this.type = Types.FUNCTION;
			}
		}
	}


	public void setCalledBy(Routine[] calledBy) {
		this.calledBy = calledBy;
	}


	public void setCallFor(Routine[] callFor) {
		this.callFor = callFor;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	};
	
	public String getParameters(){
		int firstParentesis = this.source.indexOf("(");
		int lastParentesis = this.source.indexOf(")");
		return this.source.substring(firstParentesis+1,lastParentesis);
		
	}
	
	public String getParams(){
		String ret = getParameters();
		String ret2 = "";
		if( !ret.trim().equalsIgnoreCase("") ){
			StringTokenizer st = new StringTokenizer(ret);
			boolean first = true;
			while(st.hasMoreTokens()){
				String tok = st.nextToken();
				if( !first ){
					ret2 = ret2 + ",";
				}
				ret2 = ret2 + tok;			
				while( !tok.endsWith(",") ){
					if( st.hasMoreTokens() ){
						tok = st.nextToken();
					}else{
						break;
					}
					}
				first = false;
			}
		}
		return ret2;
	}
	
	public String getReturnDeclaration(){
		String ret = "";
		if( this.type == Types.FUNCTION ){
			int init = this.source.toLowerCase().indexOf("return");
			init = init + 6;
			char temp = this.source.charAt(init++);
			
			while(temp == ' '){
				temp = this.source.charAt(init++);
			}
			
			while(temp != ' '){
				ret += temp;
				temp = this.source.charAt(init++);
			}
			
			ret = "return " + ret;
		}
		return ret.trim();
	}
}
