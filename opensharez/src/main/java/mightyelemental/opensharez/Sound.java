package mightyelemental.opensharez;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {

	AudioStream stream;

	private Sound(File f) throws FileNotFoundException, IOException {
		stream = new AudioStream( new FileInputStream( f ) );
	}

	public static Sound soundFromFile(String path) throws URISyntaxException {
		path = path.replace( ".", "/" );
		File f = new File( Sound.class
				.getResource( "/mightyelemental/opensharez/sounds/" + path + ".wav" ).toURI() );
		if (f.exists()) {
			try {
				return new Sound( f );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void play() { AudioPlayer.player.start( stream ); }

}
