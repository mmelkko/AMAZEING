package game.control;
import game.logics.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The GameKeyListener is the controller in the gamesituation, it takes the
 * commands given by the user of the program through the keyboard and
 * calls the methods that correspond them.
 * 
 * @extends KeyAdapter
 * @implements Runnable
 * 
 * @author Merituuli Melkko
 * */
public class GameKeyListener extends KeyAdapter implements Runnable {
	private Game game;
	private Player player;

	/**
	 * @param Game that this is for.
	 * */
	public GameKeyListener(Game game){
		this.game = game;
		this.player = this.game.getPlayer();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (this.game.isInMotion()){
			int key = event.getKeyCode();
			if (key == KeyEvent.VK_UP){
				this.player.move(Direction.UP);
			} else if (key == KeyEvent.VK_DOWN){
				this.player.move(Direction.DOWN);
			} else if(key == KeyEvent.VK_LEFT){
				this.player.move(Direction.LEFT);
			} else if (key == KeyEvent.VK_RIGHT){
				this.player.move(Direction.RIGHT);
			} 
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int key = event.getKeyCode();
		if (key == KeyEvent.VK_A){
			Square square = this.player.getLocation().getNearbyDoor();
			if (square != null){
				square.openDoor();
			}
		}
		char[] charecters = new char[1];
		charecters[0] = event.getKeyChar();
		try{
			int i = Integer.parseInt(new String(charecters));
			for (int a = 1; a < Itemslot.NUMBER_OF_SLOTS+1; a++){
				if (i == a){
					this.player.useItemFromSlot(a);
				}
			}
		} catch (NumberFormatException nfe){

		}
		
		if (key == KeyEvent.VK_SPACE){
			this.game.pause();
		}
	}

	@Override
	public void run() {}
	
}