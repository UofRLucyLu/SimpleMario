
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.WindowConstants;


//this is a small class that stored all things needed when the game starts

public class Game extends JLayeredPane{
	private Canvas canvas;
	private JLabel time, score;
	
	public Game(int index, Button pause){
		//all components needed in this class
		this.time = new JLabel();
		this.score = new JLabel();
		//set other component's focusable as false
		this.time.setFocusable(false);
		this.score.setFocusable(false);
		//put them on certain layer
		this.add(this.time, JLayeredPane.MODAL_LAYER);
		this.add(this.score, JLayeredPane.MODAL_LAYER);
		this.add(pause, JLayeredPane.MODAL_LAYER);
		
		this.canvas = new Canvas(index);
		//this.canvas.setFocusable(true);
		
		this.canvas.setTimeLabel(this.time);
		this.canvas.setScoreLabel(this.score);
		this.canvas.setBounds(0, 0, 640, 480);
		this.add(this.canvas, JLayeredPane.DEFAULT_LAYER);
		this.setBounds(0, 0, 640, 480);
		
		//this.setFocusable(true);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		Button pause = new Button("PAUSE");
		Game game = new Game(1, pause);
		game.start();
		frame.setVisible(true);
		frame.add(game);
		
	}
	
	//this method help check if the canvas's game is still on
	public boolean isOver(){
		return this.canvas.getOver();
	}
	
	//get the lables in this panel
	public JLabel getScore(){
		return this.score;
	}
	
	public JLabel getTime(){
		return this.time;
	}
	
	//return the total label here
	public JLabel getTotal(){
		return this.canvas.getTotalLabel();
	}
	
	
//	public boolean isFocusTraversable(){
//		return false;
//	}
	
	public void start(){
		this.canvas.start();
		
		//this line is soooooo crucial!
		//Since there are so many components, I use the grabFocus method so that regardless whether other
		//components are active or not, the canvas grab the focus
		this.canvas.grabFocus();
	}
	//method that stop the canvas
	public void stop(){
		this.canvas.stop();
	}
	//method that stop the music
	public void stopMusic(){
		this.canvas.stopMusic();
	}
	

	
}
