package game.logics;
import game.control.Game;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Enemies are Beings that move almost randomly around in the maze according
 * every time their build-in timer ticks.
 * 
 * @extends Being
 * 
 * @author Merituuli Melkko
 * */
public class Enemy extends Being{
	private static Random rand = new Random();
	private Direction latestdirection;

	/**
	 * @param location that this is set to
	 * @param game that this is in
	 * */
	public Enemy(Square location, Game game) {
		super(1, location, game);
		this.latestdirection = null;
	}

	/**
	 * Moves this to a random direction in which this can go to from 
	 * this.location while avoiding the parameter direction
	 * 
	 * @param notHere is the direction that this doesn't want to move to. 
	 * If null, moves to any direction possible.
	 * */
	public void moveToRandomDirection(Direction notHere){
		Direction[] directions = Direction.values();
		ArrayList<Direction> allowed = new ArrayList<Direction>();

		for (int a = 0; a < directions.length; a++){
			Direction direction = directions[a];
			if (this.getLocation().canIGoTo(direction) && (notHere == null ||
					direction != notHere)){
				allowed.add(direction);
			}
		}

		if(!allowed.isEmpty()){
			Direction direction = allowed.get(rand.nextInt(allowed.size()));
			if (this.move(direction)){
				this.latestdirection = direction;
			} else {
				this.latestdirection = direction.opposite();
			}
		} else {
			if (notHere != null){
				this.moveToRandomDirection(null);
			} else {
				System.err.println("Enemy-error: I can't move anywhere!!?!??");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		Direction direction = null;
		if (this.latestdirection != null){
			direction = this.latestdirection.opposite();
		}
		this.moveToRandomDirection(direction);
	}

}
