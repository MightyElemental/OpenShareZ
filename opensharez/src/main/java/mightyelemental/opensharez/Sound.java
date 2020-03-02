package mightyelemental.opensharez;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	Clip                clip;
	BufferedInputStream audioInStream;

	private Sound(InputStream f) throws LineUnavailableException {
		clip = AudioSystem.getClip();
		audioInStream = new BufferedInputStream( f );
	}

	public static Sound soundFromFile(String path) throws URISyntaxException {
		path = path.replace( ".", "/" );
		InputStream file = Sound.class.getClassLoader()
				.getResourceAsStream( "mightyelemental/opensharez/sounds/" + path + ".wav" );
		if (file != null) {
			try {
				return new Sound( file );
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println( "Could not find sound file " + path );
		}
		return null;
	}

	public void play() {
		clip.stop();
		clip.close();
		clip.setMicrosecondPosition( 0 );
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream( audioInStream );
			clip.open( ais );
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		clip.start();
	}

}
