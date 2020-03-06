package mightyelemental.opensharez;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Preview extends FixedJFrame {

	private static final long serialVersionUID = -7111189485432492836L;

	private BufferedImage previewImg;

	public Preview(BufferedImage img, String title) {
		super( title );
		this.setType( Type.POPUP );
		previewImg = img;
		Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		double ratio = (double) screenBounds.height / (double) screenBounds.width;
		int width = (int) (screenBounds.width / 4f);
		int x = screenBounds.x + screenBounds.width - width - 25;
		int height = (int) (ratio * width);
		int y = screenBounds.y + screenBounds.height - height - 50;
		this.setLocation( x, y );
		this.setSize( width, height );
		this.setUndecorated( true );
		System.out.printf( "%d %d %d %d\n", x, y, width, height );
		this.setVisible( true );
		this.setResizable( false );
		this.setAlwaysOnTop( true );
		this.repaint();
		fade();
	}

	private void fade() {// TODO:Fix fading
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
		exec.schedule( () -> {
			Preview.this.setOpacity( 0.5f );
			Preview.this.repaint();
		}, 3, TimeUnit.SECONDS );
		exec.schedule( () -> {
			Preview.this.setVisible( false );
			Preview.this.dispose();
		}, 4, TimeUnit.SECONDS );
	}

	public void paint(Graphics g) {
		if (previewImg != null) {
			g.drawImage( previewImg, 0, 0, this.getWidth(), this.getHeight(), null );
		}
		g.setColor( new Color( 0, 0, 0, 0.7f ) );
		g.fillRect( 0, 0, this.getWidth(), 20 );
		g.setColor( Color.white );
		g.drawString( this.getTitle(), 5, 15 );
	}

}
