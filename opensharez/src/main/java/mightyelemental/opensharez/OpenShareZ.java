package mightyelemental.opensharez;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class OpenShareZ {

	public static final String HOME_DIR = System.getProperty( "user.home" );

	public OpenShareZ() { OSZAppFrame frame = new OSZAppFrame(); frame.setVisible( true ); }

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
