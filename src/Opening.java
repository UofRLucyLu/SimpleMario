import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


//this class is for the opening page of the whole game
//this class is also not that large
public class Opening extends JPanel implements MouseMotionListener{
	
	private BufferedImage opening, pointer;
	private Button start, quitAll, level1, level2;
	
	//these two variables for the location
	private int x, y;
	
	private Sound openMusic;
	
	public Opening(Button start, Button quitAll, Button level1, Button level2){
		
		//sound for the opening page
		this.openMusic = new Sound("Music/Menu.wav");
		
		//set the layout of the opening
		this.setLayout(null);
		
		//add mouse motion listner
		this.addMouseMotionListener(this);
		
		//initialize all button
		this.start = start;
		this.quitAll = quitAll;
		this.level1 = level1;
		this.level2 = level2;
		
		//set location of these buttons
		this.start.setLocation(260, 280);
		this.quitAll.setLocation(260, 340);
		this.level1.setLocation(260, 280);
		this.level2.setLocation(260, 340);
		
		//add image to this player and paint itself
		try {
			this.opening = ImageIO.read(new File("Picture/Opening.jpg"));
			this.pointer = ImageIO.read(new File("Picture/Pointer.png"));
		} catch (IOException e) {
			System.out.print("You do not have this image in the folder.");
		}
		
		//initialize the starting position of the pointer
		//originally it is on the first button
		this.x = 240;
		this.y = 280;
		
		//originally add the opening stuff
		this.openMusic.play();
		this.openMusic.loop(1);
		this.addOpening();
		
	}
	
	
	//method that stop the openMusic
	public void stopMusic(){
		this.openMusic.stop();
	}
	
	public void startMusic(){
		this.openMusic.play();
	}
	
	public void addOpening(){
		this.add(this.start);
		this.add(this.quitAll);
	}
	
	//this is used when player chooose to start
	public void removeOpening(){
		this.remove(this.start);
		this.remove(this.quitAll);
	}
	
	public void addLevelSelect(){
		//add the level choices
		this.add(this.level1);
		this.add(this.level2);
	}
	
	public void removeLevelSelect(){
		//remove the level choices
		this.remove(this.level1);
		this.remove(this.level2);
	}
	
	//set preferredSize of this panel
	public Dimension getPreferredSize(){
		return new Dimension(640, 480);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	
	public void paintComponent(Graphics g){
		g.drawImage(this.opening, 0, 0, 640, 480, this);
		
		g.drawImage(this.pointer, this.x, this.y, 25, 25, this);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getY() > 310){
			this.y = 340;
		}
		else{
			this.y = 280;
		}
		
		//whenever mouse moved, repaint
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getY() > 330){
			this.y = 340;
		}
		else{
			this.y = 280;
		}
		
		//whenever mouse moved, repaint
		repaint();
	}
	
}
