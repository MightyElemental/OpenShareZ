package mightyelemental.opensharez;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class FixedJFrame extends JFrame {

	private static final long serialVersionUID = 3023145860072683185L;

	private Point locked;

	public FixedJFrame(String title) {
		super( title );
		addComponentListener( new ComponentAdapter() {

			public void componentMoved(ComponentEvent e) {
				if (locked != null) FixedJFrame.this.setLocation( locked );
			}
		} );
	}

	public FixedJFrame() { this( "" ); }

	public void setLocked(boolean lock) {
		if (lock) {
			locked = getLocation();
		} else {
			locked = null;
		}
	}

}
