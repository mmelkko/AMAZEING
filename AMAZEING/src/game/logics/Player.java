package game.logics;
import game.control.*;

/**
 * Player is a being that if not controlled from outside will just stand in
 * one spot in the maze with all the items it just happens to have in its
 * itemslot. To make the player actually functioning, a GameKeyListener is
 * needed to listen the commands from the user.
 * 
 * @see GameKeyListener
 * 
 * @extends Being
 * 
 * @author Merituuli Melkko
 * */
public class Player extends Being {
	private int points;
	private Itemslot itemslot;

	/**
	 * @param game that this is in.
	 * */
	public Player(Game game) {
		super(3, null, game);
		this.points = 0;
		this.itemslot = new Itemslot(this);
	}

	/**
	 * @return Itemslot that is the itemslot of this
	 * */
	public Itemslot getItemslot(){
		return this.itemslot;
	}

	/**
	 * @return int that is the amount of points this has
	 * */
	public int getPoints(){
		return this.points;
	}

	/**
	 * @param item that is added to the itemslot.
	 * */
	public void addItem(Item item){
		this.itemslot.addItem(item);
	}

	/**
	 * Buys the desired item if the player has enough points to pay for it.
	 * 
	 * @param item that is bought
	 * */
	public void buy(Item item){
		if (this.getPoints() >= item.getWorth()){
			item.setOwner(this);
			this.changePoints(-item.getWorth());
		}
	}

	/**
	 * Changes the points by the amount, but takes care that points don't
	 * go negative.
	 * 
	 * @param amount the point is changed. 
	 * */
	public void changePoints(int amount){
		if (this.points + amount < 0){
			this.points = 0;
		} else {
			this.points += amount;
			this.getGame().getWindow().updatePoints();
		}
	}

	/**
	 * Checks if Player is in goal and does what needs to be done, if that
	 * is true.
	 * 
	 * @return boolean if Player is in goal
	 * */
	public boolean isInGoal(){
		if (this.getLocation().isGoal()){
			this.getGame().levelComplete();
			return true;
		}
		return false;
	}

	/**
	 * @param number of the slot the item is used from.
	 * */
	public void useItemFromSlot(int number){
		this.itemslot.useItemFromSlot(number);
	}

}
