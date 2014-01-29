package game.logics;

/**
 * A Bananapeel is an item that makes any Being who comes into the square stop
 * all movement for one second (the Being "slips and falls" because of
 * the Bananapeel).
 * 
 * @extends Item
 * 
 * @author Merituuli Melkko
 * */
public class Bananapeel extends Item {
	/**int that is the worth of a bananapeel*/
	public static final int WORTH = 50;

	public Bananapeel() {
		super(Bananapeel.WORTH, 2);
	}

	/*if the location is not null and the bananapeel has no owner, the
	 * being that "tries to pick the bananapeel up" gets a time penalty.*/
	@Override
	public void setOwner(Being being){
		if (this.getLocation() == null || this.getOwner() != null){
			super.setOwner(being);
		} else if (this.getLocation() != null){
			being.timePenalty(10);
		} else {
			this.placeToSquare(null);
		}
	}

	@Override
	public String toString(){
		return "A bananapeel.<br>Stepping on it<br>makes one fall<br>" +
				"and stop for<br>a while.";
	}

}
