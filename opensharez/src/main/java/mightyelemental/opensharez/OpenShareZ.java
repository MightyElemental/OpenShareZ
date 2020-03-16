package mightyelemental.opensharez;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import mightyelemental.opensharez.config.ConfigManager;

public class OpenShareZ {

	public static Sound ERROR, CAPTURE, TASK_COMPLETE;

	public static final String HOME_DIR = System.getProperty( "user.home" );
	public static final String TITLE    = "OpenShareZ - Screen Share Program";

	public OpenShareZ() {
		OSZAppFrame frame = new OSZAppFrame();
		frame.setVisible( true );
		frame.addWindowListener( new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) { Utils.stopRecording(); }

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

		} );
	}

	static {
		try {
			ERROR = Sound.soundFromFile( "ErrorSound" );
			CAPTURE = Sound.soundFromFile( "CaptureSound" );
			TASK_COMPLETE = Sound.soundFromFile( "TaskCompletedSound" );
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	public static void testIfRunning() {
		AtomicInteger i = new AtomicInteger( 0 );
		ProcessHandle
				.allProcesses().filter( ph -> ph.info().command().isPresent()
						&& ph.info().commandLine().get().contains( "OpenShareZ" ) && ph.info().commandLine().get().contains( "java" ) )
				.forEach( (process) -> { i.addAndGet( 1 ); } );
		if (i.get() > 1) {
			System.out.println( "OpenShareZ is running already" );
			System.exit( 1 );
		}
	}

	public static void main(String[] args) {
		try {
			ConfigManager.createConfigFileIfNotPresent();
			ConfigManager.loadSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utils.stopRecording();// used to ensure ffmpeg has stopped
		// Utils.verifySupportForTransparancy();
		testIfRunning();
		Utils.setGTK();

		new OpenShareZ();
	}
}
