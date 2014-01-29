package game.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A TimeKeeper is the ActionListener used for the main timer of the game.
 * It executes and refresh all things that need to be refreshed or checked
 * 20 times in a second.
 * 
 * @implements ActionListener
 * 
 * @author Merituuli Melkko
 * */
public class Timekeeper implements ActionListener {
	private long begintime, latesttime, minutes, seconds;
	private Game game;

	/**
	 * @param Game that this Timekeeper is used in.
	 * */
	public Timekeeper(Game game){
		this.begintime = 0;
		this.latesttime = 0;
		this.minutes = 0;
		this.seconds = 0;
		this.game = game;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		/* If the game is in motion, asks the player if it is in the goal,
		 * decreases the points of the player by one and calls repaint 
		 * on the canvas*/
		if (this.game.isInMotion()){
			this.game.getPlayer().changePoints(-1);
			this.game.getPlayer().isInGoal();
			this.game.getCanvas().repaint();
		}
		
		/* Everything down from here is just used for keeping track of the time
		 * the player has used on this game and updating the time on the
		 * bottom pane.*/
		if (this.begintime == 0){
			this.begintime = System.currentTimeMillis();
		}
		
		this.latesttime = event.getWhen();
		long second = (this.latesttime-this.begintime)/1000;

		if (second/60 > this.minutes){
			this.minutes++;
		}
		
		this.seconds = second - this.minutes*60;
		this.game.getWindow().updateTime(this.minutes, this.seconds);
	}

}
