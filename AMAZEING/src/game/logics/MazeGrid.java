package game.logics;
import game.control.*;

import java.io.*;
import java.util.ArrayList;

/**
 * MazeGrid holds the structure of the current maze in a Square[][] attribute.
 * The mazes are created according to the defined file for each level. The
 * MazeGrid reads the file and deciphers the information given to make a
 * complete maze.
 * 
 * @author Merituuli Melkko
 * */
public class MazeGrid {
	/**int that represents the first level*/
	public static final int LEVEL1 = 100;
	/**int that represents the second level*/
	public static final int LEVEL2 = 200;
	/**int that represents the third level*/
	public static final int LEVEL3 = 300;

	private int width, height;
	private Square[][] squares;
	private Game game;

	/**
	 * @param Game that this MazeGrid is used for.
	 * */
	public MazeGrid(Game game){
		this.width = 18;
		this.height = 10;
		this.game = game;
	}

	/**
	 * @return int height of this
	 * */
	public int getHeight(){
		return this.height;
	}

	/**
	 * @return int width of this
	 * */
	public int getWidth(){
		return this.width;
	}

	/**
	 * Gets the neighbour of the desired Square in the desired direction.
	 * 
	 * @param x that is the x of the Square the neighbour is wanted of
	 * @param y that is the y of the Square the neighbour is wanted of
	 * @param direction that the neighbour is wanted in
	 * 
	 * @return Square that is the neighbour desired.
	 * */
	public Square getNeighbourOf(int x, int y, Direction direction){
		Square neighbour = null;

		if (x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight()){
			switch (direction){
			case UP: if (y > 0){ neighbour = this.squares[y-1][x];}; break;
			case DOWN: if (y < this.getHeight() -1){
				neighbour = this.squares[y+1][x];}; break;
			case LEFT: if (x > 0){ neighbour = this.squares[y][x-1];}; break;
			case RIGHT: if (x < this.getWidth() -1){
				neighbour = this.squares[y][x+1];}; break;
			default: break;
			} 
		}

		return neighbour;
	}

	/**
	 * @param being that is set
	 * @param x the being is set to
	 * @param y the being is set to
	 * */
	public void setBeing(Being being, int x, int y){
		try {
			this.squares[y][x].setBeing(being);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/* Checks that the walls of the maze are correctly. Useful when making new
	 * mazes, since typos occur often. Informs of this error to the
	 * messageflow and corrects this mistake.*/
	private void checkWallsOfMaze(){
		for (int y = 0; y < this.getHeight(); y++){
			for (int x = 0; x < this.getWidth(); x++){
				Square square = this.squares[y][x];
				Direction[] directions = Direction.values();
				for (int a = 0; a < directions.length; a++){
					Square neighbour = this.getNeighbourOf(x, y, directions[a]);
					Direction direction = directions[a];
					/*the following if: if the square isn't null, if the
					 * square can't go to the direction (the neighbour is in
					 * this direction) and neighbour is not null and
					 * from the neighbour it is possible to go to the
					 * square and the neighbour doesn't have a door and
					 * the square doesn't have a door (because doors function
					 * in a very special way). Anyway if this is true, the
					 * neighbour is not well-enough informed.*/
					if (square != null && !square.canIGoTo(direction) 
							&& neighbour != null
							&& neighbour.canIGoTo(direction.opposite()) 
							&& !neighbour.hasDoor() && !square.hasDoor()){
						System.err.println("The neighbour of Square in "
								+ x + "&" + y + " in " + direction +
						" didn't know there was a wall.");
						neighbour.setWall(direction.opposite());
					}
				}
			}
		}
	}

	/**
	 * Creates the maze for the specified level by reading the information from
	 * the for every level specified file.
	 * 
	 * @param level that the maze is created for.
	 * */
	public void createMazeForLevel(int level){
		File filename = null;
		if (level == MazeGrid.LEVEL1){
			filename = new File("src/textfiles/level1.txt");
		} else if (level == MazeGrid.LEVEL2){
			filename = new File("src/textfiles/level2.txt");
		} else if (level == MazeGrid.LEVEL3){
			filename = new File("src/textfiles/level3.txt");
		} else {
			System.err.println("*************THERE IS NO LEVEL***********");
			return;
		}

		this.squares = new Square[this.getHeight()][this.getWidth()];
		this.game.getCanvas().determineSquareCoatAmount();

		try {
			BufferedReader linereader
			= new BufferedReader(new FileReader(filename));
			try {
				String line = linereader.readLine();
				int height = 0;
				while (line != null){
					String[] units = line.split("!");
					if (units.length != this.width){
						System.err.println("The maze is wider than what the " +
								"MazeGrid knows of!! " 
								+ units.length);
					}
					/* How one square is read:
					 * - || define which information is part of which square.
					 * Inside the || is one square.
					 * - 1 is for wall and 0 is for no wall.
					 * - the directions are in order up, down, left, right.
					 * - if there's extra info, it's after the directions.
					 * Example:
					 * |0 0 1 1 door|
					 * a square that has a wall in left and right and a door.
					 * */
					for (int a = 0; a < units.length; a++){
						String[] parts = units[a].split(" ");
						Square square = null;
						if (parts.length >= 4){
							ArrayList<Direction> walls 
							= new ArrayList<Direction>();
							if (parts[0].contains("1")){
								walls.add(Direction.UP);
							}
							if (parts[1].contains("1")){
								walls.add(Direction.DOWN);
							}
							if (parts[2].contains("1")){
								walls.add(Direction.LEFT);
							}
							if (parts[3].contains("1")){
								walls.add(Direction.RIGHT);
							}
							square = new Square
							(a, height, this, walls);
						}

						this.squares[height][a] = square;
						this.game.getCanvas().createCoatFor(square, a, height);

						if (parts.length == 5){
							if (parts[4].contains("goal")){
								this.squares[height][a].setAsGoal();
							}
							if (parts[4].contains("door")){
								this.squares[height][a].setDoor(true);
							}
							if (parts[4].contains("player")){
								if (this.game.getPlayer().getLocation() 
										!= null){
									this.game.getPlayer().setLocation(null);
								}

								this.setBeing(this.game.getPlayer(), a, height);
							}
							if (parts[4].contains("enemy")){
								Enemy enemy = new Enemy(square, this.game);
								this.game.addEnemy(enemy);
							}
							if (parts[4].contains("treasure")){
								new Treasure(square, 0, 0);
							}
							if (parts[4].contains("apple")){
								new Apple(square);
							}
						}
					}
					height++;
					line = linereader.readLine();
				}
				this.checkWallsOfMaze();

				if (linereader != null){
					linereader.close();
				}
			} catch (IOException e) {
				System.err.println("Error in reading the file.");
			}
		} catch (FileNotFoundException e) {
			System.err.println("THERE IS NO FILE");
		}
	}
	
}
