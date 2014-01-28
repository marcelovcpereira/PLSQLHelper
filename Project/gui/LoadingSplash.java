package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class LoadingSplash extends JDialog{
	
	private JPanel panel;
	public JLabel label;
	public JProgressBar bar;
	private JDialog parent;
	public JLabel routine;
	//private JButton cancelB;
	
	public LoadingSplash(JDialog par){
		this.parent = par;
		initialize();		
		
	}

	
	public void initialize(){
		this.setTitle("Please Wait...");
		this.setLocation(
		        (parent.getX()+parent.getWidth()/2)-
		        this.getWidth()/2,
		        (parent.getY()+parent.getHeight()/2)-
		        this.getHeight()/2);
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		this.setSize(265, 140);
		this.setModal(true);
		this.setEnabled(false);
		this.setAlwaysOnTop(true);
		this.panel = new JPanel(null);
		
		this.label = new JLabel("Loading User data...");		
		this.label.setBounds(5,5,245,30);
		
		this.bar = new JProgressBar();
		this.bar.setBounds(5,35, 245, 30);
		this.bar.setStringPainted(true);
		
		this.routine = new JLabel("");
		this.routine.setBounds(5, 70, 245, 30);

		
		this.panel.add(label);
		this.panel.add(bar);
		this.panel.add(routine);
		this.setContentPane(panel);
	}
	
}
