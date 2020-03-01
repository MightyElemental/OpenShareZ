package mightyelemental.opensharez;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class OpenShareZ {

	public static Sound ERROR, CAPTURE, TASK_COMPLETE;

	public static final String HOME_DIR = System.getProperty( "user.home" );

	public OpenShareZ() {
		JFrame.setDefaultLookAndFeelDecorated( true );
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				OSZAppFrame frame = new OSZAppFrame();
				frame.setOpacity( 0.55f );
				frame.setVisible( true );
			}
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
		ProcessHandle.allProcesses()
				.filter( ph -> ph.info().command().isPresent()
						&& ph.info().commandLine().get().contains( "OpenShareZ" ) )
				.forEach( (process) -> { i.addAndGet( 1 ); } );
		if (i.get() > 1) {
			System.out.println( "OpenShareZ is running already" );
			System.exit( 1 );
		}
	}

	public static void main(String[] args) {

		Utils.verifySupportForTransparancy();
		testIfRunning();
		Utils.setGTK();

		new OpenShareZ();
	}
}
