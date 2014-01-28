package basicas;

import java.util.Vector;

public class Package {

	private String name;
	private String owner;
	private Routine[] routines;
	private String body;
	private String declaration;
	
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public String getSource() {
		return body;
	}
	public void setSource(String source) {
		this.body = source;
	}
	public String getName() {
		return name;
	}
	public String getOwner() {
		return owner;
	}
	public Routine[] getRoutines() {
		return routines;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setRoutines(Routine[] routines) {
		this.routines = routines;
	}
	public String toString(){
		return this.getName();
	}
	
	
	public String generatePackageDeclaration(){
		String head = "CREATE OR REPLACE PACKAGE " + this.getName();
		head += " IS\n";
		
		String body = "";
		for(int i = 0; i < this.getRoutines().length; i++){
			Routine temp = this.getRoutines()[i];
			body += "\n" + temp.getType().toString() + " " + temp.getName();
			body += "(" + temp.getParameters() + ")" + temp.getReturnDeclaration() + ";\n";
		}
		
		String end = "\nEND " + this.getName() + ";";
		
		return head + body + end;
	}
}
