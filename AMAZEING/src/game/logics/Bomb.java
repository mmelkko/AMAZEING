package game.logics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * When placed to a Square, the Bomb will start a Timer, explode and hurt
 * nearby Beings. If someone steps on a Bomb before it explodes it will
 * explode right there and then.
 * 
 * @implements ActionListener
 * 
 * @extends Item
 * 
 * @author Merituuli Melkko
 * */
public class Bomb extends Item implements ActionListener {
	/**int that states the worth of a bomb*/
	public static final int WORTH = 150;
	private Timer timer;
	private boolean isExploding;
	private int seconds;

	public Bomb(){
		super(Bomb.WORTH, 3);
		this.timer = new Timer(1000, this);
		this.isExploding = false;
		this.seconds = 0;
	} 

	/**
	 * Makes the Bomb explode and hurt every Being that is within two Squares
	 * (but not through walls).
	 * */
	public void explode(){
		this.isExploding = true;
		ArrayList<Being> beings = this.getLocation().collectNearbyBeings();
		for (int a = 0; a < beings.size(); a++){
			beings.get(a).itHurts();
		}
		
		this.getLocation().needsUpdateNow();
	}

	/**
	 * @return boolean if the bomb is currently exploding
	 * */
	public boolean isExploding(){
		return this.isExploding;
	}

	/*a bomb can only get a new owner if it is in no Square. If it is in a
	 * Square and someone tries to "pick it up", it will only explode faster.*/
	@Override
	public void setOwner(Being being){
		if (this.getLocation() == null){
			super.setOwner(being);
		} else if (!this.isExploding()){
			this.explode();
		}
	}

	/*when placing a bomb to a square, the countdown to the explosion starts!*/
	@Override
	public void placeToSquare(Square square) {
		super.placeToSquare(square);
		if (square != null){
			this.timer.start();
		}
	}

	/*after two seconds the bomb will explode and after three seconds the
	 * explosion is over (this is mostly so that the boom-icon will be shown
	 * for a while that is longer than a millisecond or so).
	 * 
	 * When the bomb has exploded, it is removed from the square.*/
	@Override
	public void actionPerformed(ActionEvent event) {
		this.seconds++;
		if (this.seconds == 2){
			if (!this.isExploding()){
				this.explode();
			}
		} else if (this.seconds == 3){
			this.isExploding = false;
			this.placeToSquare(null);
			timer.stop();
		}
	}

	@Override
	public String toString(){
		return "A bomb. Explodes<br>over time and<br>hurts everyone<br>" +
				"nearby (but not<br>through walls).";
	}

}
