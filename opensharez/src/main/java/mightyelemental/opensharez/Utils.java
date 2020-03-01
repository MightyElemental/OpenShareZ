package mightyelemental.opensharez;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;
import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

	public static String getScreenshotPath() {
		return String.format( "%s/Pictures/Screenshots/%4d-%02d", OpenShareZ.HOME_DIR, getYear(),
				getMonth() );
	}

	public static String saveImage(BufferedImage img, String filename) throws IOException {
		if (img == null) return null;

		String path = getScreenshotPath();
		createDirectories( path );
		File f = new File( String.format( "%s/%s_%s.png", path, filename, generateTimeStamp() ) );
		System.out.printf( "Saved image %s\n", f.getPath() );
		ImageIO.write( img, "png", f );
		return f.getPath();
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

	public static Process recordScreen(Rectangle rect, int fps, String filename) throws IOException {// TODO: allow for non-linux screen recording
		String cmd = String.format(
				"ffmpeg -video_size %dx%d -framerate %d -f x11grab -i :0.0+%d,%d %s %s/%s", rect.width,
				rect.height, fps, rect.x, rect.y, "-f pulse -ac 2 -i default", getScreenshotPath(),
				filename );
		System.out.println( cmd );
		return Runtime.getRuntime().exec( cmd );
	}

	public static void stopRecording() {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
		exec.schedule( () -> {
			try {
				Runtime.getRuntime()
						.exec( "killall --user $USER --ignore-case --signal SIGTERM  ffmpeg" );
			} catch (IOException e) {
				e.printStackTrace();
			}
			OpenShareZ.TASK_COMPLETE.play();
		}, 1, TimeUnit.SECONDS );
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

	public static void verifySupportForTransparancy() {
		// Determine what the default GraphicsDevice can support.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported( TRANSLUCENT );
		boolean isPerPixelTranslucencySupported = gd
				.isWindowTranslucencySupported( PERPIXEL_TRANSLUCENT );
		boolean isShapedWindowSupported = gd.isWindowTranslucencySupported( PERPIXEL_TRANSPARENT );

		System.out.println( isUniformTranslucencySupported );
		System.out.println( isPerPixelTranslucencySupported );
		System.out.println( isShapedWindowSupported );
	}

	public static void setDefault() {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame.setDefaultLookAndFeelDecorated( true );
	}

	public static void setGTK() {
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
		JFrame.setDefaultLookAndFeelDecorated( false );
	}

	public static void showPreview(BufferedImage img, String title) {
		Preview p = new Preview( img, title );
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
		exec.schedule( () -> { p.dispose(); }, 4, TimeUnit.SECONDS );
	}

}
