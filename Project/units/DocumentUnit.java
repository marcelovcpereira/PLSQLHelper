package units;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sourceforge.pldoc.PLDoc;
import net.sourceforge.pldoc.Settings;
import basicas.Package;
import drivers.DBMSDriver;

public class DocumentUnit{

	private DBMSDriver driver;
	
	//call pldoc.bat -doctitle 'Sample Application' -overview samples/overview1.html -d SampleApplicationDoc samples/sample*.sql
	public DocumentUnit(DBMSDriver d) {
		this.setDriver(d);
	}
	
	/*public void generatePackageDocumentation(String packName,String projName,String outputPath){
		plDocPackageDocumentation(packName,projName, outputPath);
	}*/
	
	public void generateDefaultPackageDocumentation(Package[] pack,String projName,String outputPath) throws IOException, SQLException{
		long startTime = System.currentTimeMillis();
		
		String[] names = new String[pack.length]; 
		//creating real files for package sources
		for(int i = 0; i < pack.length; i++){			
			String nameOfFile =  outputPath+"/"+projName+(i+1)+".sql";
			names[i] = new String(nameOfFile);
			File file = new File(nameOfFile);
			FileWriter fw = new FileWriter(file);
			for(String s: this.driver.getPackageSource(pack[i])){
				fw.write(s);
			}
			fw.flush();
			fw.close();
			System.out.println("file "+nameOfFile + " created.");
		}

		String arguments = "";	
		arguments += "-doctitle \'" + projName + "\' ";
		arguments += "-d " + outputPath.replaceAll(" ", "%20") + " ";
		arguments += outputPath+"/"+projName+"*.sql";
		String[] args = arguments.split(" ");
		Settings sets = new Settings();
		try {
			sets.processCommandString(args);
			ArrayList ifiles = new ArrayList();
			for(String s: names){
				ifiles.add(s);
			}
			sets.setInputFiles(ifiles);
		} catch (Exception e2) {
			System.out.println("ERROR ON PROCESS COMMAND @ Settings.java");
			e2.printStackTrace();
		}
		PLDoc p = new PLDoc(sets);
		try {
			p.run();
			long finishTime = System.currentTimeMillis();
		    System.out.println("Done (" + (finishTime-startTime)/1000.00 + " seconds).");
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		
		for(String name: names){
			File f = new  File(name);
			f.delete();
		}
	}
	
	/*public void plDocPackageDocumentation(String packName,String projName,String outputPath){
		long startTime = System.currentTimeMillis();
		String arguments = "";	
		arguments += "-doctitle \'" + projName + "\' ";
		arguments += "-url " + this.getDriver().getUrl() + " ";
		arguments += "-user " + this.getDriver().getUsername().toUpperCase() + " ";
		arguments += "-password " + this.getDriver().getPassword() + " ";
		arguments += "-sql " + packName + " ";
		arguments += "-d " + outputPath.replaceAll(" ", "%20");
		String[] args = arguments.split(" ");
		Settings sets = new Settings();
		try {
			sets.processCommandString(args);
		} catch (Exception e2) {
			System.out.println("ERROR ON PROCESS COMMAND @ Settings.java");
			e2.printStackTrace();
		}
		PLDoc p = new PLDoc(sets);
		try {
			p.run();
			long finishTime = System.currentTimeMillis();
		    System.out.println("Done (" + (finishTime-startTime)/1000.00 + " seconds).");
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		
	}*/
	
	/*public void generateAllPackagesDocumentation(String projName,String outputPath){
		plDocPackageDocumentation("%",projName, outputPath);
	}*/


	public DBMSDriver getDriver() {
		return driver;
	}

	public void setDriver(DBMSDriver driver) {
		this.driver = driver;
	}

}
