package game.logics;

/**
 * An apple is a less-of-worth Treasure and has directly 
 * all the functionalities of that upperclass.
 * 
 * @extends Treasure
 * 
 * @author Merituuli Melkko
 * */
public class Apple extends Treasure {
	/**int that represents the worth of an apple*/
	public static final int WORTH = 200;

	/**
	 * @param square that the Apple is set to.
	 * */
	public Apple(Square square) {
		super(square, Apple.WORTH, 4);
	}
	
	/*The original intention of Apple was to be more than this, but since
	 * the time wasn't enough and the programming wouldn't have been easy*/
	
}
