package mightyelemental.opensharez;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class FullscreenRegionSelectionWindow extends JFrame {

	private static final long serialVersionUID = -8330678002588348027L;

	private JPanel contentPane;

	private BufferedImage capture;

	private Rectangle selection;

	private int oldy, oldx;

	private void updateSelection(int newx, int newy) {
		if (newy - oldy < 0) {
			if (newx - oldx < 0) {
				selection = new Rectangle( newx, newy, -newx + oldx, -newy + oldy );
			} else {
				selection = new Rectangle( oldx, newy, newx - oldx, -newy + oldy );
			}
		} else {
			if (newx - oldx < 0) {
				selection = new Rectangle( newx, oldy, -newx + oldx, newy - oldy );
			} else {
				selection = new Rectangle( oldx, oldy, newx - oldx, newy - oldy );
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public FullscreenRegionSelectionWindow(BufferedImage img) {
		this.capture = img;
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// setBounds( 100, 100, 450, 300 );
		contentPane = new JPanel() {

			private static final long serialVersionUID = -2060599554374078227L;

			public void paint(Graphics g) {
				g.drawImage( capture, 0, 0, null );
				g.setColor( new Color( 0.5f, 0.5f, 0.5f, 0.2f ) );
				g.fillRect( 0, 0, capture.getWidth(), capture.getHeight() );
				if (selection != null) {
					g.setColor( Color.DARK_GRAY );
					g.drawRect( selection.x, selection.y, selection.width, selection.height );
				}
			}

		};
		contentPane.addMouseListener( new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				oldx = x;
				oldy = y;
			}
		} );
		contentPane.addMouseMotionListener( new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {

				int x = e.getX();
				int y = e.getY();

				updateSelection( x, y );
				contentPane.repaint();
			}
		} );
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		contentPane.setLayout( new BorderLayout( 0, 0 ) );
		setContentPane( contentPane );
	}

}
