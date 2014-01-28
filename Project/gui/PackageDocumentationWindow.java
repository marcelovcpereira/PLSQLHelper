package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import negocio.Fachada;
import basicas.Package;

public class PackageDocumentationWindow extends JDialog{
	
	private GUI parent;
	
	private JFileChooser dirC;
	private JButton generateB;
	private JButton chooseDirB;
	private JButton cancelB;
	private JLabel dirL;
	private JTextField dirF; 
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTable pacTable;
	private DefaultTableModel pacTableModel;
	private JButton allYesB;
	private JButton allNoB;
	private JLabel titleL;
	private JTextField titleF;
	
	public PackageDocumentationWindow(GUI par){
		super();
		this.parent = par;
		this.initialize();
		this.setVisible(true);
	}

	public GUI getParent(){
		return this.parent;
	}
	
	private void initialize() {
		this.setModal(true);
		this.setSize(360,300);
		this.setLocation(
		        (parent.getX()+parent.getWidth()/2)-
		        this.getWidth()/2,
		        (parent.getY()+parent.getHeight()/2)-
		        this.getHeight()/2);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Generate Package Documentation");
		
		this.dirL = new JLabel("Choose Output Directory:");
		this.dirF = new JTextField();
		
		this.titleL = new JLabel("Project Title:");
		this.titleF = new JTextField();
		
		this.generateB = new JButton("Generate");
		this.chooseDirB = new JButton("Choose Dir.");
		this.cancelB = new JButton("Cancel");
		this.allYesB = new JButton("All Yes");
		this.allNoB = new JButton("All No");
		
		this.dirF.setText("/home/mpereira/Desktop/RESULTADO_PLDOC");		
		this.dirF.setBounds(10, 195, 200, 22);
		this.dirL.setBounds(dirF.getX(),dirF.getY()-25,dirF.getWidth(),dirF.getHeight());			
		
		this.chooseDirB.setBounds(dirF.getX()+dirF.getWidth()+15, dirF.getY(), 120, 22);
		chooseDirBListener();
		
		this.titleL.setBounds(10,10,100,22);
		this.titleF.setBounds(chooseDirB.getX(), titleL.getY(), 120, 22);
		
		
		
		
		this.cancelB.setBounds(52,chooseDirB.getY()+45,chooseDirB.getWidth(),chooseDirB.getHeight());
		cancelBListener();
		
		this.generateB.setBounds(cancelB.getX()+cancelB.getWidth()+25,cancelB.getY(),cancelB.getWidth(),cancelB.getHeight());
		generateBListener();
		
		
		getPacTable();
		
		this.allYesB.setBounds(chooseDirB.getX(),scrollPane.getY(),chooseDirB.getWidth(),chooseDirB.getHeight());
		allYesListener();
		
		this.allNoB.setBounds(chooseDirB.getX(),scrollPane.getY()+35,chooseDirB.getWidth(),chooseDirB.getHeight());
		allNoListener();
		
		this.panel = new JPanel(null);
		this.panel.add(dirL);
		this.panel.add(dirF);
		this.panel.add(generateB);
		this.panel.add(chooseDirB);
		this.panel.add(cancelB);
		this.panel.add(scrollPane);
		this.panel.add(allNoB);
		this.panel.add(allYesB);
		this.panel.add(titleL);
		this.panel.add(titleF);
		this.setContentPane(panel);
	}
	
	private void setAllTable(boolean b) {
		String yesNo = (b)?"Yes":"No";
		
		Vector columns = new Vector(pacTableModel.getColumnCount());
		for(int i = 0; i < pacTableModel.getColumnCount(); i++){
			columns.addElement(pacTableModel.getColumnName(i));
		}
		
		Vector v = pacTableModel.getDataVector();
		for(int i = 0; i < v.size(); i++){
			Vector v1 = (Vector) v.elementAt(i);
			v1.setElementAt(yesNo, 1);
		}
		
		this.pacTableModel.setDataVector(v, columns);
	}
	
	private void allNoListener() {
		this.allNoB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e) {
						setAllTable(false);
					}					
				}
		);
		
	}

	private void allYesListener() {
		this.allYesB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e) {
						setAllTable(true);
					}
				}
		);
	}

	public void getPacTable(){
		String[] pacNames = getAllPackageNames();
		String[][] data = new String[pacNames.length][2];
		for(int i = 0; i < pacNames.length; i++){
			data[i][0] = pacNames[i];
			data[i][1] = "No";
		}
		
		String[] columns = new String[]{"Package Name","Generate"};
		pacTableModel = new DefaultTableModel(data,columns);
		
		this.pacTable = new JTable(pacTableModel){
			public boolean isCellEditable(int rowIndex, int colIndex) {
		        return false;   //Disallow the editing of any cell
		      }
		};
		scrollPane = new JScrollPane(pacTable); 
		scrollPane.setBounds(10, 55, 200, 100);
		
		pacTable.addMouseListener(
			new MouseAdapter(){
		     public void mouseClicked(MouseEvent e){
		    	 int row = pacTable.getSelectedRow();
		    	 changeRowValue(row);
		      }			
			} 
		);

	}
	
	private void changeRowValue(int row) {
		String s = (String) pacTableModel.getValueAt(row, 1);
		if( s.equalsIgnoreCase("Yes")){
			pacTableModel.setValueAt("No", row, 1);
		}else{
			pacTableModel.setValueAt("Yes", row, 1);	
		}
	}
	
	private void cancelBListener(){
		this.cancelB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e) {
						PackageDocumentationWindow.this.dispose();
					}
				}
		);
	}

	private void chooseDirBListener(){
		this.chooseDirB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e) {
						dirC = new JFileChooser();
						dirC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						dirC.setAcceptAllFileFilterUsed(false);						
						dirC.setCurrentDirectory(new java.io.File(dirF.getText()));
						dirC.setDialogTitle("Browse");
					        
					    if (dirC.showOpenDialog(PackageDocumentationWindow.this) == JFileChooser.APPROVE_OPTION) { 
					    	dirF.setText(dirC.getSelectedFile().toString());
					    }
					}
				}	
			);
	}

	private void generateBListener() {
		this.generateB.addActionListener(
				new ActionListener(){				
					public void actionPerformed(ActionEvent e) {
						String output = dirF.getText();
						String projName = titleF.getText();
						if(projName == null || projName.trim().equalsIgnoreCase("")){
							String user = parent.getFachada().getDriver().getUsername();
							String sid = parent.getFachada().getDriver().getSid();
							projName = new String( user + "@" + sid );
							
						}
						String[] packs = getYesPackages();
						
						if(packs != null){							
								Package[] packages = new Package[packs.length];
								for(int i = 0; i < packs.length; i++){
									packages[i] = new Package();
									packages[i].setName(packs[i]);
								}

								Fachada f = PackageDocumentationWindow.this.getParent().getFachada();

								try {
									f.getDocumenter().generateDefaultPackageDocumentation(packages, projName, output);
								} catch (IOException e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null,"Errore","Warning",JOptionPane.WARNING_MESSAGE);
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,"Error2","Warning",JOptionPane.WARNING_MESSAGE);
								}

							
							
							JOptionPane.showMessageDialog(null,"Documentation Successfully Generated!","Finished", JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null,"No Package selected","Warning",JOptionPane.WARNING_MESSAGE);
						}
					}
				}
		);
	}
	
	public String[] getYesPackages(){
		Vector<String> v = pacTableModel.getDataVector();
		StringBuffer buff = new StringBuffer();		
		Iterator it = v.iterator();
		int i  = 0;
		while(it.hasNext()){			
			Vector<String> v1 = (Vector<String>)it.next();
			
			if( ((String)v1.elementAt(1)).equalsIgnoreCase("Yes") ){
				if(i != 0){
					if( buff.length() > 0 ){
						buff.append(",");
					}
				}
				buff.append((String)v1.elementAt(0));
			}
			i++;
		}
		String[] ret = null;
		if(!buff.toString().trim().equalsIgnoreCase("")){
			ret = buff.toString().split(",");
		}
		
		return ret; 
	}
	
	
	
	public static void main(String[] args) {
		new GUI().setVisible(true);
	}
	
	public String[] getAllPackageNames(){
		String[] ret = null;
		try {
			ret = this.parent.getFachada().getAllPackagesNames();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Error obtaining package names.\nPossible a connection problem","Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		return ret;
	}

}
