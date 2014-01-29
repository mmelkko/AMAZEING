package game.view;
import game.logics.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

/**
 * A SquareCoat is the visual execution of a Square. It get the information
 * of the Square and draws the information gotten on the canvas when requested.
 * 
 * @see Square
 * 
 * @extends JPanel
 * 
 * @author Merituuli Melkko
 * */
public class SquareCoat extends JPanel {
	private static final long serialVersionUID = 1L;
	private Square owner;
	private int x, y;
	private MazeCanvas canvas;

	/**
	 * @param owner Square of this
	 * @param canvas that this is in
	 * @param x that is the x of this
	 * @param y that is the y of this
	 * */
	public SquareCoat(Square owner, MazeCanvas canvas, int x, int y){
		this.owner = owner;
		this.canvas = canvas;
		this.x = x*25+25;
		this.y = y*25+100;
		this.setPreferredSize(new Dimension(25,25));
	}

	/**
	 * @return Square that is the owner of this
	 * */
	public Square getOwner(){
		return this.owner;
	}

	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Verdana", Font.BOLD, 12));
		g2.setColor(Color.WHITE);
		g2.fillRect(this.x, this.y, 25, 25);

		if(this.owner.isGoal()){
			g2.drawImage(MazeCanvas.goal.getImage(), this.x+1, this.y+1, null);
		}

		if(this.owner.hasDoor()){
			g2.drawImage(MazeCanvas.getDoorIcon().getImage(), this.x+1, 
					this.y+1, null);
			g2.setColor(Color.RED);
			g2.drawString(" " + this.owner.getDoorcount(), this.x+4, this.y+15);
		}

		g2.setColor(Color.BLACK);
		Direction[] directions = Direction.values();
		Point start = null;
		Point end = null;
		/*draws the lines of walls according to how the owner has them*/
		for(int a = 0; a < directions.length; a++){
			Direction direction = directions[a];
			Square neighbour = this.owner.getNeighbour(direction);
			if (!this.owner.canIGoTo(direction) && (neighbour == null 
					|| !neighbour.canIGoTo(direction.opposite()))){
				switch(direction){
				case UP: start = new Point(this.x,this.y);
				end = new Point(this.x+25,this.y);break;

				case DOWN: start = new Point(this.x,this.y+25); 
				end = new Point(this.x+25,this.y+25);break;

				case LEFT: start = new Point(this.x,this.y);
				end = new Point(this.x,this.y+25);break;

				case RIGHT: start = new Point(this.x+25,this.y); 
				end = new Point(this.x+25,this.y+25);break;

				default: break;
				}

				if (start != null || end !=  null){
					g2.draw(new Line2D.Double(start, end));
				}

			}
		}

		Being being = this.owner.getBeing();
		if (being != null){
			if (being instanceof Player){
				g2.drawImage(this.canvas.getPlayerIcon().getImage(), this.x+1, 
						this.y+1, null);
			} else if (being instanceof Enemy){
				g2.drawImage(this.canvas.getEnemyIcon().getImage(), this.x+1, 
						this.y+1, null);
			}
		}

		Item item = this.owner.getItem();
		if (item != null){
			if (item instanceof Bomb && ((Bomb) item).isExploding()){
				g2.drawImage(MazeCanvas.boom.getImage(), this.x+1, this.y+1, 
						null);
			} else {
				g2.drawImage(MazeCanvas.getItemIcon(item.getNumber()).getImage(), 
						this.x+1, this.y+1, null);
			}
		}
	}

}
