package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.SQLException;
import java.util.EventListener;

import javax.management.InvalidAttributeValueException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import negocio.Connecter;
import negocio.Fachada;

public class ConnectWindow extends JDialog{
	
	private GUI parent;
	
	private JPanel painel;	
	private JLabel userL;
	private JLabel passL;
	private JLabel portL;
	private JLabel ipL;
	//private JLabel urlL;
	private JLabel sidL;
	private JLabel dbL;
	
	private JTextField userF;
	private JPasswordField passF;
	private JTextField portF;
	private JTextField ipF;
	private JTextField sidF;	
	
	private JButton connectB;
	private JButton cancelB;
	private JComboBox databaseCombo;
	
	private String url;
	
	/**
	 * Height of the fields (in pixels)
	 * */
	public static final int fieldH = 22;
	
	/**
	 * Separation between label and it's field (vertical separation)
	 * */
	public static final int labelSep = 25;
	
	/**
	 * Minimum horizontal position
	 * */
	public static final int hMargin = 10;
	
	/**
	 * Horizontal separation between fields
	 */
	 public static final int hSep = 25;
	 
	 /**
	  * Vertical separation between fields
	  * 
	  * */
	 public static final int vSep = 65;
	
	public ConnectWindow(GUI par){
		super();
		this.parent = par;
		this.initialize();
		this.setVisible(true);
		
	}
	
	private void initialize(){
		this.setModal(true);
		this.setSize(380,280);
		this.setLocation(
				        (parent.getX()+parent.getWidth()/2)-
				        this.getWidth()/2,
				        (parent.getY()+parent.getHeight()/2)-
				        this.getHeight()/2);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Connect");
		
		
		//Labels
		this.passL = new JLabel("Password:");
		this.userL = new JLabel("Username:");
		this.portL = new JLabel("Port:");
		this.ipL = new JLabel("Hostname/Ip:");
		this.sidL = new JLabel("SID:");
		this.dbL = new JLabel("Database Management System:");
		//this.urlL = new JLabel("");
		
		
		//Fields
		this.passF = new JPasswordField();
		this.passF.setText("");
		this.userF = new JTextField();
		this.userF.setText("");
		this.portF = new JTextField();
		this.portF.setText("1521");
		this.ipF = new JTextField();
		this.ipF.setText("localhost");
		
		this.sidF = new JTextField();
		this.sidF.setText("orcl");
		
			
		
		//Buttons		
		this.connectB = new JButton("Connect");
		this.cancelB = new JButton("Cancel");
		
		
		Connecter.Dbms[] values = Connecter.availableBDs;
		String[] comboValues = new String[values.length];
		for(int i = 0; i < values.length; i++){
			comboValues[i] = values[i].toString();
		}
		this.databaseCombo = new JComboBox(comboValues);
		
		
		//Positions		
		this.dbL.setBounds(hMargin,10,200,fieldH);
		this.databaseCombo.setBounds(dbL.getX()+dbL.getWidth()+10, dbL.getY(), 145, fieldH);
		
		this.userF.setBounds(hMargin, dbL.getY()+vSep, 165, fieldH);
		this.userL.setBounds(userF.getX(), userF.getY()-labelSep, userF.getWidth(), userF.getHeight());
		
		this.passF.setBounds(userF.getX()+userF.getWidth()+hSep, userF.getY(), 165, fieldH);
		this.passL.setBounds(passF.getX(),passF.getY()-labelSep,passF.getWidth(),passF.getHeight());		
		
		this.portF.setBounds(hMargin, userF.getY()+vSep, 95, fieldH);
		this.portL.setBounds(portF.getX(),portF.getY()-labelSep,portF.getWidth(),portF.getHeight());		
		
		this.sidF.setBounds(portF.getX()+portF.getWidth()+hSep, portF.getY(), 95, fieldH);
		this.sidL.setBounds(sidF.getX(),sidF.getY()-labelSep,sidF.getWidth(),sidF.getHeight());
		
		this.ipF.setBounds(sidF.getX()+sidF.getWidth()+hSep, portF.getY(), 115, fieldH);
		this.ipL.setBounds(ipF.getX(),ipF.getY()-labelSep,ipF.getWidth(),ipF.getHeight());
		
		
		//this.urlL.setBounds(hMargin,140,350,fieldH);		
		
		this.cancelB.setBounds(52,portF.getY()+vSep,125,fieldH);
		cancelBListener();
		
		this.connectB.setBounds(cancelB.getX()+cancelB.getWidth()+hSep, cancelB.getY(),125, fieldH);		
		connectBListener();
		
		this.painel = new JPanel(null);
		this.painel.add(passF);
		this.painel.add(passL);
		this.painel.add(userF);
		this.painel.add(userL);
		this.painel.add(portF);
		this.painel.add(portL);
		this.painel.add(ipF);
		this.painel.add(ipL);
		this.painel.add(sidF);
		this.painel.add(sidL);
		//this.painel.add(generateB);
		//this.painel.add(urlL);
		this.painel.add(databaseCombo);
		this.painel.add(dbL);
		this.painel.add(connectB);
		this.painel.add(cancelB);
		//this.painel.add(dirF);
		//this.painel.add(dirL);
		//this.painel.add(chooseDirB);
		this.setContentPane(this.painel);
		//this.updateURL();
	}
	
	private void cancelBListener() {
		this.cancelB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e)  {
						ConnectWindow.this.dispose();
					}

					
				}
		);
		
	}

	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String s){
		this.url = s;
	}
	
	/*private void updateURL(){
		//this.updateParentConnecter();
		if(this.parent.getConnecter() != null){
			this.urlL.setText(this.parent.getConnecter().getUrl());
		}
				
	}*/
	
	private String parseDB(String s){
		String ret = "";
		if(s.equalsIgnoreCase("oracle")){
			ret = "oracle";
		}else{
			ret = "mysql";
		}
		return ret;
	}
	
	private Connecter getConnecterFromFields(){
		Connecter c = new Connecter();;
		try {			
			c.setPort(Integer.parseInt(portF.getText()));							
			c.setIp(ipF.getText());
			c.setUsername(userF.getText());
			c.setPassword(new String(passF.getPassword()));							
			c.setDatabase((String)databaseCombo.getSelectedItem());
			c.setSid(sidF.getText());
		} catch (InvalidAttributeValueException e1) {
			JOptionPane.showMessageDialog(null,"Invalid Database Selection","Internal Error",JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException nfe){
			JOptionPane.showMessageDialog(null,"Invalid Port Number","Error",JOptionPane.ERROR_MESSAGE);
		}
		return c;
	}
	
	private boolean connect(){		
		Connecter c = getConnecterFromFields();
		try {
			System.out.println("trying to connect to " + c.getUsername() + "->" + c.getUrl());
			this.parent.connectFachada(c);
			System.out.println("successfully connected!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"The connection could not be established","Connection Error",JOptionPane.ERROR_MESSAGE);
			System.out.println("could not connect to the specified parameters");		
			return false;
		}
		
		return true;
	}
	
	private boolean verifyFields() {
		if( ipF.getText() == null || 
			ipF.getText().trim().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"Empty Ip Address","Field Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(	sidF.getText() == null || 
			sidF.getText().trim().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"Empty SID","Field Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if(	userF.getText() == null || 
			userF.getText().trim().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"Empty Username","Field Error",JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		
		if(	portF.getText() == null ||
			portF.getText().trim().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"Empty Port Number","Field Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}else{		
			try{
				int port = Integer.parseInt(portF.getText());
			}catch( NumberFormatException nfe ){
				JOptionPane.showMessageDialog(null,"Invalid Port Number","Field Error",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		String s = new String(passF.getPassword());
		if(s == null || s.trim().equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null,"Empty Password","Field Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private void connectBListener() {
		this.connectB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e)  {
						if( verifyFields() ){
							if ( connect() ){
								ConnectWindow.this.dispose();
								JOptionPane.showMessageDialog(null, "Connection established successfully!", "Connected",JOptionPane.INFORMATION_MESSAGE);
							}
														
						}
					}

					
				}
		);
		
		
	}
	
	
	
	

}
