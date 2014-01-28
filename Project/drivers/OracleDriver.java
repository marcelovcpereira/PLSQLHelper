package drivers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import basicas.Package;
import basicas.ParserUtils;
import basicas.Routine;

public class OracleDriver extends DBMSDriver{

	
	public Routine[] getRoutinesFromPackage(String packageName) throws SQLException{
		Routine[] ret = null;
		StringBuffer buff = new StringBuffer("");
		String query = "SELECT distinct aa.object_name," +
		"(CASE WHEN "+ 
			"(SELECT count(*) FROM all_arguments aa1 WHERE aa1.argument_name IS NULL AND aa1.object_name = aa.object_name) > 0 THEN 'FUNCTION'"+
			" ELSE 'PROCEDURE' "+
		"END) FROM all_arguments aa,all_objects ao WHERE "+
		"ao.object_type = 'PACKAGE' AND " +
		"ao.object_name = ? AND " +
		"ao.owner = ? AND " +
		"ao.object_id = aa.object_id " + 
		"ORDER BY aa.object_name";
		
		PreparedStatement pstm = this.getConnecter().getConn().prepareStatement(query);
		ResultSet rset = null;
	 
		pstm.setString(1, packageName.toUpperCase());
		pstm.setString(2, getConnecter().getUsername());

		rset = pstm.executeQuery();
		
		// If the object is not present return false
		while(rset.next()) {
			buff.append(rset.getString(1) + "@"); 
			buff.append(rset.getString(2) + ",");
		}
		String[] routs = buff.toString().split(",");
		
		ret = new Routine[routs.length];
		for(int i = 0; i < routs.length; i++){
			
			ret[i] = new Routine();
			String[] fields = routs[i].split("@");
			ret[i].setName(fields[0]);
			ret[i].setType(fields[1]);
			ret[i].setPackageName(packageName);
		}
		
		if( rset != null ){
			rset.close();
		}
		if(pstm != null){
			pstm.close();
		}
		
		return ret;
	}
	public String getRoutineSource(Routine routine) throws SQLException{
		String ret = "";
		
		StringBuffer buff = new StringBuffer("");
		String query = 
			"SELECT a.text FROM all_source a " +
			"WHERE a.type = 'PACKAGE BODY' AND "+
			"a.owner = ? AND "+
			"a.name = ?";
		
		PreparedStatement pstm = this.getConnecter().getConn().prepareStatement(query);
		ResultSet rset = null;
	 
		pstm.setString(1,getConnecter().getUsername());
		pstm.setString(2, routine.getPackageName().toUpperCase());

		rset = pstm.executeQuery();
		
		// If the object is not present return false
		while(rset.next()) {
			buff.append(rset.getString(1));
		}
		String packageSource = buff.toString().toUpperCase();
		packageSource = ParserUtils.removeSingleLineComments(packageSource);
		packageSource = ParserUtils.removeBlockComments(packageSource);
		
		
		String type = routine.getType().toString().toUpperCase();
		String name = routine.getName().toUpperCase();
		String declaration = type + " " + name; 

		//no double spaces
		String pkg = ParserUtils.removeDoubleSpaces(packageSource.toUpperCase());
		
		
		int initPos = pkg.indexOf(declaration.toUpperCase());

		String finalization = "end " + name + ";";
		int endPos = pkg.indexOf(finalization.toUpperCase(),initPos);
		
		if( endPos == -1 ){
			finalization = "END;";
			endPos = searchEndPos(initPos,routine,pkg);			
		}
		
		
		ret = pkg.substring(initPos, endPos+finalization.length());
		
		return ret;
	}
	
	public String[] getPackageSource(Package pack) throws SQLException{
		String[] ret = null;
		Vector<String> vector = new Vector<String>();
		
		String query = 
			"SELECT a.text FROM all_source a " +
			"WHERE a.type = 'PACKAGE' AND "+
			"a.owner = ? AND "+
			"a.name = ?";
		
		PreparedStatement pstm = this.getConnecter().getConn().prepareStatement(query);
		ResultSet rset = null;
	 
		pstm.setString(1,getConnecter().getUsername());
		pstm.setString(2, pack.getName().toUpperCase());

		rset = pstm.executeQuery();
		
		// If the object is not present return false
		while(rset.next()) {
			vector.add(rset.getString(1));
		}
		
		ret = new String[vector.size()];
		for(int i = 0; i < vector.size(); i++){
			ret[i] = vector.elementAt(i);
		}
		
		return ret;
	}
	
	public int searchEndPos(int initPos,Routine r, String pkg){
		int ret = 0;		
	
		//first end
		String finalization = "END;";
		ret = pkg.indexOf(finalization,initPos);	
		String teste = pkg.substring(ret, ret+20);
		
		
		String beginStr = "BEGIN";
		int beginL = beginStr.length();
		
		//first begin
		int begin = 0;
		begin = pkg.indexOf(beginStr,initPos);
		teste = pkg.substring(begin, begin+20);
		
		
		
		int count = 1; 
		//is there any other begin?
		begin = pkg.indexOf(beginStr, begin+beginL);
		teste = pkg.substring(begin, begin+20);
		while(begin != -1){			
			if( begin < ret ){
				count++;
				ret = pkg.indexOf(finalization,ret+finalization.length());
				teste = pkg.substring(ret, ret+20);
			}else{
				break;
			}
			begin = pkg.indexOf(beginStr, begin+beginL);
			teste = pkg.substring(begin, begin+20);
		}
		return ret;
	}
	
	public int getRoutinesCount() throws SQLException{
		int count = 0;
		String query = "SELECT distinct aa.object_name, "+
		"(CASE WHEN "+
		"(SELECT count(*) FROM all_arguments aa1 WHERE aa1.argument_name IS NULL AND aa1.object_name = aa.object_name) > 0 THEN 'FUNCTION' "+
			 "ELSE 'PROCEDURE' "+
		"END), ao.object_name FROM all_arguments aa,all_objects ao WHERE "+
		"ao.object_type = 'PACKAGE' AND "+
		"ao.owner = ? AND "+
		"ao.object_id = aa.object_id "+ 
		"ORDER BY aa.object_name";
		
		PreparedStatement pstm = this.getConnecter().getConn().prepareStatement(query);
		ResultSet rset = null;
	 
		pstm.setString(1,getConnecter().getUsername());

		rset = pstm.executeQuery();
		
		// If the object is not present return false
		while(rset.next()) {
			count++;
		}
		
		if( rset != null ){
			rset.close();
		}
		if(pstm != null){
			pstm.close();
		}
		
		
		return count;
	}
	@Override
	public String[] getAllPackagesNames() throws SQLException {
		StringBuffer ret = new StringBuffer();
		if( this.getConnecter() != null ){
			if( this.getConnecter().getConn() != null ){
				Connection conn = this.getConnecter().getConn();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT object_name FROM all_objects "+
		                 "WHERE owner = ? "+
		                 "AND object_type = 'PACKAGE' ORDER BY object_name");
				pstmt.setString(1, getConnecter().getUsername());
				ResultSet rset = pstmt.executeQuery();
				int i = 0;
				while(rset.next()){
					ret.append(rset.getString(1) + ","); 
				}
				if( rset != null ){
					rset.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			}
		}
		 return ret.toString().split(",");
	}
	
	public void createPackage(String packDec, String pacBody) throws SQLException{
		if( this.getConnecter() != null ){
			if( this.getConnecter().getConn() != null ){
				Connection conn = this.getConnecter().getConn();
				PreparedStatement pstmt = conn.prepareStatement(packDec);
				pstmt.executeUpdate();
				System.out.println("Package declaration compiled successful.");
				
				PreparedStatement pstmt2 = conn.prepareStatement(pacBody);
				pstmt2.executeUpdate();
				System.out.println("Package body compiled successfuly");
				
				pstmt.close();
				pstmt2.close();
				
			}
		}
	}
	@Override
	public boolean isPrimitiveType(String param) {
		String[] types = {
				"VARCHAR2",
				"VARCHAR",
				"NVARCHAR2",
				"CHAR",
				"NCHAR",
				"LOB",
				"LONG",
				"BINARY_FLOAT",
				"BINARY_DOUBLE",
				"NUMBER",
				"DATE",
				"CLOB",
				"NCLOB",
				"BLOB",
				"BFILE",
				"RAW",
				"LONG RAW",
				"ROWID",
				"UROWID",
				"TIMESTAMP",
				"FLOAT",
				"DECIMAL",
				"INTEGER",
				"INT",
				"SMALLINT",
				"REAL",
				"NUMERIC",
				"DOUBLE",
				"SYS_REFCURSOR"
				
		};
		for(String s: types){
			if(param.equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
}
