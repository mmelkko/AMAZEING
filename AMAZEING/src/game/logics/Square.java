package game.logics;
import java.util.ArrayList;
import java.util.List;

/**
 * A Square represents one "room" in the maze. This "room" can have walls into
 * some directions, a door, the goal, a being and a item. A Square has to
 * have a specified place in a MazeGrid to function properly.
 * 
 * @see MazeGrid
 * 
 * @author Merituuli Melkko
 * */
public class Square {
	private int x, y, doorcount;
	private Being being;
	private MazeGrid grid;
	private List<Direction> walls;
	private boolean hasDoor, isGoal, needsRefresh;
	private Item item;

	/**
	 * @param x the square is in
	 * @param y the square is in
	 * @param grid the square is in
	 * @param walls the square has
	 * */
	public Square(int x, int y, MazeGrid grid, ArrayList<Direction> walls){
		this.x = x;
		this.y = y;
		this.being = null;
		this.grid = grid;
		this.walls = walls;
		this.hasDoor = false;
		this.isGoal = false;
		this.doorcount = 0;
		this.item = null;
		this.needsRefresh = true;
	}

	/**
	 * @return Being that recides in this. If null, there are none.
	 * */
	public Being getBeing(){
		return this.being;
	}

	/**
	 * @return int that is the doorcount (how many times the door has to be
	 * tried so that it opens)
	 * */
	public int getDoorcount(){
		return this.doorcount;
	}

	/**
	 * @return Item that is in this square. If null, there are none.
	 * */
	public Item getItem(){
		return this.item;
	}

	/**
	 * @return Square that is one of this's neighbours and has a door
	 * */
	public Square getNearbyDoor(){
		Direction directions[] = Direction.values();
		for (int a = 0; a < directions.length; a++){
			Square square = this.getNeighbour(directions[a]);
			if (square != null && square.hasDoor() && !this.walls.contains(directions[a])){
				return square;
			}
		}
		return null;
	}

	/**
	 * @param Direction in which the neighbour is gotten from
	 * 
	 * @return Square that is the neighbour of this in Direction direction
	 * */
	public Square getNeighbour(Direction direction){
		return this.grid.getNeighbourOf(this.x, this.y, direction);
	}

	/**
	 * Sets this as goal
	 * */
	public void setAsGoal(){
		this.isGoal = true;
	}

	/**
	 * @param Being that is tried to be set to recide in this
	 * */
	public void setBeing(Being being){
		if (being == null){
			this.being = null;
		} else {
			if (this.being != null){
				System.err.println("This should never happen! Two beings," +
				" one square");
			} else {
				this.being = being;
				if (this.being.getLocation() != this){
					this.being.setLocation(this);
				}
				if (this.getItem() != null){
					this.getItem().setOwner(this.being);
				}
			}
		}
		this.needsUpdateNow();
	}

	/**
	 * @param boolean that tells, whether the door is to be set or removed
	 * */
	public void setDoor(boolean door){
		if (door){
			this.doorcount = 9;
		} else {
			this.doorcount = 0;
		}
		this.hasDoor = door;
		this.needsUpdateNow();
	}

	/**
	 * If this already has an item, the item is not set.
	 * 
	 * @param Item that is set as the item of this
	 * */
	public void setItem(Item item){
		if (item == null){
			this.item = null;
		} else {
			if (this.item == null){
				this.item = item;
			}
		}
		this.needsUpdateNow();
	}

	/**
	 * @param Direction to which a wall is set
	 * */
	public void setWall(Direction direction){
		if (!this.walls.contains(direction)){
			this.walls.add(direction);
			this.needsUpdateNow();
		}
	}

	/**
	 * Collects all the beings in the nearby (the neighbours in all available
	 * direction and then their neighbours that are available except those who
	 * are already checked for) squares of this.
	 * 
	 * @return ArrayList<Being> that holds all the nearby beings.
	 * */
	public ArrayList<Being> collectNearbyBeings(){
		ArrayList<Being> beings = new ArrayList<Being>();
		ArrayList<Square> squares = new ArrayList<Square>();
		squares.add(this);

		Direction[] directions = Direction.values();
		for (int a = 0; a < directions.length; a++){
			Direction direction = directions[a];
			Square neighbour = this.getNeighbour(direction);
			if (neighbour != null && this.canIGoTo(direction)){
				squares.add(neighbour);
				for (int b = 0; b < directions.length; b++){
					Square nextneighbour = neighbour.getNeighbour(directions[b]);
					if (nextneighbour != null 
							&& neighbour.canIGoTo(directions[b]) 
							&& !squares.contains(nextneighbour)){
						squares.add(nextneighbour);
					}
				}
			}
		}
		for (int c = 0; c < squares.size(); c++){
			Being being = squares.get(c).getBeing();
			if (being != null){
				beings.add(being);
			}
		}

		return beings;
	}

	/**
	 * Puts the square to notice that it needs update.
	 * */
	public void needsUpdateNow(){
		this.needsRefresh = true;
	}

	/**
	 * Asks the Square, if one can go from this into Direction direction
	 * 
	 * @param Direction that one would like to go to
	 * 
	 * @return boolean that tells, if one can go to that Direction
	 * */
	public boolean canIGoTo(Direction direction){
		Square neighbour = this.getNeighbour(direction);
		if (neighbour == null || this.walls.contains(direction) 
				|| neighbour.hasDoor()){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return boolean if this has a door
	 * */
	public boolean hasDoor(){
		return this.hasDoor;
	}

	/**
	 * @return boolean if this is a goal
	 * */
	public boolean isGoal(){
		return this.isGoal;
	}

	/**
	 * @return boolean if this needs refresh
	 * */
	public boolean needsRefresh(){
		return this.needsRefresh;
	}

	/**
	 * Tries to open the door once.
	 * 
	 * @return int how many times the door has to be tried to open for it to
	 * open
	 * */
	public int openDoor(){
		this.doorcount--;
		if (this.doorcount <= 0){
			this.hasDoor = false;
		}
		this.needsRefresh = true;
		return this.doorcount;
	}

	/**
	 * The square has been updated. It doesn't need refreshing anymore.
	 * */
	public void update(){
		this.needsRefresh = false;
	}

}
