package units;

import java.sql.SQLException;
import java.util.Vector;

import basicas.Package;
import basicas.Routine;
import drivers.DBMSDriver;

public class RefactoringUnit{

private DBMSDriver driver;
	
	public RefactoringUnit(DBMSDriver d){
		this.driver = d;
	}
	
	public Package generateFacadePackage(Package[] packs){
		Package facade = new Package();
		String name = "PKG_Facade";
		String head = "CREATE OR REPLACE PACKAGE BODY " + name +	" IS\n";		
		String body = "";
		String end = "BEGIN\nNULL;\nEND "+ name +";";
		Vector<Routine> routinesV = new Vector<Routine>();
		
		String headDec = "CREATE OR REPLACE PACKAGE " + name + " IS\n";
		String bodyDec = "";
		String endDec = "\nEND " + name + ";";
		for( int i = 0; i < packs.length; i++ ){
			Package pTemp = packs[i];
			
			for( int j = 0; j < pTemp.getRoutines().length; j++ ){
				Routine rTemp = pTemp.getRoutines()[j];
				routinesV.add(rTemp);
				String typeNameParams = "\n" + rTemp.getType().toString() + " ";
				typeNameParams += rTemp.getName() + "(" + rTemp.getParameters() + ")\n ";
				
				body += typeNameParams;
				bodyDec += typeNameParams;				
				
				String retDec = rTemp.getReturnDeclaration();				
				
				if( !retDec.trim().equalsIgnoreCase("")){
					String[] ret = retDec.split(" ");
					if( this.driver.isPrimitiveType(ret[1]) ){
						retDec = ret[0] + " " + ret[1];
					}else{
						retDec = ret[0] + " " + rTemp.getPackageName() + "." + ret[1];
					}
				}
				
				body += retDec;
				bodyDec += retDec + ";\n\n";
				
				body +=  "\nIS\nBEGIN\n";
				
				if( rTemp.getType() == Routine.Types.FUNCTION ){
					body += "\t return ";
				}
				body += rTemp.getPackageName() + "." + rTemp.getName();
				body += "(" + rTemp.getParams() + ");\nEND "+rTemp.getName() + ";\n\n";
			}
		}
		String packageSource = head + body + end;
		String packageDeclaration = headDec + bodyDec + endDec;
		facade.setSource(packageSource);
		facade.setDeclaration(packageDeclaration);
		facade.setName(name);
		facade.setOwner(driver.getUsername());
		
		Routine[] routines = new Routine[routinesV.size()];
		for(int w = 0; w < routinesV.size(); w++){
			routines[w] = new Routine();
			routines[w].setName(routinesV.elementAt(w).getName());
			routines[w].setPackageName(name);
			routines[w].setSource(routinesV.elementAt(w).getSource());
			routines[w].setType(routinesV.elementAt(w).getType());			
		}
		facade.setRoutines(routines);
		return facade;
	}
	
	public void saveFacadePackage( Package fac ) throws SQLException{
		String packDec = fac.getDeclaration();
		String packBody = fac.getSource();		
		this.driver.createPackage(packDec, packBody);
	}
	
	

}
