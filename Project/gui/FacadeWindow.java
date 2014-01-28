package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import basicas.Package;
import basicas.Routine;

public class FacadeWindow extends JDialog{
	
	private GUI parent;
	private JPanel panel;
	public int loadPercentage;
	private Package[] packages;
	
	private JLabel packsL;
	private JList packsList;
	private JScrollPane packsS;
	
	private JTextArea sourceArea;
	private JScrollPane areaS;
	
	private JButton createB;
	private JButton cancelB;
	private JButton saveB;
	
	private Package facade;
	
	public FacadeWindow(GUI par){
		this.parent = par;
		this.setTitle("Facade Generator");
		this.setSize(685, 520);
		this.setResizable(false);
		this.setLocation(
		        (parent.getX()+parent.getWidth()/2)-
		        this.getWidth()/2,
		        (parent.getY()+parent.getHeight()/2)-
		        this.getHeight()/2);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		final LoadingSplash ls = new LoadingSplash(this);	

		SwingWorker s = new SwingWorker(){
			@Override
			protected Object doInBackground() throws Exception {
				loadData(ls);
				return null;
			}			
			

			@Override
			protected void done() {
				// TODO Auto-generated method stub
				super.done();
				ls.dispose();
			}
			
		};
		
		s.execute();
		ls.setVisible(true);

		initialize();
		ls.dispose();
		this.setVisible(true);
	}

	private void initialize() {
		this.packsL = new JLabel("User Packages:");
		this.packsL.setBounds(10,10,100,30);
		
		this.packsList = new JList(packages); 
		this.packsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.packsList.setEnabled(true);
		
		this.packsS = new JScrollPane(packsList);		
		this.packsS.setBounds(packsL.getX(), packsL.getY()+25, 150, 100);
		
		
		this.sourceArea = new JTextArea();
		this.sourceArea.setEditable(false);
		this.areaS = new JScrollPane(sourceArea);
		this.areaS.setBounds(packsS.getX()+packsS.getWidth()+15, 10, 500, 480);	
		
		
		this.createB = new JButton("Create Facade");
		this.createB.setBounds(10,packsS.getY()+packsS.getHeight()+25,150,22);
		createBListener();
		
		this.saveB = new JButton("Save Facade");
		this.saveB.setBounds(createB.getX(),createB.getY()+createB.getHeight()+8,createB.getWidth(),createB.getHeight());
		this.saveB.setEnabled(false);
		saveBListener();
		
		this.cancelB = new JButton("Cancel");
		this.cancelB.setBounds(createB.getX(),saveB.getY()+saveB.getHeight()+8,createB.getWidth(),createB.getHeight());
		cancelBListener();
		
		this.panel = new JPanel(null);
		this.panel.add(packsL);
		this.panel.add(packsS);
		this.panel.add(areaS);
		this.panel.add(createB);
		this.panel.add(saveB);
		this.panel.add(cancelB);
		this.setContentPane(panel);
	}

	private void saveBListener() {
		this.saveB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						try {
							parent.getFachada().saveFacadePackage(facade);
							JOptionPane.showMessageDialog(null,"Facade Successfully Created!","Finished", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null,e1.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
		);
		
	}

	private void cancelBListener() {
		this.cancelB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				}
		);
	}

	private void createBListener() {
		this.createB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						facade = parent.getFachada().generateFacadePackage(packages);
						sourceArea.setText(facade.getSource());
						saveB.setEnabled(true);
					}
				}
		);
	}

	public void loadData(LoadingSplash ls) throws SQLException {
		loadPercentage = 0;
		int total = parent.getFachada().getRoutinesCount();
		int routineNumber = 0;
		
		try {
			String[] pkgs = this.parent.getFachada().getAllPackagesNames();
			this.packages = new Package[pkgs.length];
			for(int i = 0; i < pkgs.length; i++){				
				packages[i] = new Package();
				packages[i].setName(pkgs[i]);
				packages[i].setOwner(parent.getFachada().getDriver().getConnecter().getUsername());
				
				Routine[] routs = parent.getFachada().getRoutinesFromPackage(packages[i].getName());
				for(int j = 0; j < routs.length; j++){
					routineNumber++;
					loadPercentage = ((routineNumber)*100)/total;
					ls.bar.setValue(loadPercentage);
					ls.routine.setText(routs[j].getPackageName()+"."+routs[j].getName());
					String s = parent.getFachada().getRoutineSource(routs[j]);
					routs[j].setSource(s);
					
				}
				packages[i].setRoutines(routs);
				
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"LOAD ERROR","ERROR",JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
