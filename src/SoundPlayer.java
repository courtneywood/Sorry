import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class SoundPlayer extends Thread {
	private String sound;
	public SoundPlayer(String sound) {
		this.sound = sound;
		//play();
	}
	
	public void play() {
		start();
	}
	
	public void run() {
		try{
			File file = new File("assets/sounds/" + sound + ".mp3");
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			Player player = new Player(bis);
			player.play();
		}
		catch(Exception ex)
		{
			System.out.println("Doesn't work");
		}
	}
	
	
}

