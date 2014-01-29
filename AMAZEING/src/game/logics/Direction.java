package game.logics;

/**
 * A enumeration to represent the four directions.
 * 
 * @author Merituuli Melkko
 * */
public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	/**
	 * @return Direction that is the opposite to this
	 * */
	public Direction opposite(){
		switch (this) {
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		default: return null;
		}
	}
	
}
