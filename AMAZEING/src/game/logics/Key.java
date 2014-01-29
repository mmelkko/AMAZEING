package game.logics;

/**
 * A Key has the ability to open a door that is in the square next to the
 * square that the user is in. After a key is used, it dissappears.
 * 
 * @extends Item
 * 
 * @author Merituuli Melkko
 * */
public class Key extends Item {
	/**int that states the worth of a key*/
	public static final int WORTH = 100;
	
	public Key(){
		super(Key.WORTH, 1);
	}
	
	/*Whether the key is used next to a door or not, when it is used, it will
	 * disappear.*/
	@Override
	public void placeToSquare(Square square){
		Square withdoor = square.getNearbyDoor();
		if (withdoor != null){
			withdoor.setDoor(false);
		}
		super.placeToSquare(null);
	}
	
	@Override
	public String toString(){
		return "A key. Used for<br>opening doors<br>instantly. Has to<br>"
				+ "be used next to<br>a door.";
	}
	
}
