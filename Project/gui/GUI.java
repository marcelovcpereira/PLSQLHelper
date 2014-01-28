package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import drivers.DBMSDriver;

import negocio.Connecter;
import negocio.Fachada;

public class GUI extends JFrame{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int default_height = 38;

	private Fachada fachada;
	
	
	
	//GUI Components
	private JPanel panel;
	private JMenuBar bar;
	
	private JMenu menuSystem;
		private JMenuItem itemConnect;
		private JMenuItem itemDisconnect;
		private JMenuItem itemExit;
		
	private JMenu menuHelp;
		private JMenuItem itemAbout;
		
	private JMenu menuDocumentation;
		private JMenuItem itemPackageDoc;
		private JMenuItem itemStandaloneDoc;
		private JMenuItem itemCompleteDoc;
		
	private JMenu menuAnalysis;
		private JMenuItem itemListPackageRoutines;
		private JMenuItem itemStandaloneRoutines;
		private JMenuItem itemMetrics;
		private JMenuItem itemRelationModel;
		
	private JMenu menuRefactoring;
		private JMenuItem itemFacadeCreation;
		//private JMenuItem itemSingletonCreator;
		private JMenuItem itemRenameComponent;
		
		
	private JMenu menuTest;
		private JMenuItem itemUnitTest;
		private JMenuItem itemPackageTest;

	public GUI(){
		this.fachada = new Fachada();
		this.initializeFrame();
		this.initializeComponents();		
	}
	
	private void initializeFrame(){
		this.setSize(800,600);
		this.setTitle("Prototype");
		this.setResizable(false);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setLocation(((int)d.getWidth()/2)-getWidth()/2, ((int)d.getHeight()/2)-getHeight()/2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void connectFachada(Connecter c) throws SQLException{		
		this.fachada.connect(c);		
	}
	
	private void initializeComponents(){
		this.panel = new JPanel(null);
		this.setContentPane(panel);
		
		this.bar = new JMenuBar();
		this.bar.setSize(panel.getWidth(), default_height);			
		this.setJMenuBar(bar);
		
		this.menuSystem = new JMenu("System");	
		this.menuSystem.setMnemonic(KeyEvent.VK_S);
		this.bar.add(menuSystem);
			this.itemConnect = new JMenuItem("Connect");
			this.itemConnect.setMnemonic(KeyEvent.VK_C);
			this.createConnectListener();
			this.itemDisconnect = new JMenuItem("Disconnect");
			this.itemDisconnect.setMnemonic(KeyEvent.VK_I);
			this.itemDisconnect.setEnabled(false);
			this.createDisconnectListener();
			this.itemExit = new JMenuItem("Exit");
			this.itemExit.setMnemonic(KeyEvent.VK_E);
			this.createExitListener();
		this.menuSystem.add(itemConnect);
		this.menuSystem.add(itemDisconnect);
		this.menuSystem.add(itemExit);
		
		this.menuDocumentation = new JMenu("Documentation");
		this.menuDocumentation.setMnemonic(KeyEvent.VK_D);
			this.itemPackageDoc = new JMenuItem("Generate Package Documentation");
			this.itemPackageDoc.setMnemonic(KeyEvent.VK_G);
			this.createPackageDocListener();
			
			this.itemStandaloneDoc = new JMenuItem("Standalone Documentation");
			this.itemStandaloneDoc.setMnemonic(KeyEvent.VK_O);
			this.itemStandaloneDoc.setEnabled(false);
			
			this.itemCompleteDoc = new JMenuItem("Complete User Documentation");
			this.itemCompleteDoc.setMnemonic(KeyEvent.VK_C);
			this.itemCompleteDoc.setEnabled(false);
		this.menuDocumentation.add(itemPackageDoc);
		this.menuDocumentation.add(itemStandaloneDoc);
		this.menuDocumentation.add(itemCompleteDoc);
		this.bar.add(menuDocumentation);
		
		this.menuAnalysis = new JMenu("Analysis");
		this.menuAnalysis.setMnemonic(KeyEvent.VK_A);
			this.itemListPackageRoutines = new JMenuItem("Package Routines Analyser");
			this.itemListPackageRoutines.setMnemonic(KeyEvent.VK_R);
			this.createListPackageRoutinesListener();
			
			this.itemStandaloneRoutines = new JMenuItem("Standalone Routines Analyser");
			this.itemStandaloneRoutines.setMnemonic(KeyEvent.VK_O);			
			this.itemStandaloneRoutines.setEnabled(false);
			
			this.itemMetrics = new JMenuItem("Metrics");
			this.itemMetrics.setMnemonic(KeyEvent.VK_M);
			this.itemMetrics.setEnabled(false);
			
			this.itemRelationModel = new JMenuItem("Relational Model Diagram");
			this.itemRelationModel.setMnemonic(KeyEvent.VK_I);
			this.itemRelationModel.setEnabled(false);
		this.menuAnalysis.add(itemListPackageRoutines);
		this.menuAnalysis.add(itemStandaloneRoutines);
		this.menuAnalysis.add(itemMetrics);
		this.menuAnalysis.add(itemRelationModel);
		this.bar.add(menuAnalysis);
		
		this.menuRefactoring = new JMenu("Refactoring");
		this.menuRefactoring.setMnemonic(KeyEvent.VK_R);
			this.itemFacadeCreation = new JMenuItem("Facade Creation");
			this.itemFacadeCreation.setMnemonic(KeyEvent.VK_F);
			this.itemFacadeCreation.setEnabled(true);
			this.createFacadeCreatioinListener();
			
			//this.itemSingletonCreator = new JMenuItem("Singleton Creation");
			//this.itemSingletonCreator.setMnemonic(KeyEvent.VK_G);
			//this.itemSingletonCreator.setEnabled(false);
			
			this.itemRenameComponent = new JMenuItem("Rename Component");
			this.itemRenameComponent.setMnemonic(KeyEvent.VK_N);
			this.itemRenameComponent.setEnabled(false);
		this.menuRefactoring.add(itemFacadeCreation);
		//this.menuRefactoring.add(itemSingletonCreator);
		this.menuRefactoring.add(itemRenameComponent);
		this.bar.add(menuRefactoring);
		
		this.menuTest = new JMenu("Test");
		this.menuTest.setMnemonic(KeyEvent.VK_T);
			this.itemUnitTest = new JMenuItem("Routine Unit Test");
			this.itemUnitTest.setMnemonic(KeyEvent.VK_U);
			this.itemUnitTest.setEnabled(false);
			
			this.itemPackageTest = new JMenuItem("Package Test");
			this.itemPackageTest.setMnemonic(KeyEvent.VK_P);
			this.itemPackageTest.setEnabled(false);
		this.menuTest.add(itemUnitTest);
		this.menuTest.add(itemPackageTest);
		this.bar.add(menuTest);
		
		this.menuHelp = new JMenu("Help");
		this.menuHelp.setMnemonic(KeyEvent.VK_H);
			this.itemAbout = new JMenuItem("About");
			this.itemAbout.setMnemonic(KeyEvent.VK_B);
			this.createAboutListener();
		this.menuHelp.add(itemAbout);
		this.bar.add(menuHelp);
		
		setEditableAll(false);
	}	
	
	
	private void createFacadeCreatioinListener() {
		this.itemFacadeCreation.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						
							new FacadeWindow(GUI.this);
						
					}
				}
		);
	}

	private void createListPackageRoutinesListener() {
		this.itemListPackageRoutines.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						
							new AnalysisWindow(GUI.this);
						
					}
				}
		);
		
	}

	private void createDisconnectListener() {
		this.itemDisconnect.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						disconnect();
					}
				}
		);
		
	}

	private void createAboutListener() {
		// TODO Auto-generated method stub
		
	}

	private void createPackageDocListener() {
		this.itemPackageDoc.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						new PackageDocumentationWindow(GUI.this);
					}
				}
		);
		
	}

	public DBMSDriver getDriver(){
		if( this.fachada != null ){
			return this.fachada.getDriver();
		}else{
			return null;
		}
	}

	private void createExitListener() {
		this.itemExit.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						System.exit(0);
					}
				}
		);
		
	}
	
	private void setEditableAll(boolean b){
		this.menuDocumentation.setEnabled(b);	
		this.menuAnalysis.setEnabled(b);
		this.menuTest.setEnabled(b);
		this.menuRefactoring.setEnabled(b);
	}
	
	public void showConnectWindow(){
		new ConnectWindow(this);		
			
		try {
			if( this.fachada.isConnected() ){				
				setEditableAll(true);
				this.itemConnect.setEnabled(false);
				this.itemDisconnect.setEnabled(true);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Error trying to verify the connection", "Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void createConnectListener() {
		this.itemConnect.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						showConnectWindow();
					}
				}
		);
	}
	
	public void disconnect(){	
		try {
			this.fachada.disconnect();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Error on trying to disconnect","Connection error",JOptionPane.ERROR_MESSAGE);
		}
		setEditableAll(false);
		this.itemConnect.setEnabled(true);
		this.itemDisconnect.setEnabled(false);
	}

	public Fachada getFachada() {
		return fachada;
	}

	public void setFachada(Fachada fachada) {
		this.fachada = fachada;
	}
	
	
}
