
/*Name: Xin Lu
 * NetID: xlu32
 * Assign_Num: Project 04
 * Lab section: TR 12:30pm - 01:45 pm
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Canvas extends JComponent implements ActionListener, KeyListener, MouseListener{

	private Timer timer;	//the timer that will be shared by player and foreground
	private Foreground foreground;	//other game elements
	private Player player;
	
	private BufferedImage background, grassBackground;
	private BufferedImage health;	//this image store the heart icon that represents player's health
	
	private boolean stop;	//this check if the timer is stopped
	
	private boolean over;	//this boolean checks if the game is still on
	
	private long startTime = 0;	//initially the time is not running
	private long endTime;	//these two variables, the later one keep changing till the end of game
	private long time = 0;
	
	//variable that store the font of the timeLabel
	private Font font;
	
	private JLabel timeLabel;	//this variable stores the time label taken from game
	private JLabel scoreLabel;	//this variable stores the score of the player
	private JLabel totalLabel;	//stores the total points for the label
	
	private int postpone;	//this index helps the game not to start so quick
	
	//this stores the information about which map will be displayed
	private int num;
	private RescaleOp op = new RescaleOp(.5f, 0, null);
	
	//sound file
	private Sound jumpSound, BGM, deadSound, winSound;
	
	public Canvas(int num){
		
		this.num = num;
		
		//add listener
		addKeyListener(this);
		setFocusable(true);
		addMouseListener(this);
		
		
		//initialize the timer and other variable
		this.timer = new Timer(50, this);
		
		//since the player does not has its personal feature, we create here without any parameter
		this.player = new Player();
		this.foreground = new Foreground(num);
		this.player.setFore(this.foreground);
		
		//initially the game is not over
		this.over = false;
		
		//set the player to the enemy of all monsters
		this.foreground.setPlayer(player);
		
		//add the background image of this component and read the image of the health
		//add image to this player and paint itself
		try {
			this.background = ImageIO.read(new File("Picture/Background.png"));
			this.grassBackground = ImageIO.read(new File("Picture/GrassBackground.jpg"));
			this.health = ImageIO.read(new File("Picture/Health.png"));
		} catch (IOException e) {
			System.out.print("You do not have this image in the folder.");
		}
		
		//initialize the jumpSound, BGM, deadSound and winSound
		this.jumpSound = new Sound("Music/Jump.wav");
		this.BGM = new Sound("Music/BGM.wav");	//the background music
		this.deadSound = new Sound("Music/Dead.wav");
		this.winSound = new Sound("Music/Win.wav");
		
		//get all the input streams and etc here
		FileInputStream temp;
		InputStream input;
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
		//initialize all labels
		this.totalLabel = new JLabel();
		this.totalLabel.setForeground(Color.WHITE);
		this.totalLabel.setFont(font);
	}
	
	public boolean isFocusTraversable(){
		return true;
	}
	
	//set the prefer size of this canvas
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(640, 480);
	}
	
	//this method check if the player is dead, play the deadSound
	public boolean getOver(){
		//if the game is over, stop the timer
		if(this.over){
			//update the total points label
			this.getPoints();
			
			this.timer.stop();
		}
		
		return this.over;
	}
	
	//this method return the JLabel of the total scores
	public JLabel getTotalLabel(){
		return this.totalLabel;
	}
	
	//since we also add score for a shorter finish on this game, we sum up the score in this class
	//rather than in player class
	public int getPoints(){
		//add the player socres(including monster killing, powerUp points and winning points
		int points = this.player.getScore() + 200*this.player.getHealth();
		
		//since the better the less time used, we use 600 seconds to minus the total seconds being used
		//and add it to the points
		//600 seconds is 10 minutes, which is definity long enough
		//if exceeds 10 minutes, we will subtract total score
		if(this.player.win()){	//only if the player wins will we calculate the time
			points += (int)(3600 - (this.time / 1000000000));
		}
		
		this.totalLabel.setText(String.valueOf(points));	//set the value of the JLabel
		
		return points;
		
	}
	
	public void setScoreLabel(JLabel score){
		this.scoreLabel = score;
		//set the font for the timeLabel
		this.scoreLabel.setFont(font);
		this.scoreLabel.setForeground(Color.WHITE);	//the color of the text
		
		//set the position of the score label
		this.scoreLabel.setLocation(150, 10);
		
	}
	
	
	//like the below, these two methods set the Score label and also update score each time the timer ends
	public void setScore(){
		//get player's score and change it to the string
		this.scoreLabel.setSize(scoreLabel.getPreferredSize().width, 
				scoreLabel.getPreferredSize().height);
		this.scoreLabel.setText(String.valueOf(this.player.getScore()));
	}
	
	//this method return the timedifference
	public void setTime(){
		this.time = this.endTime - this.startTime;	//calculate the time difference
		
		//now since the time is in milliseconds, convert the nanotime
		long totalSeconds = (this.time / 1000000000);
		//Format the above seconds. So it will have at least 
		int minute = (int)(totalSeconds/60);
		double second = totalSeconds - (minute*60);	//get the remaining second part
		
		DecimalFormat minuteFormat = new DecimalFormat("##0");
		DecimalFormat secondFormat = new DecimalFormat("###");	//so we only get the 3 digits
		String minutePart = minuteFormat.format(minute);	//get the string for the second part
		String secondPart = secondFormat.format(second);
		
		
		String result = minutePart + ":" + secondPart;
		
		//each time we set time, change the bounds
		this.timeLabel.setText(String.valueOf(result));//set the JLabel text
		//when we set the time label, also set its location
		this.timeLabel.setSize(timeLabel.getPreferredSize().width, 
				timeLabel.getPreferredSize().height);
	}
	
	//method that takes the timeLabel
	public void setTimeLabel(JLabel timeLabel){
		this.timeLabel = timeLabel;
		
		//set the font for the timeLabel
		this.timeLabel.setFont(font);
		this.timeLabel.setForeground(Color.WHITE);	//the color of the text
		this.timeLabel.setLocation(30, 10);
	}
	
	
	//this method start the timer
	public void start(){
		this.timer.start();
		
		/*this.BGM.play();	//make it play once we have the background after the game start
		this.BGM.loop(1);	//make it loop constantly
		
		//once we start the BGM, start calculating the time
		//we minus this.time because if the game is restarted, we want to adjust the time displayed
		//on the label since the paused time does not account
		this.startTime = System.nanoTime() - this.time;	//in nanoTime*/
		
		this.stop = false;
	}
	
	//this method stop the timer
	public void stop(){
		this.timer.stop();
		//whenever we stop the entire game, also calculate the endTime
		this.endTime = System.nanoTime();
		this.time = this.startTime - this.endTime;	//calculate the time difference
		
		this.stop = true;
	}
	
	public void stopMusic(){
		this.BGM.stop();
	}
	
	//the paintcomponent method paint this canvas
	public void paintComponent(Graphics g){
		
		BufferedImage image = this.background;
		if(this.num == 2){
			image = this.grassBackground;
		}
		
		//first paint the background image
		g.drawImage(image, 0, 0, 640, 480, this);
		//then draw the foreground
		this.foreground.paintComponent(g);
		//and last draw the player
		this.player.paintComponent(g);
		
		//temporary index that initialized as 0 every time
		int i = 0;
		int x = 600;	//the starting position of the health
		//paint the health on the right upper corner of the screen
		while(i < this.player.getHealth()){
			g.drawImage(this.health, x, 0, 38, 38, this);
			i++;	//change the index
			x -= 40;	//change the x position the heart is drawn
						//make a little distance between each heart
		}
	}
	
	//tester for this class
	public static void main(String[] args){
		//player.setPowerUp(true);
		
		JLayeredPane pane = new JLayeredPane();
		
		JLabel time = new JLabel();
		JLabel score = new JLabel();
		
		pane.add(time, JLayeredPane.MODAL_LAYER);
		pane.add(score, JLayeredPane.MODAL_LAYER);
		pane.setBounds(0, 0, 640, 480);
		
		JFrame frame = new JFrame();
		
		frame.add(pane);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
	}
	
	//this index is for the final win jump
	private int index = 0;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		while(this.postpone < 4){
			this.postpone ++;
			return;
		}
		
		//when the postpone ends(about 2 seconds), start the game
		if(this.postpone == 4){
			this.BGM.play();	//make it play once we have the background after the game start
			this.BGM.loop(1);	//make it loop constantly
			
			//once we start the BGM, start calculating the time
			//we minus this.time because if the game is restarted, we want to adjust the time displayed
			//on the label since the paused time does not account
			this.startTime = System.nanoTime() - this.time;	//in nanoTime
			
			this.postpone = 5;
		}
		
		//whenever the timer goes to an end, get the endTime again and therefore reset the label
		this.endTime = System.nanoTime();
		this.setTime();	//whenever the timer goes out, set the label again
		this.setScore();	//display the newest score
		
		//store the boolean of the ifOnGround and also run this method once to update the hindered boolean
		boolean check = this.player.ifOnGround();
		
		//if the player is not on the ground
		//this method is run always when player is not on ground such that if the player is dropping
		//into a gap, we can go through the drop method
		if(!check){	//if the player is dead, always run the below
			//if this player is jumping, the below method will be called
			if(this.player.getJump()){
				this.player.jump();	//then the player will keep jumping until it reaches the ground
			}
			else if(this.player.getDrop()){
				this.player.drop();
			}
		}
		
		//this part of the method runs later since we need to update the current situation of player
		//if the player is hindered, stop background from scrolling
		if(this.player.getHindered()){
			this.stop = true;	//we do not technically stop the timer, just stop the foreground from moving
			//if this player is dead and hindered, that means he drops to the bottom of the gap
			if(this.player.dead()){
				//similar as below. This make the dead sound only played once
				if(!this.player.getDeadDrop()){
					this.deadSound.play();
				}
				//if the player is dead, set the dy so that the player will jump up and drop down
				//to show that he is dead
				this.player.deadDrop(30);	//then jump to show that he is dead
				this.BGM.stop();
				
				//only when all picture finish their jump will we set the over as true
				if(this.player.getY() >= 500){
					this.over = true;
				}
			}
		}
		//otherwise the player is killed by the mosnter so the player should not jump as high
		else if(this.player.dead()){
			this.stop = true;
			//if the player has not dead drop, play the sound
			if(!this.player.getDeadDrop()){
				this.deadSound.play();
				this.BGM.stop();
			}
			//if the player is dead, set the dy so that the player will jump up and drop down
			//to show that he is dead
			this.player.deadDrop(15);	//then jump to show that he is dead
			
			//only when all picture finish their jump will we set the over as true
			if(this.player.getY() >= 500){
				this.over = true;
			}
		}
		//if the foreground does not stop, keep move the foreground
		else if(!(this.foreground.stop())){	//if the player is no longer hindered and the timer is now being stopped, restart the timer
			//the foreground move only when the player is not hindered
			this.foreground.move(6);	//make the foreground move 10 pixel each 0.1 s
		}
		//if the foreground stop moving
		else if(!this.player.win()){
			//after the foreground stop moving, we move the player until he wins
			this.player.moveX(6);
		}
		else if(this.player.win()){
			//in the case that this player win and has never jumped before, play the win music and jump
			if(!this.player.getWinJump()){
				//only at the first time the player jump for winning we add score to this player
				this.player.addScore(500);
				
				this.player.jump();
				//once the player win, play the sound
				this.BGM.stop();
				this.winSound.play();
			}
			//this method is to allow mario to do three time jump after he wins
			else if(index < 3 && this.player.ifOnGround()){
				this.player.jump();
				index ++;
			}
			
			//if all the above execution is done, we know the game should end
			else if(index >= 3 && this.player.ifOnGround()){
				this.over = true;
			}
		}
		
		//check whenever a timer go to see if this game ends
		this.getOver();
		
		repaint();
	}


	//i add the declaration here so that it is easy to see the association
	private int code;

	@Override
	public void keyPressed(KeyEvent e) {
		this.code = e.getKeyCode();
		switch(this.code){
		case 38:
			//if the player has already pressed the jump and thus changing the status
			//so only when a player has not double jump before, and is now jumping, and also has
			//the power up present could this player double jump
			if(this.player.getDoubleJump() && this.player.getPowerUp() && this.player.getJump()){
				this.player.doubleJump();			
				this.jumpSound.play();
			}
			//other wise, if the player is not on a gap, allow to jump
			else if(!(this.foreground.getIndex(this.player.getX()).getIsGap())){
				this.player.jump();
				this.jumpSound.play();
			}
			break;
		}
		
	}
	
	//by doing so, the player can choose to either use mouse or up key to do the jump
	public void mouseClicked(MouseEvent e) {
		//if the player has already pressed the jump and thus changing the status
		//so only when a player has not double jump before, and is now jumping, and also has
		//the power up present could this player double jump
		if(this.player.getDoubleJump() && this.player.getPowerUp() && this.player.getJump()){
			this.player.doubleJump();			
			this.jumpSound.play();
		}
		//other wise, if the player is not on a gap, allow to jump
		else if(!(this.foreground.getIndex(this.player.getX()).getIsGap())){
			this.player.jump();
			this.jumpSound.play();
		}
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
