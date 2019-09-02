import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

//this is also a very small class, and it stores the things needed on a pause page
public class PausePage extends JPanel{
	
	private BufferedImage background1, background2;
	private Button quit, back;
	
	private Font font;
	
	private int index;	//this determine which pause page will be showed
	
	private JLabel scoreL, timeL, totalL, score, time, totalPoints;	
	//these label descirbes the numbers displayed on other three labels
	
	//take the parameter in so that we can handle reaction of these button in the frame
	public PausePage(Button quit, Button back){
		FileInputStream temp;
		BufferedInputStream input;
		Font smallFont;
		try {
			temp = new FileInputStream("Font/Font.ttf");	//import the font files
			input = new BufferedInputStream(temp);
			smallFont = Font.createFont(Font.TRUETYPE_FONT, input);
		    this.font = smallFont.deriveFont(25f);
		} catch (FileNotFoundException e1) {
			System.out.print("File not found");
		} catch (FontFormatException e) {
			System.out.print("FontFormatException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.print("IOException");
		}
		
		this.index = 1;	//originally the index is 1
		
		this.scoreL = new JLabel("SCORE");
		this.timeL = new JLabel("USED TIME");
		this.totalL = new JLabel("TOTAL POINTS");
		
		this.scoreL.setFocusable(false);
		this.timeL.setFocusable(false);
		this.totalL.setFocusable(false);
		
		this.timeL.setFont(font);
		this.scoreL.setFont(font);
		this.totalL.setFont(font);
		
		this.timeL.setForeground(Color.WHITE);
		this.scoreL.setForeground(Color.WHITE);
		this.totalL.setForeground(Color.WHITE);
		
		//set size for these label
		this.scoreL.setSize(200, 80);
		this.timeL.setSize(200, 80);
		this.totalL.setSize(250, 80);
		
		this.setLayout(null);
		
		//set bounds for this panel
		this.setBounds(0, 0, 640, 480);
		
		try {
			this.background1 = ImageIO.read(new File("Picture/Background.png"));
			this.background2 = ImageIO.read(new File("Picture/GrassBackground.jpg"));
		} catch (IOException e) {
			System.out.print("You do not have this image in the folder.");
		}
		this.quit = quit;
		this.back = back;
		
		//set the location of the button
		this.quit.setLocation(240, 280);
		this.back.setLocation(240, 340);
		
		this.add(this.quit);
		this.add(this.back);

	}
	
	//method that set index for the pause page
	public void setIndex(int index){
		this.index = index;
	}
	
	//this method is for when the game is done, display player's points and etc.
	public void setLabels(JLabel score, JLabel time, JLabel totalPoints){
		this.score = score;
		this.time = time;
		this.totalPoints = totalPoints;
		
		//set the size for the total points
		this.totalPoints.setSize(200, 30);
		
		this.score.setLocation(40, 240);
		this.time.setLocation(220, 240);
		this.totalPoints.setLocation(400, 240);
		
		this.scoreL.setLocation(10, 180);
		this.timeL.setLocation(200, 180);
		this.totalL.setLocation(390, 180);
		
		this.add(this.score);
		this.add(this.time);
		this.add(this.totalPoints);
		this.add(this.scoreL);
		this.add(this.timeL);
		this.add(this.totalL);
		//since these are displayed only after the game is end, we remove the return button
		this.remove(this.back);
		
		this.repaint();
	}
	
	//methods that remove the label
	public void removeLables(){
		this.remove(this.score);
		this.remove(this.time);
		this.remove(this.totalPoints);
		this.remove(this.scoreL);
		this.remove(this.timeL);
		this.remove(this.totalL);
		
		this.add(this.back);
		this.add(this.quit);
		
		this.repaint();
	}
	
	public void paintComponent(Graphics g){
		BufferedImage background = this.background1;
		
		if(this.index == 2){	//if we are at map 2
			background = this.background2;
		}
		
		g.drawImage(background, 0, 0, 640, 480, this);
	}
	
	
}
