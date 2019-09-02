import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;

//this is just a small class that help to reduce the redundant cancel background method when creating a 
//JButton
public class Button extends JButton{

	private Font font;
	
	public Button(String text){
		super(text);
		
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
		
		//set fonts for all button
		this.setFont(font);
		this.setForeground(Color.WHITE);
		//since we have keyBoard control, we need to set all other JComponent's focusable as false
		this.setFocusable(false);
		this.setMargin(new Insets(0, 0, 0, 0));	//exclude the margin
		this.setBorder(null);
		this.setSize(180, 30);	//set the same size for all button
		
		this.setBorderPainted(false); 
        this.setContentAreaFilled(false); 
        this.setFocusPainted(false); 
        this.setOpaque(false);
	}
	
	
}
