package mightyelemental.opensharez;

import static mightyelemental.opensharez.OpenShareZ.HOME_DIR;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class CaptureOperations {

	// public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Robot           robot;
	private static Calendar        calendar = Calendar.getInstance();
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

	public static int getYear() {
		calendar.setTimeInMillis( System.currentTimeMillis() );
		return calendar.get( Calendar.YEAR );
	}

	public static int getMonth() {
		calendar.setTimeInMillis( System.currentTimeMillis() );
		return calendar.get( Calendar.MONTH ) + 1;
	}

	public static String generateTimeStamp() {
		calendar.setTimeInMillis( System.currentTimeMillis() );
		int year = calendar.get( Calendar.YEAR );
		int month = calendar.get( Calendar.MONTH ) + 1;
		int day = calendar.get( Calendar.DATE );
		int hour = calendar.get( Calendar.HOUR_OF_DAY );
		int mins = calendar.get( Calendar.MINUTE );
		int sec = calendar.get( Calendar.SECOND );
		return String.format( "%4d-%02d-%02d_%02d-%02d-%02d", year, month, day, hour, mins, sec );
	}

	private static void createDirectories(String path) {
		File f = new File( path );
		if (!f.exists()) f.mkdirs();
	}

	public static void saveImage(BufferedImage img, String filename) throws IOException {
		if (img == null) return;
		int year = getYear();
		int month = getMonth();
		String path = String.format( "/Pictures/Screenshots/%4d-%02d", year, month );
		createDirectories( HOME_DIR + path );
		File f = new File(
				String.format( "%s%s/%s_%s.png", HOME_DIR, path, filename, generateTimeStamp() ) );
		System.out.printf( "Saved image %s", f.getPath() );
		ImageIO.write( img, "png", f );
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
