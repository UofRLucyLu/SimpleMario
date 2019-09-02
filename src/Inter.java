import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Inter extends JFrame implements ActionListener{
	
	private Button start, quitAll, level1, level2, pause, back, quit;
	private Opening open;//this variable stores the open page of the game
	private Game game;	//this is the panel that stores all game stuff
	private PausePage page;
	
	//this is a timer that is not align with the one in the game part, this is to make sure that
	//the frame keep track of whether the game is over and therefore change the gui interface
	private Timer timer = new Timer(1000, this);
	
	
	public Inter(){
		this.timer.addActionListener(this);
		
		//initialize all buttons needed here
		this.start = new Button("START");
		this.quitAll = new Button("QUIT");
		this.level1 = new Button("LEVEL 1");
		this.level2 = new Button("LEVEL 2");
		this.pause = new Button("PAUSE");
		this.back = new Button("RETURN");
		this.quit = new Button("QUIT");
		
		//add listener to the class
		this.start.addActionListener(this);
		this.quitAll.addActionListener(this);
		this.level1.addActionListener(this);
		this.level2.addActionListener(this);
		this.pause.addActionListener(this);
		this.back.addActionListener(this);
		this.quit.addActionListener(this);
		
		//set location for pause
		this.pause.setLocation(320, 10);
		
		//initialize the two useful panel
		this.open = new Opening(start, quitAll, level1, level2);
		this.page = new PausePage(quit, back);
		
		this.setSize(640, 480);
		
		//add the open page to the frame
		this.add(this.open);
		this.setVisible(true);
		
		//set default close oepration and visible and etc
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.timer.start();
	}
	
	public static void main(String[] args){
		Inter run = new Inter();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//if it is the timer that cause the event, check here
		if(e.getSource().equals(this.timer)){
		
			//since whenever the timer is keep track, we do not need to check the source
			//if the game is over, it must be on the game page
			if(this.game != null){	//check if the game is initialized
				if(this.game.isOver()){
					
					this.remove(this.game);
					
					//add summary to the page
					//first get the total score
					this.page.setLabels(this.game.getScore(), this.game.getTime(), this.game.getTotal());
					
					this.add(this.page);	//add the pause page
					
					this.repaint();
				}
			}
		}
		
		else{
			Button temp = (Button) e.getSource();	//get which button is working
		
			if(temp.equals(this.start)){
				this.open.removeOpening();	//if it is the start button being pressed, remove all opening
				this.open.addLevelSelect();	//add the level Selecting page
				
				this.repaint();
			}
			
			else if(temp.equals(this.quitAll)){
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			
			else if(temp.equals(this.level1)){
				this.open.removeLevelSelect();	//remove the level select page
				//since we start the game, we initialize the game variable, remove the menu panel and add
				//the game panel back
				this.game = new Game(1, this.pause);
				
				//set the pause page
				this.page.setIndex(1);
	
				this.remove(this.open);
				this.add(this.game);
				
				//once the game is added, stop the open music
				this.open.stopMusic();
				this.game.start();
				
			}
			
			else if(temp.equals(this.level2)){
				this.open.removeLevelSelect();	//remove the level select page
				//since we start the game, we initialize the game variable, remove the menu panel and add
				//the game panel back
				this.game = new Game(2, this.pause);
				
				//set the pause page
				this.page.setIndex(2);
				
				this.remove(this.open);
				this.add(this.game);
				
				//once the game is added, stop the open music
				this.open.stopMusic();
				this.game.start();
				
			}
			
			else if(temp.equals(this.pause)){
				//stop the game first
				this.game.stop();
				
				this.remove(this.game);
				this.add(this.page);
				
				this.repaint();
			}
			else if(temp.equals(this.back)){
				this.remove(this.page);
				
				//add back the game and start the game again
				this.add(this.game);
				this.game.start();
				
				this.repaint();
			}
			else if(temp.equals(this.quit)){
				this.game.stopMusic();	//stop the bgm
				
				//if this button is used when game is end:
				if(this.game.isOver()){
					this.page.removeLables();
				}
				//since the game is over, set the game to null to prevent running this method again
				this.game = null;
				
				this.remove(this.page);
				
				this.open.addOpening();
				this.add(this.open);
				this.open.startMusic();	//start the menu music
				
				this.repaint();
			}
			
		}
		
		this.setVisible(true);
		
	}


	
}
