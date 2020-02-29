package mightyelemental.opensharez;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class CaptureOperations {

	// public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Robot           robot;
	public static GraphicsDevice[] screens  = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getScreenDevices();

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		System.out.println( getMonitorSizes() );
	}

	private static String getMonitorSizes() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < screens.length; i++) {
			DisplayMode dm = screens[i].getDisplayMode();
			sb.append( String.format( "Screen %d: width: %d, height: %d\n", i, dm.getWidth(),
					dm.getHeight() ) );
		}
		return sb.toString();
	}

	public static Dimension getScreenDim(int screenNum) {
		if (screenNum > screens.length - 1 || screenNum < 0) return null;
		DisplayMode dm = screens[screenNum].getDisplayMode();
		return new Dimension( dm.getWidth(), dm.getHeight() );
	}

	public static BufferedImage captureScreen(int screenNum) {// TODO: adjust to account for multiple screens
		return robot.createScreenCapture( new Rectangle( getScreenDim( screenNum ) ) );
	}

	public static BufferedImage subImage(BufferedImage img, Rectangle rect) {
		return img.getSubimage( rect.x, rect.y, rect.width, rect.height );
	}

	private static FullscreenRegionSelectionWindow frame;

	public static BufferedImage captureRegion() {
		BufferedImage img = captureScreen( 0 );

		frame = new FullscreenRegionSelectionWindow( img );
		screens[0].setFullScreenWindow( frame );

		while (frame == null || !frame.regionSelected) {
			try {
				Thread.sleep( 50 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		screens[0].setFullScreenWindow( null );
		Rectangle sel = frame.selection;
		frame.dispose();
		frame = null;

		return subImage( img, sel );
	}

}
