package basicas;

public class ParserUtils {
	
	
	
	public static String removeBlockComments(String s){
		String ret = new String(s);
		int commPos = -1;
		int init = 0;
		
		while( ret.indexOf("/*") != -1 ){
			commPos = ret.indexOf("/*");
			int end = ret.indexOf("*/",commPos);
			if(end == -1){
				end = ret.length();
			}
			if(end != ret.length()){
				ret = ret.substring(init, commPos) + ret.substring(end+2, ret.length());
			}else{
				ret = ret.substring(init, commPos);
			}
		}
		
		return ret;
	}
	
	public static String removeSingleLineComments(String s){
		String ret = new String(s);
		int commPos = -1;
		int init = 0;
		
		while( ret.indexOf("--") != -1 ){
			commPos = ret.indexOf("--");
			int end = ret.indexOf("\n",commPos);
			if(end == -1){
				end = ret.length();
			}
			
			
			if(end != ret.length()){
				ret = ret.substring(init, commPos) + ret.substring(end, ret.length());
			}else{
				ret = ret.substring(init, commPos);
			}
			
		}
		
		return ret;
	}
	
	public static String removeDoubleSpaces(String s){
		String ret = s;
		String white = "  ";
		while(ret.indexOf(white) != -1){
			ret = ret.replaceAll(white, " ");
		}
		return ret;
	}

}
