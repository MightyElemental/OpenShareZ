package mightyelemental.opensharez;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FullscreenRegionSelectionWindow extends JFrame {

	private static final long serialVersionUID = -8330678002588348027L;

	private JPanel contentPane;

	private BufferedImage capture;

	public Rectangle selection;

	public boolean regionSelected, cancelled;

	private int oldy, oldx, currentX, currentY;

	private void updateSelection( int newx, int newy ) {
		if (newy - oldy < 0) {
			if (newx - oldx < 0) {
				selection = new Rectangle(newx, newy, -newx + oldx, -newy + oldy);
			} else {
				selection = new Rectangle(oldx, newy, newx - oldx, -newy + oldy);
			}
		} else {
			if (newx - oldx < 0) {
				selection = new Rectangle(newx, oldy, -newx + oldx, newy - oldy);
			} else {
				selection = new Rectangle(oldx, oldy, newx - oldx, newy - oldy);
			}
		}
	}

	public void drawZoom( Graphics g ) {
		int x = Math.max(0, currentX - 7);
		int y = Math.max(0, currentY - 7);
		BufferedImage img = capture.getSubimage(x, y, Math.min(capture.getWidth() - x, 15), Math.min(capture.getHeight() - y, 15));
		g.drawImage(img, currentX + 80, currentY - 80, 150, 150, null);
		// g.drawLine( currentX + 80, currentY + 4, currentX + 80 + 79, currentY + 4 );
		// g.drawLine( currentX + 80 + 79 + 8, currentY + 4, currentX + 80 + 79 + 80, currentY + 4 );

		for (int ix = 0; ix < 150; ix += 10) {
			for (int iy = 0; iy < 150; iy += 10) {
				int gridX = currentX + 80 + ix;
				int gridY = currentY - 80 + iy;
				if (ix == 70 && iy == 70) {
					g.setColor(Color.white);
					g.drawRect(gridX + 1, gridY + 1, 8, 8);
					g.setColor(Color.black);
					g.drawRect(gridX, gridY, 10, 10);
				} else if (ix == 70 || iy == 70) {
					g.setColor(new Color(0.9f, 0.9f, 1f, 0.25f));
					g.fillRect(gridX, gridY, 10, 10);
				}
				g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.5f));
				g.drawRect(gridX, gridY, 10, 10);
			}
		}

		g.setColor(Color.black);
		g.drawRect(currentX + 80, currentY - 80, 150, 150);
		g.setColor(Color.white);
		g.drawRect(currentX + 79, currentY - 81, 152, 152);

	}

	public void drawRegion( Graphics g ) {
		g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.3f));
		int width = capture.getWidth();
		int height = capture.getHeight();
		if (selection == null) {
			g.fillRect(0, 0, width, height);
		} else {
			g.fillRect(0, 0, selection.x, height);
			g.fillRect(selection.x + selection.width, 0, width - selection.x, height);

			g.fillRect(selection.x, 0, selection.width, selection.y);
			g.fillRect(selection.x, selection.y + selection.height, selection.width, height - (selection.y + selection.height));

			// g.fillRect( 0, selection.y + selection.height, selection.width, selection.y );

			g.setColor(Color.black);
			g.drawRect(selection.x, selection.y, selection.width, selection.height);
		}

	}

	/**
	 * Create the frame.
	 */
	public FullscreenRegionSelectionWindow( BufferedImage img ) {
		this.capture = img;
		currentX = capture.getWidth() / 2;
		currentY = capture.getHeight() / 2;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// setBounds( 100, 100, 450, 300 );
		contentPane = new JPanel() {

			private static final long serialVersionUID = -2060599554374078227L;

			public void paint( Graphics g ) {
				g.drawImage(capture, 0, 0, null);

				drawRegion(g);
				drawZoom(g);
			}

		};
		contentPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased( MouseEvent e ) {
				if (selection != null) {
					regionSelected = true;
					System.out.println("selected");
				}
			}

			@Override
			public void mousePressed( MouseEvent e ) {
				if (e.getButton() == 1) {
					int x = e.getX();
					int y = e.getY();
					oldx = x + 1;
					oldy = y + 1;
				} else if (e.getButton() == 3) {
					cancelled = true;
				}
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved( MouseEvent e ) {
				currentX = e.getX();
				currentY = e.getY();
				contentPane.repaint();
			}

			@Override
			public void mouseDragged( MouseEvent e ) {
				currentX = e.getX();
				currentY = e.getY();

				int x = e.getX();
				int y = e.getY();

				updateSelection(x, y);
				contentPane.repaint();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
