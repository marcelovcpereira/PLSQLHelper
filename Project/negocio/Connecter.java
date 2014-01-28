package negocio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.management.InvalidAttributeValueException;

import exceptions.MySqlDriverNotFoundException;
import exceptions.OracleDriverNotFoundException;
import exceptions.SqlServerDriverNotFoundException;

public class Connecter {

	private String url;
	private String username;
	private String password;
	private int port;
	private Dbms database;
	private String ip;
	private String sid;
	private Connection conn;
	
	
	/**
	 * Available Database Management Systems
	 * */
	public enum Dbms{
		ORACLE,MYSQL,SQLSERVER
	}
	
	public static final Dbms[] availableBDs = {Dbms.ORACLE};

	
	public String getIp(){
		return this.ip;
	}
	
	public void setIp(String s){
		this.ip = s;
	}
	
	private void updateUrl() {
		//jdbc:oracle:thin:@192.168.179.12:1521:desenv1
		this.url = "jdbc:";
		this.url += getDBString();
		this.url += getIp();
		
		if( this.database == Dbms.ORACLE ){
			this.url += ":" + getPort() + ":" + getSid();
		}else if(this.database == Dbms.MYSQL){
			this.url += "/" + getSid();
		}else if( this.database == Dbms.SQLSERVER ){
			this.url += ":" + getPort();
		}
		
		
	}
	
	private void setDatabaseDriver() throws OracleDriverNotFoundException, MySqlDriverNotFoundException, SqlServerDriverNotFoundException{
		switch(this.database){
			case ORACLE:
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
				} catch (ClassNotFoundException e) {
					throw new OracleDriverNotFoundException();
				}
			break;	
			
			case MYSQL:
				try {
					 Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					throw new MySqlDriverNotFoundException();
				}
			break;
			
			case SQLSERVER:
				try {
					 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				} catch (ClassNotFoundException e) {
					throw new SqlServerDriverNotFoundException();
				}
			break;
		}
		
		
	}
	
	public void initConnection() throws SQLException{
		if( conn != null ){
			conn.close();
			conn = null;
		}
		this.conn = DriverManager.getConnection(this.url,this.username, this.password);		
	}
	
	
	
	
	public void disconnect() throws SQLException{
		this.conn.close();
	}
	
	public Connection getConn(){
		return this.conn;
	}
	
	public boolean isConnected() throws SQLException{
		boolean ret = false;
		if( this.conn != null ){
			if( !conn.isClosed() ){
				ret = true;
			}
		}
		return ret;
	}

	private String getDBString() {
		String ret = "";
		if(this.database == Dbms.ORACLE){
			ret = "oracle:thin:@";
		}else if(this.database == Dbms.MYSQL){
			ret = "mysql://";
		}else if(this.database == Dbms.SQLSERVER){
			ret = "microsoft:sqlserver://";
		}
		return ret;
	}

	public Connecter(){
		
	}

	public String getUrl() {
		updateUrl();
		return url;
	}

	private void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username.toUpperCase();
	}

	public void setUsername(String username) {
		this.username = username;
		updateUrl();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		updateUrl();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		updateUrl();
	}

	public Dbms getDatabase() {
		return database;
	}

	public void setDatabase(Dbms database) throws InvalidAttributeValueException {
		if( database == Dbms.ORACLE ||
			database == Dbms.MYSQL  ||
			//database == POSTGRESQL ||
			database == Dbms.SQLSERVER 
			//database == FIREBIRD
			){
			this.database = database;
		}else{
			throw new InvalidAttributeValueException();
		}
		updateUrl();
		
	}
	
	public void setDatabase(String s) throws InvalidAttributeValueException{
		if( s.equalsIgnoreCase("oracle")){
			this.database = Dbms.ORACLE;
		}else if( s.equalsIgnoreCase("mysql")){
			this.database = Dbms.MYSQL;
		}else if( s.equalsIgnoreCase("sqlserver") || 
				  s.equalsIgnoreCase("mssqlserver") ||
				  s.equalsIgnoreCase("microsoft") ||
				  s.equalsIgnoreCase("msserver")){
			this.database = Dbms.SQLSERVER;
		}else{
			throw new InvalidAttributeValueException();
		}
		updateUrl();
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
		updateUrl();
	}
	
	
	
}
