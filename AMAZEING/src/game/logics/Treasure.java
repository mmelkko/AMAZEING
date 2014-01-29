package game.logics;

/**
 * A Treasure is a valuable item placed to a square, that when picked up by
 * a player increases the points of the Player by the worth it has.
 * 
 * @extends Item
 * 
 * @author Merituuli Melkko
 * */
public class Treasure extends Item {

	/**
	 * @param location that the treasure is placed to
	 * @param worth that the Treasure has, if 0, the worth is set to be 400.
	 * @param number that the Treasure has, if 0, the number is set to be 100.
	 * */
	public Treasure(Square location, int worth, int number){
		super((worth != 0 ? worth : 400), (number != 0 ? number : 100));
		this.placeToSquare(location);
	}

	/*when picked up the treasure is not added to the items to the player
	 * permanently, so it is removed after being picked up.*/
	@Override
	public void setOwner(Being being) {
		super.setOwner(being);
		if (being instanceof Player){
			((Player) this.getOwner()).changePoints(this.getWorth());
		}
		if (being != null){
			super.setOwner(null);
		}
	}

}
