/*Name: Xin Lu
 * NetID: xlu32
 * Assign_Num: Project 04
 * Lab section: TR 12:30pm - 01:45 pm
*/

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;



public class Monster extends JComponent{
	
	//store the image of the monster
	private BufferedImage monster;
	private BufferedImage powerUp, coin;
	private int x, y, num;
	private Player player;
	
	private Sound Kick, Bump, powerUpSound, coinSound;
	
	private RescaleOp op = new RescaleOp(.5f, 0, null);
	
	//this boolean tell the program that this is a power up mushroom
	private boolean isPowerUp = false;
	private boolean isCoin = false;
	
	private int count;	//this is used to control the number of time the image twinkle
	
	private boolean kill;	//this variable check if the monster is being killed
	
	
	public Monster(int x, int y, int num){
		this.num = num;
		
		//each monster when dead will twinkle 2 or 3 times
		this.count = 4;
		
		this.x = x;
		this.y = y;
		
		//since this class stores both mushroom and monster, we need a boolean
		
		//initialize the image
		//add image to this monster and paint itself
		try {
			this.monster = ImageIO.read(new File("Picture/Monster.png"));
		} catch (IOException e){
			System.out.print("You do not have this image in the folder.");
		}
		
		//initialize the image
		//add image to this mushroom and paint itself
		try {
			this.powerUp = ImageIO.read(new File("Picture/PowerUp.png"));
			this.coin = ImageIO.read(new File("Picture/Coin.png"));
		} catch (IOException e){
			System.out.print("You do not have this image in the folder.");
		}
		
		//at first the monster is not killed
		this.kill = false;
		
		this.Kick = new Sound("Music/Kick.wav");	//when monster dead and player not lose blood
		this.Bump = new Sound("Music/Bump.wav");	//monster dead and player lose blood
		this.powerUpSound = new Sound("Music/PowerUp.wav");
		this.coinSound = new Sound("Music/Coin.wav");
	}
	
	//this method tell this monster itself that it is a mushroom that add player's skill
	public void isPowerUp(){
		this.isPowerUp = true;
	}
	
	public void isCoin(){
		this.isCoin = true;
	}
	
	//get the x and y position of the monster and change them
	public void move(int aNum){
		this.x -= aNum;
	}
	
	//this method help the other class know if the monster finished twinkle
	public int getCount(){
		return this.count;
	}
	
	//if the player stoods on top of the monster, the monster is dead and add score to player
	//if the player bump into the monster, the monster is killed by player's weight but player
	//also receive attack from the monster
	public void ifKill(){
		//check if the player is in range of the monster
		//if the front of the player is almost the same as that of the monster, notify
		//if the back of the player is almost the
		if(this.player.getX() + 45 >= this.x && this.player.getX() + 5 <= this.x + 25){
			
			//if this is a monster, execute the below
			if(!this.isPowerUp && !this.isCoin){
				//in this line, if the player's bottom to touch the monster, the monster is killed and no longer
				//draw on the canvas
				if((this.player.getY() + 50) >= this.y){
					//if the player is not jumping, it means the monster is killed when player go right into it
					//and also the monster is not killed before
					if(!(this.player.getJump() || this.player.getDrop()) && (!this.kill)){
						this.player.addHealth(-1);
						
						this.Bump.play();	//lose blood, play this sound
					}
					else{
						//else, play the kick sound
						this.Kick.play();
						//since the player kill a mosnter without losing blood, add score
						if(!this.kill){
							this.player.addScore(50);
						}
					}
					this.kill = true;
				}
			}
			
			else if(this.isPowerUp || this.isCoin){
				//if the player's bottom touch the mushroom
				//since the mushroom's actual y is a little bit different, use a new varaible
				int tempY = this.y - 75;
				if(this.player.getY() + 50 >= tempY && this.player.getY() <= tempY){
					
					if(this.isPowerUp){
						this.player.setPowerUp(true);	//allow double jump
						if(!this.kill){
							this.powerUpSound.play();	//play the powerup sound
							this.player.addScore(200);
						}
					}
					//in the other case, it is a coin
					else{
						if(!this.kill){
							this.coinSound.play();
							this.player.addScore(10);
						}
					}
					
					this.kill = true;	//and the mushroom disappear
				}
				//or its head touch the mushroom
				else if(this.player.getY() <= tempY + 20 && this.player.getY() + 50 >= tempY){
					//same as above
					if(this.isPowerUp){
						this.player.setPowerUp(true);	//allow double jump
					
						if(!this.kill){
							this.player.addScore(200);
							this.powerUpSound.play();
						}
					}
					else{
						if(!this.kill){
							this.player.addScore(10);
							this.coinSound.play();
						}
					}
					
					this.kill = true;	//and the mushroom disappear
				}
			}
		}
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(25, 25);
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		
		//paint the monster image
		//only if the monster is alive will the program keep painting it
		if(!kill){
			if(this.isPowerUp){
				//we change the y coordinate for power up because we want the mushroom to be in the air
				g.drawImage(this.powerUp, this.x, this.y - 75, 25, 25, this);
			}
			else if(this.isCoin){
				g.drawImage(this.coin, this.x, this.y - 75, 25, 25, this);
			}
			//otherwise, draw the monster as usual
			else{
				g.drawImage(this.monster, this.x, this.y, 25, 25, this);
			}
		}
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
}
