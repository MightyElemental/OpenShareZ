package mightyelemental.opensharez;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	Clip clip;
	File audioFile;

	private Sound(File f) throws LineUnavailableException {
		clip = AudioSystem.getClip();
		audioFile = f;
	}

	public static Sound soundFromFile(String path) throws URISyntaxException {
		path = path.replace( ".", "/" );
		File f = new File( Sound.class
				.getResource( "/mightyelemental/opensharez/sounds/" + path + ".wav" ).toURI() );
		if (f.exists()) {
			try {
				return new Sound( f );
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void play() {
		clip.stop();
		clip.close();
		clip.setMicrosecondPosition( 0 );
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream( audioFile );
			clip.open( ais );
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		clip.start();
	}

}
