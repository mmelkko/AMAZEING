package game.music;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.*;

/**
 * MusicPlayer plays a desired clip of music once when created.
 * 
 * @author Merituuli Melkko
 * */
//HUOM eclipsen asetuksia täytyy muuttaa ettei näy virhettä importissa.
public class MusicPlayer {
	private AudioPlayer aplayer;
	private AudioStream stream;
	
	/**
	 * @param fileName of the desired clip of music that will be played
	 * */
	public MusicPlayer(String fileName){
		this.aplayer = AudioPlayer.player;
		try {
			this.stream = new AudioStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Music file not found.");
		} catch (IOException e) {
			System.err.println("Error in reading the music file");
		}
		this.aplayer.start(this.stream);
	}

}