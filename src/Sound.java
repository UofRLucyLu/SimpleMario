/*Name: Xin Lu
 * NetID: xlu32
 * Assign_Num: Project 04
 * Lab section: TR 12:30pm - 01:45 pm
*/

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
    private Clip clip;
    
    //constructor that takes the path of the sound file
    public Sound(String path) {
        try {
        	//store the file from the parameter
            File file = new File(path);
            //get the audio input stream
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            //get the clip and open it
            this.clip = AudioSystem.getClip();
            this.clip.open(sound);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();	//don't know waht this exception is...use the system to just simply catch
            throw new RuntimeException("MalformedURLException");
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("UnsupportedAudioFileException");
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("LineUnavailableException");
        }
    }
    
    //this method tells the clip that it should play at the first position of the music
    public void play(){
        this.clip.setFramePosition(0);  // Must always rewind!
        this.clip.start();
    }
    //this method is for the background music
    //so once this method is initialized, the background music will keep looping
    public void loop(int aNum){
    	if(aNum != -1){
    		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    	}
    	else{
    		this.clip.loop(-1);
    	}
    }
    //stop this music
    public void stop(){
        this.clip.stop();
    }
    
    
}