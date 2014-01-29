package game.logics;
import java.util.ArrayList;

/**
 * Itemslot is a container of sorts for the specified types of items that
 * are allowed for the Player to carry around. Consists of "slots of items"
 * (ArrayList<Item>) that hold the items.
 * 
 * @author Merituuli Melkko
 * */
public class Itemslot {
	/**int that represents the number of slots there is in this itemslot*/
	public static final int NUMBER_OF_SLOTS = 3;
	private ArrayList<Item> keySlot;
	private ArrayList<Item> bananaSlot;
	private ArrayList<Item> bombSlot;
	private Player owner;
	private boolean needsRefresh;
	
	/**
	 * @param owner that owns this itemslot
	 * */
	public Itemslot(Player owner){
		this.keySlot = new ArrayList<Item>();
		this.bananaSlot = new ArrayList<Item>();
		this.bombSlot = new ArrayList<Item>();
		this.owner = owner;
		this.needsRefresh = false;
	}
	
	/**
	 * @return int that is the number of items in the "slot" wanted. If
	 * -100, the slot is null, as in there is no slot for that number.
	 * 
	 * @param number that is the number of the "slot" that is wanted.
	 * */
	public int getNumberOfItemsInSlot(int number){
		ArrayList<Item> slot = this.getSlotIn(number);
		if (slot != null){
			return slot.size();
		} else {
			return -100;
		}
	}
	
	/**
	 * @param number out of which the slot is gotten. (Every slot has a
	 * determined number).
	 * 
	 * @return ArrayList<Item> that is the "slot" in the desired number. If
	 * null, it's Array Index Out of Bounds!
	 * */
	public ArrayList<Item> getSlotIn(int number){
		if (number == 1){
			return this.keySlot;
		} else if (number == 2){
			return this.bananaSlot;
		} else if (number == 3){
			return this.bombSlot;
		} else {
			System.err.println("*From Itemslot: Array Index Out of Bounds!!*");
			return null;
		}
	}
	
	/**
	 * Adds item to the slots that holds their kind of items.
	 * 
	 * @param item that is added.
	 * */
	public void addItem(Item item){
		if (item instanceof Key){
			this.keySlot.add(item);
		} else if (item instanceof Bananapeel){
			this.bananaSlot.add(item);
		} else if (item instanceof Bomb){
			this.bombSlot.add(item);
		}
		this.needsRefresh = true;
	}
	
	/**
	 * @return boolean if the itemslot needs refreshing
	 * */
	public boolean needsRefresh(){
		return this.needsRefresh;
	}
	
	/**
	 * The slot has been updated, so there is no need for refresh anymore.
	 * */
	public void update(){
		this.needsRefresh = false;
	}
	
	/**
	 * Uses one item from the desired slot. The item is placed to the square
	 * the owner of this is in currently and removed from the slot it is in.
	 * 
	 * @param number of the slot out of which the item is got from and
	 * removed. If the number is not 'in array bounds', this method will
	 * not do the things mentioned above.
	 * */
	public void useItemFromSlot(int number){
		ArrayList<Item> slot = this.getSlotIn(number);
		if (slot != null && !slot.isEmpty()){
			slot.get(0).placeToSquare(this.owner.getLocation());
			slot.remove(0);
			this.needsRefresh = true;
		}
	}

}
