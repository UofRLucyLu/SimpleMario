/*Name: Xin Lu
 * NetID: xlu32
 * Assign_Num: Project 04
 * Lab section: TR 12:30pm - 01:45 pm
*/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.Random;

public class Hill extends JComponent{
	
	private int x, y, startX, startY;	//store the position of the hill
	private int width, height;	//these two variables store the basic information about the hill
	private int numOfW, numOfH;	//this variable stores the number of bricks in the hill horizontally and vertically
	
	private BufferedImage brick, grass;	//this variable store the image of the brick
	
	private Random gene = new Random();	//this generator generates the position of the monster randomly
	
	private boolean hasMonster;	//this variable notify the program if there is a monster on this hill
	private Monster monster;	//this stores the monster variable, if any
	
	private boolean exit;	//check if the hill exits the windows
	
	private boolean isGap;	//this boolean indicates if this hill is a gap or not
	//this is the index that helps generating the length of the gap in order to make it possible to jump over
	private int index, num;	
	
	private RescaleOp op = new RescaleOp(.5f, 0, null);
	
	//take the x, y position of this hill as a parameter
	public Hill(int x, int y, int num){
		this.num = num;
		
		Random gene = new Random();
		//in this way the hill is at least four bricks wide and at most 10 bricks wide
		this.numOfW = (gene.nextInt(7)) + 4;
		//in this way the hill will not be more than 10 bricks hegith and no less than 6 bricks height
		this.numOfH = (gene.nextInt(5)) + 6;	
		
		//since each brick has a length of 25 pixel and height of 15 pixel, we can calculate the width
		//and the height
		this.width = this.numOfW * 25;
		this.height = this.numOfH * 25;
		
		//initialize the wall position
		this.x = x;
		this.y = y;
		
		//these two variables are used to store the original position of the hill, and therefore when
		//we want to reuse this hill, we are able to directly set back the x y position by retriving these
		this.startX = x;
		this.startY = y;
		
		//at the begining, the wall should be in the window
		this.exit = false;
		
		//at the begining, the hill does not have a monster
		this.hasMonster = false;
		
		
		this.index = (gene.nextInt(9));	//the index will be at most 5 because we want the gap be at most 80
		
		//here we initialize the image of the brick
		try {
			this.brick = ImageIO.read(new File("Picture/Brick.jpg"));
			this.grass = ImageIO.read(new File("Picture/Grass.png"));
		} catch (IOException e) {
			System.out.print("You do not have this image in the folder.");
		}
		
	}
	
	//this method add a monster to this specific hill
	public void addMonster(){
		
		//by doing so we are able to restrick the monster's position to not be that close to the edge
		//on both ends
		int xPos = this.x + gene.nextInt((int)(this.width*0.4)) + (int)(this.width*0.3);
		//this line means that the monster stands on the very edge of the hill, which is not good
		if(xPos >= (this.x + this.getWidth() - 25)){
			xPos = this.x + this.getWidth() - 25;
		}
		int yPos = this.getTopY() - 25;	//since the monster image has 25 pixel height, minus 25 so that the monster
								//is on the mountain
		//only when the hill is not at the end of the game will it has a mosnter
		if(xPos <= 6200){
			//since the hill and monster should be darken at the same time, take the num directly
			this.monster = new Monster(xPos, yPos, this.num);
			this.hasMonster = true;	//notify the program that there is a monster here
		}
	}
	
	//return if the hill has a monster
	public boolean ifHasMonster(){
		return this.hasMonster;
	}
	
	//this method return the monster
	public Monster getMonster(){
		return this.monster;
	}
	
	
	//this method return the current position of the hill
	//similar to getTopY
	public int getX(){
		return this.x;
	}
	
	//this method set the existing hill to be a gap
	public void setGap(){
		//if set to gap, its width should be within the below range
		this.width = this.index * 10 + 80;	//so the gap has its width within 80 and 160
		//change the statistics for the hill's height and width
		this.numOfH = 0;
		this.height = -300;
		//set the boolean so that it notify the program that this is a gap
		this.isGap = true;
	}
	
	//this method get the gap boolean for player to check. If the in range hill is a gap, the player 
	//keep falling down till it goes out of the screen
	public boolean getIsGap(){
		return this.isGap;
	}
	
	//this method help the computer know and calculate the total width of the map
	public int getWidth(){
		return this.width;
	}
	
	//this method return the preferred size of this component
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(this.width, this.height);
	}
	
	//this method return the y position of the top line of the hill. Since the whole window should be 480
	//and as the hill has a height of 15 * num of bricks in a line, we return 480 minus that
	public int getTopY(){
		return 480 - this.height;
	}
	
	//this method check if the player is right before the hill
	public boolean rightBefore(int x){
		if((x <= this.x + 6) && (x >= this.x - 6)){	//this method helps the program know if the player should
													//be block
			return true;
		}
		else{
			return false;
		}
	}
	
	//this method check if a player's position is on this certain hill
	//take player's x-position as parameter, and actually this is going to be some pixel before the player
	//so that the inrange is going to be more realistic
	public boolean inWidthRange(int x){	
		//so if the player's position is within the width of this hill
		if(this.x <= x && (this.x + this.width) >= x){
			return true;
		}
		else{
			return false;
		}
	}
	
	//this method, together with the above, help to see if the player can be on the next ground
	//this method returns true if the taken y is smaller than the top Y, meaning the taken y
	//is above the topY of the hill
	public boolean inHeightRange(int y){
		//if the taken parameter is smaller than the top Y of the hill, it can be stood on the ground
		if(getTopY() > y){
			return true;
		}
		else{
			return false;
		}
	}
	
	//this method set the status of this hill as out of the windows, therefore we move it to another list
	public void setExit(boolean exit){
		this.exit = exit;
	}
	
	//this method check if the wall exits the window
	public boolean getExit(){
		return this.exit;
	}
	
	//this method moves the entire hill. Since in the player part, we have already determine the sign of
	//the aNum taken in based on whether the player should move to left or right, we here just use
	//the add operation
	public void move(int aNum){
		this.x -= aNum;	//the hill moves to the left
		
		//if this hill has a monster, the monster should move together with the hill
		if(this.hasMonster){
			this.monster.move(aNum);
		}
	}
	
	//this method reset the hill's data back to origin
	public void reset(){
		this.x = this.startX;
		this.y = this.startY;
	}
	
	@Override
	public void paintComponent(Graphics g){
		BufferedImage image = this.brick;
		if(this.num == 2){
			image = this.grass;
		}
		
		//because we paint the wall from bottom to up, we minus the y position
		//because we paint the wall from left to right, we add the x position
		for(int i = 0; i < this.numOfW; i++){
			for(int j = 0; j < this.numOfH; j++){
				//this method draw the picture of the brick
				g.drawImage(image, this.x + i*25, this.y - j*25, 25, 25, this);
			}
		}
	}
	
	
	//tester for this class
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
	}
}
