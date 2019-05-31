package Client;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class PlaySound extends Thread{
	AudioInputStream ais;
	File file;
	AudioFormat format;
	Clip clip;
	public PlaySound() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {e.printStackTrace();}
	}
	
	public void play(String path) {
		try {
			file = new File(path);
			ais = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
			this.start();
		} catch (Exception e) {e.printStackTrace();}
		
	}
	public void play(URL url) {
		try {
			System.out.println(url);
			ais = AudioSystem.getAudioInputStream(url);
			this.start();
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	public void run() {
		try {
			clip.open(ais);
			clip.start();
			Thread.sleep(clip.getMicrosecondLength()/1000);	
		} catch (Exception e) {}
	}
	public void pause() {
		if(clip.isRunning()) {
			clip.stop();
			clip.close();
			this.interrupt();
		}
	}
	
}
