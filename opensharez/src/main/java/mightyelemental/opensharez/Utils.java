package mightyelemental.opensharez;

import static mightyelemental.opensharez.OpenShareZ.HOME_DIR;

import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class Utils {

	private static Calendar calendar = Calendar.getInstance();

	public static int getYear() {
		calendar.setTimeInMillis( System.currentTimeMillis() );
		return calendar.get( Calendar.YEAR );
	}

	public static int getMonth() {
		calendar.setTimeInMillis( System.currentTimeMillis() );
		return calendar.get( Calendar.MONTH ) + 1;
	}

	public static void saveImage(BufferedImage img, String filename) throws IOException {
		if (img == null) return;
		int year = getYear();
		int month = getMonth();
		String path = String.format( "/Pictures/Screenshots/%4d-%02d", year, month );
		createDirectories( HOME_DIR + path );
		File f = new File(
				String.format( "%s%s/%s_%s.png", HOME_DIR, path, filename, generateTimeStamp() ) );
		System.out.printf( "Saved image %s\n", f.getPath() );
		ImageIO.write( img, "png", f );
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

	public static void getWindows() {

	}

	public static int getCursorScreenNum() {
		GraphicsDevice[] screens = CaptureOperations.screens;
		int i = 0;
		for (i = 0; i < screens.length; i++) {
			GraphicsDevice gd = screens[i];
			if (gd.getDefaultConfiguration().getBounds()
					.contains( MouseInfo.getPointerInfo().getLocation() )) {
				break;
			}
		}
		return i;
	}

}
