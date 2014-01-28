package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import basicas.MyCanvas;
import basicas.Package;
import basicas.Routine;

public class DiagramWindow extends JDialog{
	
	private JPanel panel;
	private GUI parent;
	private Package[] pack;
	private JMenuBar bar;
	private JMenu menuSave;
	public static final int packageWidth = 200;
	public static final int packageMargin = 100;
	public static final int routineWidth = 160;
	public static final int routineHeight = 37;
	public static final int routineMargin = 20;
	
	public int numberOfPacks;
	
	public DiagramWindow(GUI p, Package[] pa){
		this.parent = p;
		this.pack = pa;
		
		
		numberOfPacks = pack.length;
		int width = (numberOfPacks*packageWidth)+((numberOfPacks+1)*packageMargin)+50;
		width = Math.max(width, 800);
		int height = 600;
		this.setSize(width, height);
		this.setResizable(true);
		this.setModal(true);
		this.setTitle("Dependency Diagram");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		initializeComponents();
		this.setLocation(
		        (parent.getX()+parent.getWidth()/2)-
		        this.getWidth()/2,
		        (parent.getY()+parent.getHeight()/2)-
		        this.getHeight()/2);
	}
	
	public int getMaxRoutines(){
		int ret = 0;
		for(int i = 0; i < pack.length; i++){
			Package p = pack[i];
			int length = p.getRoutines().length;
			if(length > ret){
				ret = length;
			}
		}
		return ret;
	}
	
	public void initializeComponents(){
		final int dW = this.getWidth();
		final int dH = this.getHeight();
		this.bar = new JMenuBar();
		this.menuSave = new JMenu("Save");
		this.menuSave.setEnabled(false);
		this.bar.add(menuSave);
		MyCanvas c = new MyCanvas(){
			public void paint(Graphics g){
				super.paint(g);
				int maxRoutines = getMaxRoutines();
				this.setSize(dW, maxRoutines*routineHeight+(maxRoutines*routineMargin)+50);
				_drawSpaceG.setColor(Color.WHITE);
				_drawSpaceG.fillRect(0,0,this.getWidth(),this.getHeight());
				_drawSpaceG.setColor(Color.BLACK);
				int x0 = 0;
				int x1 = (getX()+getWidth())/numberOfPacks;
				int blockW = getWidth()/numberOfPacks;//packageWidth + packageMargin;
				
				//for each package...
				for(int i = 0; i < numberOfPacks; i++){
					x0 = i*blockW;
					x1 = x0+blockW;
					int packX = ((x0+x1)/2) - (packageWidth/2);
					int packY = 25;
					Package p  = pack[i];
					int numberOfRouts = p.getRoutines().length;
					_drawSpaceG.drawRect(packX, packY, packageWidth, (numberOfRouts*routineHeight)+((numberOfRouts+1)*routineMargin));
					_drawSpaceG.drawString(p.getName(), packX+5, packY+15);
					
					//for each of its routines...
					for(int j = 0; j < numberOfRouts; j++){
						Routine r = p.getRoutines()[j];
						int rX = packX+routineMargin;
						int rY = packY+25 + (j*routineHeight)+(j*routineMargin);
						_drawSpaceG.drawRect(rX, rY, routineWidth, routineHeight);
						_drawSpaceG.drawString(r.getName(), rX+5, rY+20);
						
						
						Routine[] callF = r.getCallFor();
						for(int h = 0; h < callF.length; h++){
							Routine called = callF[h];
							int index = 0;
							
							for(int w = 0; w < p.getRoutines().length; w++){
								Routine temp = p.getRoutines()[w];
								if( temp.getName().equalsIgnoreCase(called.getName())){
									index = w;
									break;
								}
							}
							int space = 10;
							int yCalled = packY+25 + (index*routineHeight)+(index*routineMargin) + routineHeight/2;
							//draw top down
							if( j < index ){					
								space = (space * (index - j)) + 20;
								_drawSpaceG.drawLine(rX,rY+routineHeight/2,rX- space,rY+routineHeight/2);
								_drawSpaceG.drawLine(rX- space,rY+routineHeight/2, rX - space, yCalled);
								_drawSpaceG.drawLine(rX - space, yCalled, rX, yCalled);
								
								//draw arrow
								int x11 = rX-8;
								int y1 = yCalled-8;
								
								int x2 = rX-8;
								int y2 = yCalled+8;
								
								int x3 = rX;
								int y3 = yCalled;
								_drawSpaceG.fillPolygon(new int[]{x11,x2,x3}, new int[]{y1,y2,y3}, 3);								
							}else{
								//draw bottom up
								space = space * (j - index);
								int newX = rX + routineWidth;
								_drawSpaceG.drawLine(newX,rY+routineHeight/2,newX+ space,rY+routineHeight/2);
								_drawSpaceG.drawLine(newX+space,rY+routineHeight/2, newX+space, yCalled);
								_drawSpaceG.drawLine(newX+space, yCalled, newX, yCalled);
								
								//draw arrow
								int x11 = newX+8;
								int y1 = yCalled-8;
								
								int x2 = newX+8;
								int y2 = yCalled+8;
								
								int x3 = newX;
								int y3 = yCalled;
								_drawSpaceG.fillPolygon(new int[]{x11,x2,x3}, new int[]{y1,y2,y3}, 3);
							}
							
						}
					}
				}
				
				g.drawImage(_drawSpace, 0, 0, Color.black, this);
			}
		};
		this.panel = new JPanel(){
			
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				
				g.drawRect(0,0,getWidth(),getHeight());
				
				int x0 = 0;
				int x1 = (getX()+getWidth())/numberOfPacks;
				int blockW = getWidth()/numberOfPacks;//packageWidth + packageMargin;
				
				//for each package...
				for(int i = 0; i < numberOfPacks; i++){
					x0 = i*blockW;
					x1 = x0+blockW;
					int packX = ((x0+x1)/2) - (packageWidth/2);
					int packY = 25;
					Package p  = pack[i];
					int numberOfRouts = p.getRoutines().length;
					g.drawRect(packX, packY, packageWidth, (numberOfRouts*routineHeight)+((numberOfRouts+1)*routineMargin));
					g.drawString(p.getName(), packX+5, packY+15);
					
					//for each of its routines...
					for(int j = 0; j < numberOfRouts; j++){
						Routine r = p.getRoutines()[j];
						int rX = packX+routineMargin;
						int rY = packY+25 + (j*routineHeight)+(j*routineMargin);
						g.drawRect(rX, rY, routineWidth, routineHeight);
						g.drawString(r.getName(), rX+5, rY+20);
						
						
						Routine[] callF = r.getCallFor();
						for(int h = 0; h < callF.length; h++){
							Routine called = callF[h];
							int index = 0;
							
							for(int w = 0; w < p.getRoutines().length; w++){
								Routine temp = p.getRoutines()[w];
								if( temp.getName().equalsIgnoreCase(called.getName())){
									index = w;
									break;
								}
							}
							int space = 8;
							int yCalled = packY+25 + (index*routineHeight)+(index*routineMargin) + routineHeight/2;
							//draw top down
							if( j < index ){					
								space = (space * (index - j)) + 20;
								g.drawLine(rX,rY+routineHeight/2,rX- space,rY+routineHeight/2);
								g.drawLine(rX- space,rY+routineHeight/2, rX - space, yCalled);
								g.drawLine(rX - space, yCalled, rX, yCalled);
								
								
							}else{
								//draw bottom up
								space = space * (j - index);
								int newX = rX + routineWidth;
								g.drawLine(newX,rY+routineHeight/2,newX+ space,rY+routineHeight/2);
								g.drawLine(newX+space,rY+routineHeight/2, newX+space, yCalled);
								g.drawLine(newX+space, yCalled, newX, yCalled);
							}
							
						}
					}
				}
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBounds(0,0,getWidth()+100,getHeight()+200);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(c);
		scroll.setAutoscrolls(true);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setJMenuBar(bar);
		this.getContentPane().add(scroll);
	}

}
