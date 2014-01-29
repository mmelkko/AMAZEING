package game.results;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ResultRow holds the information of the result a user makes in this program
 * and makes it comparable with other ResultRows informations.
 * 
 * @implements Comparable<ResultRow>
 * 
 * @author Merituuli Melkko
 * */
public class ResultRow implements Comparable<ResultRow>{
	private String playersName;
	private Date moment;
	private int points;
	
	/**
	 * @param playersName that is put in this ResultRow
	 * @param moment that this ResultRow is made in
	 * @param points that is put in this ResultRow
	 * */
	public ResultRow(String playersName, Date moment, int points){
		this.playersName = playersName;
		this.moment = moment;
		this.points = points;
	}
	
	/**
	 * @return String, that is the name of the Player
	 * */
	public String getPlayersName(){
		return this.playersName;
	}
	
	/**
	 * @return Date that is cloned from the Date in which this 
	 * ResultRow was made.
	 * */
	public Date getMoment(){
		return (Date) this.moment.clone();
	}
	
	/**
	 * @return int, that is the amount of points the player had at the end
	 * of the game.
	 * */
	public int getPoints(){
		return this.points;
	}

	/**
	 * @param row that this is compared to
	 * 
	 * @return int that is 1 if row was worse than this, 0 if they are the
	 * same and -1 if this is worse than row.
	 * */
	@Override
	public int compareTo(ResultRow row) {
		if (this.getPoints() == row.getPoints()){
			int vertailu = -this.getMoment().compareTo(row.getMoment());
			if (vertailu == 0){
				return this.getPlayersName().compareTo(row.getPlayersName());
			}
			return vertailu;
		} else if (this.getPoints() > row.getPoints()){
			return -1;
		}
		return 1;
	}
	
	@Override
	public String toString(){
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
		
		return this.getPlayersName() + "&" 
		+ date.format(this.getMoment()) + "&" + this.getPoints();
	}
}
