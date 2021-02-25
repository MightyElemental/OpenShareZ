package mightyelemental.opensharez;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/**
 * A JFrame that is able to be locked in place to prevent users from moving it.
 * 
 * @see JFrame
 */
public class FixedJFrame extends JFrame {

	private static final long serialVersionUID = -1299158386103105923L;

	private Point locked;

	/**
	 * Constructs a new FixedJFrame.
	 * 
	 * @param title the title of the frame
	 */
	public FixedJFrame( String title ) {
		super(title);
		addComponentListener(new ComponentAdapter() {

			public void componentMoved( ComponentEvent e ) {
				if (locked != null) FixedJFrame.this.setLocation(locked);
			}
		});
	}

	public FixedJFrame() {
		this("");
	}

	/**
	 * Lock the window in place
	 * 
	 * @param lock whether to lock or unlock the window
	 */
	public void setLocked( boolean lock ) {
		if (lock) {
			locked = getLocation();
		} else {
			locked = null;
		}
	}

}
