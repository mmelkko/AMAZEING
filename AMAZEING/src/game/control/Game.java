package game.control;
import game.logics.*;
import game.view.*;
import game.results.*;
import game.music.*;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 * The Game class starts the game and stores all the different parts that make
 * this whole game. Different stages of the game are reached through this class
 * (as in the game is begun/paused/moved to results etc. through this class).
 * 
 * @author Merituuli Melkko
 * */
public class Game {
	/**int that represents the paused state of the game*/
	public final static int PAUSED = 0;
	/**int that represents the game start*/
	public final static int START = 1;
	/**int that represents the playing state in the game*/
	public final static int PLAY = 100;
	/**int that represents the end of the game*/
	public final static int END = -100;
	/**int that represents the result-showing state of the game*/
	public final static int RESULTS = 50;
	/**int that represents the interval state of the game*/
	public final static int INTERVAL = 25;
	/**int[] that holds the levels that are played in this game*/
	public final static int[] levels =  { MazeGrid.LEVEL1, MazeGrid.LEVEL2,
		MazeGrid.LEVEL3 };
	
	private ArrayList<Enemy> enemies;
	private boolean isInMotion, hasBegun, gameWon;
	private GameKeyListener listener;
	private GameWindow window;
	private int levelCount;
	private MazeCanvas canvas;
	private MazeGrid grid;
	private Player player;
	private ResultTable resulttable;
	private Timer timer;
	private Thread gamethread;
	
	public Game(){
		this.canvas = null;
		this.enemies = new ArrayList<Enemy>();
		this.gamethread = null;
		this.hasBegun = false;
		this.isInMotion = false;
		this.gameWon = false;
		
		this.levelCount = 0;
		
		this.grid = new MazeGrid(this);
		this.player = new Player(this);
		this.listener = new GameKeyListener(this);
		this.timer = new Timer(50, new Timekeeper(this));
		this.resulttable = new ResultTable("src/textfiles/results.txt", 10);
		this.window = new GameWindow(this);
	}

	/**
	 * @return MazeCanvas that is the current canvas
	 * */
	public MazeCanvas getCanvas(){
		return this.canvas;
	}

	/**
	 * @return MazeGrid that is the current grid
	 * */
	public MazeGrid getGrid(){
		return this.grid;
	}

	/**
	 * @return GameKeyListener that is the keylistener in this game.
	 * */
	public GameKeyListener getListener(){
		return this.listener;
	}

	/**
	 * @return Player that is the player in this game
	 * */
	public Player getPlayer(){
		return this.player;
	}

	/**
	 * @return ResultTable that is the resulttable used in this game
	 * */
	public ResultTable getResultTable(){
		return this.resulttable;
	}

	/**
	 * @return int that is the current level. If -100, there are no levels left
	 * */
	public int getCurrentLevel(){
		if (this.levelCount >= 0 && this.levelCount 
				< Game.levels.length){
			return Game.levels[this.levelCount];
		}
		return -100;
	}

	/**
	 * @return GameWindow that is the window of this game
	 * */
	public GameWindow getWindow(){
		return this.window;
	}

	/**
	 * @param Enemy that is added to the list of enemies
	 * */
	public void addEnemy(Enemy enemy){
		this.enemies.add(enemy);
	}

	/**
	 * Begins the game. Sets the game as begun, gives the player 
	 * some spare points.
	 * */
	public void beginGame(){
		this.hasBegun = true;
		this.getPlayer().changePoints(250);
		this.timer.setRepeats(true);
		this.gamethread = new Thread(this.listener);
		this.gamethread.start();
		this.getWindow().openView(Game.INTERVAL);
	}

	/**
	 * Creates a level by resetting things that need to be reset and creating
	 * the new things needed for the new level.
	 * */
	public void createLevel(){
		this.enemies.clear();
		this.getWindow().removeCanvas();
		this.canvas = new MazeCanvas(this);
		this.getGrid().createMazeForLevel(this.getCurrentLevel());
	}

	/**
	 * Ends the game, saves and stops enemies.
	 * */
	public void end(){
		this.isInMotion = false;
		if (this.gameWon){
			this.getResultTable().saveRowsToFile();
		} else {
			timer.stop();
			this.stopEnemies();
		}

		this.window.openView(Game.END);
	}

	/**
	 * @return boolean if the game is won
	 * */
	public boolean gameWon(){
		return this.gameWon;
	}

	/**
	 * @return boolean if the game has begun
	 * */
	public boolean hasBegun(){
		return this.hasBegun;
	}

	/**@return boolean if the game is in motion*/
	public boolean isInMotion(){
		return this.isInMotion;
	}

	/**
	 * Completes the level, stops the timer and the enemies and if there are
	 * no more levels, moves to the result page.
	 * */
	public void levelComplete(){
		this.timer.stop();
		this.isInMotion = false;
		this.levelCount++;
		this.stopEnemies();

		if (this.getCurrentLevel() == -100){
			this.gameWon = true;
			this.results();
		} else {
			this.window.openView(Game.INTERVAL);
		}
	}
	
	/**
	 * Starts the enemies timer to make them move.
	 * */
	public void startEnemies(){
		for (int a = 0; a < this.enemies.size(); a++){
			this.enemies.get(a).getTimer().start();
		}
	}

	/**
	 * Stops the enemies timer to stop them from moving.
	 * */
	public void stopEnemies(){
		for (int a = 0; a < this.enemies.size(); a++){
			this.enemies.get(a).getTimer().stop();
		}
	}

	/**
	 * The 'pause'-command that either pauses the game (and/or opens the menu)
	 * or sets the game going again.
	 * */
	public void pause(){
		if (this.isInMotion() || !this.hasBegun()){
			this.isInMotion = false;
			this.stopEnemies();
			this.timer.stop();
			this.window.openView(Game.PAUSED);
		} else {
			this.isInMotion = true;
			this.startEnemies();
			this.timer.start();
			this.getCanvas().needsUpdateNow();
			this.window.openView(Game.PLAY);
		}
	}

	/**
	 * Plays a new level.
	 * */
	public void play(){
		this.createLevel();
		this.getPlayer().changePoints(1000);
		this.isInMotion = true;
		this.startEnemies();
		this.getWindow().openView(Game.PLAY);
		this.getCanvas().needsUpdateNow();
		this.timer.start();
	}
	
	/**
	 * Plays the music
	 * */
	public void playMusic(){
		new MusicPlayer("src/music/yay.wav");
	}

	/**
	 * Depending on if the game has begun opens the highscorelist or asks
	 * the player for a name to add their result to the highscorelist.
	 * */
	public void results(){
		if (this.hasBegun()){
			if (this.getWindow().askForResults()){
				this.window.openView(Game.RESULTS);
			} else {
				this.getWindow().openView(Game.END);
			}
		} else {
			this.window.openView(Game.RESULTS);
		}
	}

	
	public static void main(String[] args){
		new Game();
	}

}
