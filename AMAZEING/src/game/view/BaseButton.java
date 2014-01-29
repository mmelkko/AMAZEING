package game.view;

import java.awt.*;
import javax.swing.JButton;

/**
 * BaseButton is the base for all buttons used in the program. They have
 * a specific size and look and feel.
 * 
 * @extends JButton
 * 
 * @author Merituuli Melkko
 * */
public class BaseButton extends JButton {
	private static final long serialVersionUID = 1L;

	/**
	 * @param text that is added to this
	 * */
	public BaseButton(String text){
		super(text);
	}
	
	@Override
	public Font getFont(){
		return new Font("Verdana", Font.BOLD, 10);
	}
	
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(125,30);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.setColor(Color.WHITE);
		g2.drawString(this.getText(), 
				this.getWidth()/2-this.getText().length()*3, 
				this.getHeight()/2+5);
		if (!this.isEnabled()){
			g2.setColor(Color.RED);
			g2.drawLine(0, 0, this.getWidth(), this.getHeight());
		}		
	}
	
}
