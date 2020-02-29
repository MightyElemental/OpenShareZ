package mightyelemental.opensharez;

import java.net.URISyntaxException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class OpenShareZ {

	public static Sound ERROR, CAPTURE, TASK_COMPLETE;

	public static final String HOME_DIR = System.getProperty( "user.home" );

	public OpenShareZ() { OSZAppFrame frame = new OSZAppFrame(); frame.setVisible( true ); }

	static {
		try {
			ERROR = Sound.soundFromFile( "ErrorSound" );
			CAPTURE = Sound.soundFromFile( "CaptureSound" );
			TASK_COMPLETE = Sound.soundFromFile( "TaskCompletedSound" );
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		new OpenShareZ();
	}
}
