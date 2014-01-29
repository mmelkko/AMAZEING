package game.logics;
/**
 * Items are objects that can be placed to a square or held by a being. Each
 * item has a worth and number (the number is to get the type of the item,
 * sort of).
 * 
 * @author Merituuli Melkko
 * */
public abstract class Item {
	private Being owner;
	private Square location;
	private int worth, number;

	/**
	 * @param worth that is set to be the worth of this
	 * @param number that is set to be the number of this
	 * */
	public Item(int worth, int number){
		this.owner = null;
		this.location = null;
		this.worth = worth;
		this.number = number;
	}

	/**
	 * @return Square that is the location this is in
	 * */
	public Square getLocation(){
		return this.location;
	}
	
	/**
	 * @return int that is the number of this
	 * */
	public int getNumber(){
		return this.number;
	}

	/**
	 * @return Being that is the owner of this
	 * */
	public Being getOwner(){
		return this.owner;
	}

	/**
	 * @return int that is the worth of this
	 * */
	public int getWorth(){
		return this.worth;
	}

	/**
	 * When the item is picked up, it is removed from the possible square
	 * it was in.
	 * 
	 * @param being that is set as the owner of this. If null, removing the
	 * owner.
	 * */
	public void setOwner(Being being) {
		if (this.getLocation() != null){
			this.getLocation().setItem(null);
		}
		this.owner = being;
		if (this.getOwner() != null  && this.getOwner() instanceof Player 
				&& !(this instanceof Treasure)){
			((Player) this.getOwner()).addItem(this);
		}
	}

	/**
	 * When the item is placed to a square, it is removed from the possible
	 * previous one and from the possible owner it has.
	 * 
	 * @param square that this Item is placed to.
	 * */
	public void placeToSquare(Square square){
		if (this.getLocation() != null){
			this.getLocation().setItem(null);
		} else if (this.getOwner() != null){
			this.setOwner(null);
		}
		this.location = square; 

		if (this.getLocation() != null){
			this.getLocation().setItem(this);
		}
	}

}
