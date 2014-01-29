package game.view;
import game.control.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/**
 * BasePanel is the base for the panels used for the main views shown in this
 * program. The components that are added are specified in the constructor and
 * added to the BasePanel when requested.
 * 
 * @implements ActionListener
 * @extends JPanel
 * 
 * @author Merituuli Melkko
 * */
public class BasePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private ArrayList<BaseButton> buttons;
	private ImageIcon image;
	private JPanel innerpanel;
	private boolean hasBeenSet;
	private Game game;

	/**
	 * @param game that this is used in
	 * @param buttonlist that is added to this
	 * @param image that is added to this
	 * @param innerpanel that is added to this
	 * */
	public BasePanel(Game game, ArrayList<BaseButton> buttonlist, 
			ImageIcon image, JPanel innerpanel){
		super();
		this.buttons = buttonlist;
		this.image = image;
		this.innerpanel = innerpanel;
		this.hasBeenSet = false;
		this.game = game;

		this.setPreferredSize(new Dimension(500, 400));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
	}

	/**
	 * Places all the components to the spots that are reserved for them if
	 * they haven't been set yet.
	 * */
	public void placeAllComponents(){
		if (!this.hasBeenSet){
			if (this.image != null){
				this.add(Box.createRigidArea(new Dimension(0,15)));
				JLabel label = new JLabel(this.image);
				label.setAlignmentX(CENTER_ALIGNMENT);
				this.add(label);
			}

			if (this.innerpanel != null){
				this.add(Box.createRigidArea(new Dimension(0,15)));
				this.add(innerpanel);
			}

			if (this.buttons != null){
				this.add(Box.createRigidArea(new Dimension(0,30)));
				for (int a = 0; a < this.buttons.size(); a++){
					BaseButton button = this.buttons.get(a);
					button.setAlignmentX(CENTER_ALIGNMENT);
					button.setBorder(BorderFactory.createRaisedBevelBorder());
					button.addActionListener(this);
					this.add(button);
					this.add(Box.createRigidArea(new Dimension(0,15)));
				}
			}

			this.hasBeenSet = true;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent act) {
		String s = act.getActionCommand();
		if (s.contains("Start")){
			this.game.beginGame();
		} else if (s.contains("Next")){
			this.game.play();
		} else if (s.contains("Help") || s.contains("Continue")){
			this.game.pause();
		} else if (s.contains("Exit")){
			System.exit(0);
		} else if (s.contains("Save")){
			this.game.end();
		} else if (s.contains("Results")){
			this.game.results();
		} else if (s.contains("Return")){
			new Game();
			this.game.getWindow().dispose();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		Image background = MazeCanvas.maze.getImage();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (background != null){
			g2.drawImage(background, 6, 0, null);
		}
	}
}
