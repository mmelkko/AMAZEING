package game.logics;
import game.control.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Abstract class that represents all Beings that can move in the maze.
 * Holds methods to get information of the maze and move in it and also
 * methods to get information of the state (if it can move and if it is alive)
 * of the Being.
 * 
 * @author Merituuli Melkko
 * */
public abstract class Being implements ActionListener{
	private int lives;
	private Square location;
	private Game game;
	private Timer timer;
	private long penaltyTime;

	/**
	 * @param lives that the Being will be given
	 * @param location that the Being is set to
	 * @param game that this Being is in
	 * */
	public Being(int lives, Square location, Game game){
		this.game = game;
		this.penaltyTime = 0;
		this.timer = new Timer(250, this);
		
		if (lives <= 0){
			this.lives = 1;
		} else {
			this.lives = lives;
		}

		try {
			this.setLocation(location);
		} catch (Exception e) {}
	}

	protected Game getGame(){
		return this.game;
	}

	/**
	 * @return int that is the amount of lives the Being has.
	 * */
	public int getLives(){
		return this.lives;
	}

	/**
	 * @return Square that is the Beings current location
	 * */
	public Square getLocation(){
		return this.location;
	}
	
	/**
	 * @return Timer that is in use for this Being
	 * */
	public Timer getTimer(){
		return this.timer;
	}

	/**
	 * @param location, which is set as this.location
	 * */
	public void setLocation(Square location){
		if (location == null) {
			this.location = null;
		} else if (this.getLocation() == null){
			this.location = location;
		} else if (this.getLocation() == location){
			return;
		} else {
			this.location.setBeing(null);
			this.location = location;
			this.location.setBeing(this);
		}
	}

	/**
	 * @return boolean if Being is allowed to move.
	 * */
	public boolean canMove(){
		if (this.penaltyTime > 0){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return boolean that tells, if the Being is alive
	 * */
	public boolean isAlive(){
		if (this.getLives() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * It hurts, so the lives of the Being are lessened. If the Being is not
	 * alive after the lessening of the lives, measures are taken depending
	 * if the Being is a Player or an Enemy. If the Being is still alive, it
	 * only has to suffer a time penalty.
	 * */
	public void itHurts(){
		this.lives -= 1;
		this.getGame().getCanvas().needsUpdateNow();
		if (!this.isAlive()){
			if (this instanceof Player){
				this.getGame().end();
			} else {
				try {
					this.getLocation().setBeing(null);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				this.game.getPlayer().changePoints(1000);
				this.timer.stop();
				this.game.playMusic();
			}
		} else {
			this.timePenalty(5);
		}
	}

	/**
	 * Moves the Being from this Square to another Square that is one
	 * of the neighbours of the Square the Being is in.
	 * 
	 * @param direction to which the Being intents to move to
	 * 
	 * @return boolean that tells if the move succeeded
	 * */
	public boolean move(Direction direction){
		if (this.getLocation().canIGoTo(direction) && this.canMove()){
			Square nextSquare = this.getLocation().getNeighbour(direction);
			Being being = nextSquare.getBeing();
			if (being == null){
				this.setLocation(nextSquare);
				return true;
			} else {
				if (being instanceof Player){
					being.itHurts();
				} else if (this instanceof Player){
					this.itHurts();
				}
			}
		}
		return false;
	}

	/**
	 * Puts this in penalty (starts a timer unless the being already has 
	 * a running timer)
	 * 
	 * @param time that this has the penalty for
	 * */
	public void timePenalty(long time){
		this.penaltyTime = time;
		if (!this.getTimer().isRunning()){
			this.getTimer().start();
		}
	}

	/*Decreases the penalty time left (if this is a Player, when the Player
	 * gains the ability to move again (penaltytime is 1 when the event comes,
	 * then decreased to 0 and the Being can move again) the whole canvas
	 * is updated (mostly to get the players icon to refresh, but also as
	 * a precaution*/
	@Override
	public void actionPerformed(ActionEvent event) {
		if (this.penaltyTime > 0){
			this.penaltyTime--;
			if (this.canMove() && this instanceof Player){
				this.getTimer().stop();
				this.getGame().getCanvas().needsUpdateNow();
			}
		}
	}

}
