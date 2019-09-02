/*Name: Xin Lu
 * NetID: xlu32
 * Assign_Num: Project 04
 * Lab section: TR 12:30pm - 01:45 pm
*/

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import javax.swing.Timer;

public class Player extends JComponent{
	
	//field variable
	private int score;	//store the score
	
	private boolean onGround;	//this boolean check if the player is on the ground
	
	private BufferedImage image1, image2, image3, image4;	//this variable store the image of player
	
	private int x;	//the x position is not fixed because later it will move
	private int y; //these two variables store the position of the player
	
	private int dy2;	//this is the acceleartion for player to drop smoothly
	private boolean drop;	//this boolean check if the player needs to drop
	
	private int dy;	//this variable store the change of y position each time for the jump
	private int dx;	//this variable store the change of x position, so it is like the speed of the player
	
	//this variable store the ground position that sit below the player directly
	private int groundPos;
	
	//this variable check if this player is still alive
	private boolean alive;
	private int health;	//this variable stores the number of blood the player has
	
	//this index shows which pixel of image should this show
	private int index = 0;
	
	//check if the player is jumpping
	private boolean jump;
	private boolean doubleJump;	//check if the player is doubleJump and if he can
	private boolean powerUp;	//this boolean shows that if the player is able to use the double jump skill
	private boolean deadDrop;	//this check if this is a deadDrop
	private boolean winJump;	//this boolean check if this player has already executed the win jump
	
	//this boolean check if there is any hinder in front of this player
	private boolean hindered;
	
	private Foreground foreground;	//this variable store the map the player is at
	
	//take the current position of the player as vairalbes
	public Player(){
		
		//initially the player has no score
		this.score = 0;
		this.onGround = true;	//at first the player must be on the ground
		
		//add image to this player and paint itself
		try {
			this.image1 = ImageIO.read(new File("Picture/P1.png"));
			this.image2 = ImageIO.read(new File("Picture/P2.png"));
			this.image3 = ImageIO.read(new File("Picture/P3.png"));
			this.image4 = ImageIO.read(new File("Picture/P4.png"));
		} catch (IOException e) {
			System.out.print("You do not have this image in the folder.");
		}
		
		this.x = 40;	//set its initial position
		
		this.dy2 = -3;	//initialize drop down acceleration for the player
		
		//initialize the jump dy. At first, when the player starts jumpping, the dy is set to be 15
		this.dy = 15;
		this.dx = 6;	//generally we assume the player run with a speed of 5 pixel per ms
		
		this.alive = true;	//initially, the player is alive
		this.jump = false;	//initially the player is not jumping
		this.powerUp = false;	//initially does not have any powerup
		this.doubleJump = true;	//at the first place the player could double jump as long as he has powerUp
		this.deadDrop = false;	//if the player has already go through this, don't execute deadDrop again
		this.winJump = false;
		
		this.health = 3;	//initially the player has 3 blood
	}
	
	
	//getter for the winJump boolean
	public boolean getWinJump(){
		return this.winJump;
	}
	
	//the method that mvoes the player's x position
	public void moveX(int aNum){
		//if the player does not win yet, keep moving
		if(!this.win()){
			this.x += aNum;
		}
	}
	
	//check if this player has win
	public boolean win(){
		boolean result = false;	//initially set the boolean as false
		//this boolean check that if the player is in certain range of the castle, the player wins
		if(this.x + 25 >= this.foreground.getCastlePos() + 30){
			result = true;
		}
		//else the win should run as false
		return result;
	}
	
	//getter and setter for the power up method
	public void setPowerUp(boolean powerUp){
		this.powerUp = powerUp;
	}
	
	public boolean getPowerUp(){
		return this.powerUp;
	}
	
	//getter and setter for the double jump
	public void setDoubleJump(boolean temp){
		this.doubleJump = temp;
	}
	
	public boolean getDoubleJump(){
		return this.doubleJump;
	}
	
	//return the y position of the player
	public int getY(){
		return this.y;
	}
	
	//method that returns if the player has done the deadDrop
	public boolean getDeadDrop(){
		return this.deadDrop;
	}
	
	//this method notify the player to keep droping since he is dead
	public void deadDrop(int aNum){
		//if the player has never deadDrop before
		if(!this.deadDrop){
			//set the acceleration for both drop and jump
			this.dy = 20;
			this.dy2 = 20;
			this.y -= aNum;
			//when the player drops, don't do anything to the ground since we need mario to fall
			//and thus we set these variable to false
			this.jump = false;
			this.drop = false;
			this.deadDrop = true;
			//then execute the jump
			this.jump();
		}
	}
	
	public boolean dead(){
		//if the player has health less than 0, he or she should die
		if(this.health <= 0){
			this.alive = false;
		}
		//since we check if he is die, reverse the alive boolean
		return !this.alive;
	}
	
	//getter and setter for health
	public void setHealth(int aNum){
		this.health = aNum;
	}
	
	public void addHealth(int aNum){
		this.health += aNum;
	}
	
	//method that notify the program the blood player have
	public int getHealth(){
		return this.health;
	}
	
	//the method set the foreground
	public void setFore(Foreground foreground){
		//get the map from parameter
		this.foreground = foreground;
		//since a player needs to start on top of a hill, set the y based on x and the hill height
		//and once we have the foreground, we are able to set the y position of the player
		this.y = this.foreground.getTop(this.x) - 50;	//minus 50 since the player itself has 50 height	
	}
	
	//get the jump boolean
	public boolean getJump(){
		return this.jump;
	}
	
	//this method change the player's current status
	public boolean getAlive(){
		return this.alive;
	}
	
	//this method change the player's current status
	public void setAlive(boolean alive){
		this.alive = alive;
	}
	
	//this method notify the reader where is the ground, and therefore determined when player shoud
	//stop jumpping
	public void setGroundPos(int groundPos){
		this.groundPos = groundPos;
	}
	
	//this method return the x position of this player
	//this method is used for monster to check if it kills player
	public int getX(){
		return this.x;
	}
	
	//this method check if this player is on the ground
	public boolean ifOnGround(){
		//here we check the ground position
		//so whenever we want to see if a player is on ground, we first need to calculate the ground
		//top at player's position
		//we add 50 since the picture has a width of 50
		int temp = this.foreground.getTop(this.x + 50);	//adding the 50 to get player's front
		this.setGroundPos(temp);	//and set the ground position
		boolean result = false;
		//get the hill the player is currently at and see if it is a gap
		boolean aGap = this.foreground.getIndex(this.x).getIsGap();
		
		//re calculate the temp in order to see if we want the player to drop or not.
		//For player we want he to drop when most of his body is out of the edge, thus
		temp = this.foreground.getTop(this.x + 25);
		
		int yPos = this.y + 50;	//this is the y position of the bottom line of this picture, with 5 error range
		
		this.hindered = this.foreground.isHinder(this.x, this.y);
		
		//if the player has its bottom line below the hill and by above player is not hindered
		//if at the player's current position it is not a gap, we should still adjust the player's position 
		//based on the current height of the hill
		//also we rest the position when player is not dead
		if((yPos > this.groundPos) && (!(this.hindered) || !aGap) && !this.deadDrop){
			this.y = this.groundPos - 50;	//set the bottom line of the player same as the ground height
			//also set the jump and drop to false to prevent further animation on player
			this.drop = false;
			this.jump = false;
			this.doubleJump = true;	//after a player is on the ground again could he do double jump again
			this.dy = 15;
			this.dy2 = -3;
			
			result = true;
			
		}
		
		//if the player is not jumping but has bottom line higher than hill
		//since we want the player to drop when he is entirely away from the edge, minus 55
		else if((yPos < temp) && (!this.jump)){
			this.drop = true;	//we know the player now should drop
		}
		
		return result;
		
	}
	
	//method that notify the program if the player is hindered
	public boolean getHindered(){
		return this.hindered;
	}
	
	//method that get the drop boolean
	public boolean getDrop(){
		return this.drop;
	}
	
	//both drop and jump method will check if the player is still alive
	//this method is created for the player to drop down smoothly
	public void drop(){
		//only when the player is not dead and never execute the deadDrop will we keep dropping
		this.y -= this.dy2;
		this.dy2 -= 1;
		
		if(this.y >= 480){	//if the player drops low enough, the player is dead
			this.health = 0;	//set the health to 0
			//set the boolean of alive and deadDrop to true
			this.alive = false;
		}
	}
	
	//this method is called when the user press up key and thus start jumpping
	public void jump(){	
		
		this.jump = true;
		this.y -= this.dy;	//add y position. Since up is smaller y, set the -
		this.dy -= 1;
		
		if(this.y >= 480){	//if the player drops low enough, the player is dead
			this.health = 0;	//set the health to 0
			//similar as above
			this.alive = false;
		}
		
		//if this player jump after the win is set, set the winJump as false
		if(this.win()){
			this.winJump = true;
		}
	}
	
	//this method is prepared for the double jump
	public void doubleJump(){
		//whenever we run this program, we will see if the player is under a stage such that 
		//he is able to do the double jump
		this.jump = true;
		this.dy = 15;	//set the acceleration back to default
		this.y -= this.dy;
		this.doubleJump = false;	//now since he has done the double jump, only when again he is on ground
									//could player do double jump again
	}
	
	//getter and setter for information about this player
	//this method add score to this player itself
	public void addScore(int aNum){
		this.score += aNum;
	}
	
	//change the score directly
	public void setScore(int aNum){
		this.score = aNum;
	}
	
	//access the score
	public int getScore(){
		return this.score;
	}
	
	//change the boolean indicating whether the player is on ground or not
	public void setOnGround(boolean onGround){
		this.onGround = onGround;
	}
	
	//get the above boolean
	public boolean getOnGround(){
		return this.onGround;
	}
	
	//method that change the y position of this player
	public void setY(int y){
		this.y = y;
	}
	
	//set preferred size for the image
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(50, 50);
	}
	
	private BufferedImage image;
	//paint itself
	@Override
	public void paintComponent(Graphics g){
		//super.paintComponent(g);
		//paint the player image
		if(this.index < 3){
			image = this.image1;
			this.index++;
		}
		else if(this.index < 6){
			image = this.image2;
			this.index++;
		}
		else if(this.index < 9){
			image = this.image3;
			this.index++;
		}
		else if(this.index < 12){
			image = this.image4;
			this.index++;
			if(this.index == 12){
				this.index = 0;
			}
		}
		g.drawImage(image, this.x, this.y, 50, 50, this);
	}
	
	//tester
	public static void main(String[] args){
		JFrame frame = new JFrame();
		
		//frame.add(player);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
}
