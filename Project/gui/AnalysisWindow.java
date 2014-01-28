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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import basicas.Package;
import basicas.Routine;

public class AnalysisWindow extends JDialog{

	private GUI parent;
	private JPanel panel;
	
	private JLabel packsL;
	private JList packsList;
	private JScrollPane packS;
	
	private JLabel routsL;
	private JList routsList;
	private JScrollPane routS;
	
	private JTextArea sourceArea;
	private JScrollPane areaS;
	
	private Package[] packages;
	
	private JLabel depL;
	private JButton generateOneB;
	private JButton generateAllB;
	
	private JLabel sourceL;
	
	private JButton cancelB;
	public int loadPercentage;
	
	private JButton editSourceB;
	private JButton saveSourceB;
	private JButton undoB;
	private JButton redoB;
	private JButton formatB;
	
	public AnalysisWindow(GUI p){
		this.parent = p;
		this.setTitle("Analysis Unit");
		this.setSize(640, 500);
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
	
	public void loadData(LoadingSplash ls) throws SQLException{
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
	
	private void routsListListener(){
		this.routsList.addListSelectionListener(new ListSelectionListener() {
		      public void valueChanged(ListSelectionEvent evt) {
		    	  int routine = routsList.getSelectedIndex();
		    	  int pack = packsList.getSelectedIndex();
		    	  if( routine != -1 ){
		    		  String source = packages[pack].getRoutines()[routine].getSource();
			    	  sourceArea.setText(source);
			    	  sourceArea.repaint();  
		    	  }else{
		    		  
		    	  }		    	  
		    	 
			  
		      }
		});
	}
	
	private void packsListListener(){
		this.packsList.addListSelectionListener(new ListSelectionListener() {
		      public void valueChanged(ListSelectionEvent evt) {
		    	int pack = packsList.getSelectedIndex();
		    	Routine[] routs = packages[pack].getRoutines();
		    	routsList.setListData(routs);
				routsList.repaint();		  
		      }
		});
	}
	private void initialize(){
		this.packsL = new JLabel("User Packages:");
		this.packsL.setBounds(10,10,100,30);
		
		this.packsList = new JList();
		this.packsList = new JList(packages); 
		this.packsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.packsList.setEnabled(true);
		packsListListener();
		this.packS = new JScrollPane(packsList);		
		this.packS.setBounds(packsL.getX(), packsL.getY()+25, 150, 100);
		
		this.routsL = new JLabel("Routines:");
		this.routsL.setBounds(packsL.getX()+packsL.getWidth()+80,packsL.getY(),100,30);
		this.routsList = new JList();
		routsListListener();
		this.routS = new JScrollPane(routsList);
		this.routS.setBounds(routsL.getX(),routsL.getY()+25,200,100);
		
		this.sourceArea = new JTextArea();
		this.sourceArea.setEditable(false);
		this.areaS = new JScrollPane(sourceArea);
		this.areaS.setBounds(10, 190, 610, 200);
		
		this.sourceL = new JLabel("Routine Source:");
		this.sourceL.setBounds(areaS.getX(), areaS.getY()-35, 120, 30);
		
		this.editSourceB = new JButton("Edit Source");
		this.editSourceB.setBounds(areaS.getX(),areaS.getY()+areaS.getHeight()+35,110,24);
		this.editSourceB.setEnabled(false);
		this.saveSourceB = new JButton("Save");
		this.saveSourceB.setBounds(editSourceB.getX()+125,editSourceB.getY(),110,24);
		this.saveSourceB.setEnabled(false);
		this.undoB = new JButton("Undo");
		this.undoB.setBounds(saveSourceB.getX()+125,editSourceB.getY(),110,24);
		this.undoB.setEnabled(false);
		this.redoB = new JButton("Redo");
		this.redoB.setBounds(undoB.getX()+125,editSourceB.getY(),110,24);
		this.redoB.setEnabled(false);
		this.formatB = new JButton("Format Text");
		this.formatB.setBounds(redoB.getX()+125,editSourceB.getY(),110,24);
		this.formatB.setEnabled(false);
		
		this.generateOneB = new JButton("Selected Package");
		this.generateOneB.setBounds(routS.getX()+routS.getWidth()+20,routS.getY()+4,210,24);
		generateBListener();
		
		this.depL = new JLabel("Generate Routine Dependency for:");
		this.depL.setBounds(generateOneB.getX(), generateOneB.getY()-25, 220, generateOneB.getHeight());
		
		this.generateAllB = new JButton("All Packages");
		this.generateAllB.setBounds(generateOneB.getX(),generateOneB.getY()+generateOneB.getHeight()+8,generateOneB.getWidth(),generateOneB.getHeight());
		generateAllBListener();
		this.generateAllB.setEnabled(false);
		
		
		this.cancelB = new JButton("Cancel");
		this.cancelB.setBounds(generateOneB.getX(),generateAllB.getY()+generateAllB.getHeight()+8,generateOneB.getWidth(),generateOneB.getHeight());
		cancelBListener();
		
		
		this.panel = new JPanel(null);
		this.panel.add(packsL);
		this.panel.add(routsL);
		this.panel.add(sourceL);
		this.panel.add(depL);
		this.panel.add(packS);
		this.panel.add(routS);
		this.panel.add(areaS);
		this.panel.add(generateOneB);
		this.panel.add(generateAllB);
		this.panel.add(editSourceB);
		this.panel.add(saveSourceB);
		this.panel.add(undoB);
		this.panel.add(redoB);
		this.panel.add(formatB);
		this.panel.add(cancelB);
		this.setContentPane(panel);
	}
	
	public void cancelBListener(){
		this.cancelB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				}
		);
	}
	
	public void generateAllBListener(){
		this.generateAllB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Package[] p = packages;
						
						if( p != null ){
							//generate
							for(int i = 0; i < p.length; i++){
								
							}
							
							DiagramWindow dw = new DiagramWindow(parent,p);
							dw.setVisible(true);
							
						}else{
							JOptionPane.showMessageDialog(null,"No package selected.","Error", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
		);
	}
	
	public void generateBListener(){
		this.generateOneB.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Package p = (Package)packsList.getSelectedValue();
						
						if( p != null ){
							p = parent.getFachada().generateRoutinesDependencies(p);
							DiagramWindow dw = new DiagramWindow(parent,new Package[]{p});
							dw.setVisible(true);
							
						}else{
							JOptionPane.showMessageDialog(null,"No package selected.","Error", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
		);
	}
}
