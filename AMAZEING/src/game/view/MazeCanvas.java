package game.view;
import game.logics.*;
import game.control.*;

import java.awt.*;

import javax.swing.ImageIcon;

/**
 * The game itself is drawn on a MazeCanvas. The class holds almost all of the
 * icons and images used in the game. When repainted, checks which parts need
 * updateing and updates only them (except when everything needs updating of
 * course). The MazeCanvas also creates and manages the SquareCoats.
 * 
 * @extends Canvas
 * 
 * @author Merituuli Melkko
 * */
public class MazeCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	public static final ImageIcon door = new ImageIcon("src/images/lock.png"),
	heart = new ImageIcon("src/images/hearts.png"),
	apple = new ImageIcon("src/images/apple.png"),
	bomb = new ImageIcon("src/images/bomb.png"), 
	key = new ImageIcon("src/images/key.png"),
	banana = new ImageIcon("src/images/banana.png"), 
	boom = new ImageIcon("src/images/boom.png"),
	goal = new ImageIcon("src/images/goal.png"), 
	treasure = new ImageIcon("src/images/treasure.png"),
	maze = new ImageIcon("src/images/maze.png");
	
	private MazeGrid grid;
	private Game game;
	private SquareCoat[][] coats;
	private int updateCount;
	private ImageIcon playericon, playerhurticon, enemyicon, gamelosticon, 
	gamewonicon;

	/**
	 * @param game that this is used in
	 * */
	public MazeCanvas(Game game){
		this.grid = game.getGrid();
		this.game = game;
		this.updateCount = 1;
		this.playericon = new ImageIcon("src/images/duck.png");
		this.playerhurticon = new ImageIcon("src/images/duckhurts.png");
		this.enemyicon = new ImageIcon("src/images/fireball.png");
		this.gamelosticon = new ImageIcon("src/images/gamelost.gif");
		this.gamewonicon = new ImageIcon("src/images/gamewon.gif");
	}

	/**
	 * @return ImageIcon of door
	 * */
	public static ImageIcon getDoorIcon(){
		return MazeCanvas.door;
	}

	/**
	 * @param number that is the number of the class of item that the 
	 * ImageIcon is wanted for
	 * 
	 * @return ImageIcon of the specified item
	 * */
	public static ImageIcon getItemIcon(int number){
		if (number == 1){
			return MazeCanvas.key;
		} else if (number == 4){
			return MazeCanvas.apple;
		} else if (number == 2){
			return MazeCanvas.banana;
		} else if (number == 3){
			return MazeCanvas.bomb;
		} else if (number == 100){
			return MazeCanvas.treasure;
		} else {
			return null;
		}
	}

	/**
	 * @return ImageIcon that is the icon used for the player (depends on
	 * if the player can move or not)
	 * */
	public ImageIcon getPlayerIcon(){
		if (this.game.getPlayer().canMove()){
			return this.playericon;
		} else {
			return this.playerhurticon;
		}
	}
	
	/**
	 * @return ImageIcon that is the icon used in the endview. Depends on
	 * if the game is won or not.
	 * */
	public ImageIcon getEndIcon(){
		if (this.game.gameWon()){
			return this.gamewonicon;
		} else {
			return this.gamelosticon;
		}
	}

	/**
	 * @return ImageIcon that is the icon used for enemies
	 * */
	public ImageIcon getEnemyIcon(){
		return this.enemyicon;
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(500, 400);
	}

	/**
	 * Increases the update count
	 * */
	public void needsUpdateNow(){
		this.updateCount++;
	}

	/**
	 * Creates a SquareCoat and sets it into the table of coats.
	 * 
	 * @param Square that the coat is created for
	 * @param int x the x-coordinates of the square
	 * @param int y the y-coordinates of the square
	 * */
	public void createCoatFor(Square square, int x, int y){
		SquareCoat coat = null;
		if (square != null){
			coat = new SquareCoat(square, this, x, y);
		}
		this.coats[y][x] = coat;
	}

	/**
	 * Determines the amount of SquareCoats that is needed by getting the 
	 * height and weight of the grid that holds the squares.
	 * */
	public void determineSquareCoatAmount(){
		this.coats = new SquareCoat[this.grid.getHeight()][this.grid.getWidth()];
	}

	/**
	 * Paints the itemslot if it requests painting.
	 * 
	 * @param g that is used for drawing
	 * */
	public void paintItemslot(Graphics g){
		Itemslot slot = this.game.getPlayer().getItemslot();

		if (slot.needsRefresh() || this.updateCount > 0){
			Graphics2D g2 = (Graphics2D) g;

			for (int a = 1; a < Itemslot.NUMBER_OF_SLOTS +1; a++){
				g2.setFont(new Font("Arial", Font.BOLD, 12));
				g2.setColor(Color.WHITE);
				g2.fillRect(a*25, 20, 25, 25);
				g2.drawString(" " + a, 25*a + 5, 60);
				g2.setColor(Color.BLACK);
				g2.drawRect(a*25, 20, 25, 25);
				g2.drawImage(MazeCanvas.getItemIcon(a).getImage(), 25*a +1, 22, null);
				g2.setFont(new Font("Arial", Font.BOLD, 9));
				g2.drawString(" " + slot.getNumberOfItemsInSlot(a), 25*a+18, 45);
			}
			slot.update();
		}
	}

	/**
	 * Paints the lives of the Player
	 * 
	 * @param g that is used for the drawing
	 * */
	public void paintLives(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.setColor(Color.WHITE);
		g2.drawString("LIVES:", 360, 35);
		g2.setColor(Color.RED);
		for (int life = this.game.getPlayer().getLives(); life > 0; life--){
			g2.drawImage(MazeCanvas.heart.getImage(), 375 + life*30, 20, null);
		}
	}

	@Override
	public void paint(Graphics g){
		for (int y = 0; y < this.grid.getHeight(); y++){
			for (int x = 0; x < this.grid.getWidth(); x++){
				SquareCoat coat = this.coats[y][x];
				if (coat != null && (coat.getOwner().needsRefresh() 
						|| this.updateCount > 0)){
					coat.paintComponent(g);
					coat.getOwner().update();
				}
			}
		}
	}

	@Override
	public void update(Graphics g){
		if (this.updateCount > 0){
			Image background = MazeCanvas.maze.getImage();
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			if (background != null){
				g2.drawImage(background, 6, 0, null);
			}
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 12));
			g2.drawString("[Press SPACE for pause]", 180, 35);
			this.paintLives(g);
			this.paintItemslot(g);
			this.paint(g);
			this.updateCount--;
		} else {
			this.paintItemslot(g);
			this.paint(g);
		}
	}

}
