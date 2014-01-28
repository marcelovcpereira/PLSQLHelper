package negocio;

import java.sql.SQLException;

import units.AnalysisUnit;
import units.DocumentUnit;
import units.RefactoringUnit;
import units.TestUnit;

import basicas.Package;
import basicas.Routine;
import drivers.DBMSDriver;
import drivers.MySqlDriver;
import drivers.OracleDriver;
import drivers.SqlServerDriver;

public class Fachada {
	
	private DocumentUnit documenter;
	private AnalysisUnit analyser;
	
	private RefactoringUnit refactorer;
	private TestUnit tester;
	
	private DBMSDriver driver;
	
	public enum Sgbd {
		ORACLE,MYSQL,POSTGRESQL,SQLSERVER,FIREBIRD
	}
	public Fachada(){
		this.driver = null;		
	}
	




	private void initialize() {
		this.documenter = new DocumentUnit(this.driver);
		this.analyser = new AnalysisUnit(this.driver);
		this.refactorer = new RefactoringUnit(this.driver);
		//		
	}
	
	public void disconnect() throws SQLException{		
		this.driver.disconnect();
		this.analyser = null;
		this.documenter = null;
		this.refactorer = null;
		this.tester = null;
	}
	
	public void connect(Connecter c) throws SQLException{
		if( this.driver == null ){
			if( c.getDatabase() == Connecter.Dbms.ORACLE){
				this.driver = new OracleDriver();
			}else if(c.getDatabase() == Connecter.Dbms.MYSQL){
				this.driver = new MySqlDriver();
			}else{
				this.driver = new SqlServerDriver();
			}
		}
		this.driver.connect(c);
		this.initialize();
	}
	
	public boolean isConnected() throws SQLException{
		return this.driver.isConnected();		
	}
	
	


	public DocumentUnit getDocumenter() {
		return documenter;
	}

AnalysisUnit getAnalyser() {
		return analyser;
	}


	public RefactoringUnit getRefactorer() {
		return refactorer;
	}


	public TestUnit getTester() {
		return tester;
	}


	public void setDocumenter(DocumentUnit documenter) {
		this.documenter = documenter;
	}


	public void setAnalyser(AnalysisUnit analyser) {
		this.analyser = analyser;
	}


	public void setRefactorer(RefactoringUnit refactorer) {
		this.refactorer = refactorer;
	}


	public void setTester(TestUnit tester) {
		this.tester = tester;
	}
	
	
	
	public String[] getAllPackagesNames() throws SQLException{
		return this.driver.getAllPackagesNames();
	}
	
	public Routine[] getRoutinesFromPackage(String packageName) throws SQLException{
		return this.driver.getRoutinesFromPackage(packageName);
	}
	
	
	public String getRoutineSource(Routine r) throws SQLException{
		return this.driver.getRoutineSource(r);
	}
	
	public int getRoutinesCount() throws SQLException{
		return this.driver.getRoutinesCount();
	}
	
	public Package generateRoutinesDependencies(Package p){
		return this.analyser.generateRoutinesDependencies(p);
	}

	public DBMSDriver getDriver() {
		return driver;
	}

	public void setDriver(DBMSDriver driver) {
		this.driver = driver;
	}
	
	public Package generateFacadePackage(Package[] packs){
		return this.refactorer.generateFacadePackage(packs);
	}
	
	public void saveFacadePackage( Package fac ) throws SQLException{
		this.refactorer.saveFacadePackage(fac);
	}
}
