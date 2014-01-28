package drivers;

import java.sql.SQLException;

import negocio.Connecter;
import basicas.Routine;
import basicas.Package;

public abstract class DBMSDriver {
	
	private Connecter connecter;	
	
	
	public Connecter getConnecter() {
		return connecter;
	}
	public void setConnecter(Connecter conn) {
		this.connecter = conn;
	}
	public abstract Routine[] getRoutinesFromPackage(String packageName) throws SQLException;
	public abstract String getRoutineSource(Routine routine) throws SQLException;
	public abstract int getRoutinesCount() throws SQLException;
	public abstract String[] getAllPackagesNames() throws SQLException;
	public abstract String[] getPackageSource(Package pack) throws SQLException;
	public abstract void createPackage(String packDec, String pacBody) throws SQLException;
	public abstract boolean isPrimitiveType(String s); 
	
	public boolean isConnected() throws SQLException {
		if( this.connecter != null ){
			return this.connecter.isConnected();
		}else{
			return false;
		}
	}
	public void disconnect() throws SQLException {
		this.connecter.disconnect();
	}
	public void connect(Connecter c) throws SQLException {
		this.connecter = c;
		this.connecter.initConnection();
	}
	
	public String getUsername(){
		return this.connecter.getUsername();
	}
	
	public String getSid(){
		return this.connecter.getSid();
	}
	
	public String getIp(){
		return this.connecter.getIp();
	}
	
	public String getPassword(){
		return this.connecter.getPassword();
	}
	
	public String getUrl(){
		return this.connecter.getUrl();
	}

}
