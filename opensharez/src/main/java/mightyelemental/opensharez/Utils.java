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
import java.lang.ProcessBuilder.Redirect;
import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Utils {

	private static Calendar calendar = Calendar.getInstance();

	/**
	 * Gets the current year
	 * 
	 * @return The current year
	 */
	public static int getYear() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Gets the current month as its integer value
	 * 
	 * @return The current numerical month
	 */
	public static int getMonth() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * Get the default path the screenshot should be saved to
	 * 
	 * @return The default path to the screenshot folder
	 */
	public static String getScreenshotPath() {
		return String.format("%s/Pictures/Screenshots/%4d-%02d", OpenShareZ.HOME_DIR, getYear(), getMonth());
	}

	/**
	 * Saves the screenshot as a png
	 * 
	 * @param img the image to save
	 * @param filename the name the file should have
	 * @return The filepath of the image
	 * @throws IOException If an error occurs during writing or when not able to create required ImageOutputStream.
	 * @see ImageIO#write(java.awt.image.RenderedImage, String, File)
	 */
	public static String saveImage( BufferedImage img, String filename ) throws IOException {
		if (img == null) return null;

		String path = getScreenshotPath();
		createDirectories(path);
		File f = new File(String.format("%s/%s_%s.png", path, filename, generateTimeStamp()));
		System.out.printf("Saved image %s\n", f.getPath());
		ImageIO.write(img, "png", f);
		return f.getPath();
	}

	/**
	 * Creates a timestamp with the following format:<br>
	 * {@code String.format("%4d-%02d-%02d_%02d-%02d-%02d", year, month, day, hour, mins, sec);}
	 * 
	 * @return The timestamp in String form
	 */
	public static String generateTimeStamp() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int mins = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		return String.format("%4d-%02d-%02d_%02d-%02d-%02d", year, month, day, hour, mins, sec);
	}

	/**
	 * Creates a folder structure if one does not already exist
	 * 
	 * @param path the folder structure to create
	 * @see File#mkdirs()
	 */
	private static void createDirectories( String path ) {
		File f = new File(path);
		if (!f.exists()) f.mkdirs();
	}

	/** Get a list of open windows */
	public static void getWindows() {

	}

	/**
	 * Initiates an ffmpeg process to record a section of the screen
	 * 
	 * @param rect the region of the screen to record
	 * @param fps the FPS the screen should be recorded at
	 * @param filename the filename to save the video as
	 * @return The ffmpeg process
	 * @throws IOException if an I/O error occurs
	 * @see ProcessBuilder
	 */
	public static Process recordScreen( Rectangle rect, int fps, String filename ) throws IOException {// TODO: allow for non-linux
																																		// screen recording
		String audio = "-f pulse -ac 1 -i default";// -af \"aresample=async=1000\"
		String cmd = String.format("ffmpeg -video_size %dx%d -framerate %d -f x11grab -i :0.0+%d,%d %s -preset ultrafast -threads 0 %s/%s", rect.width, rect.height, fps, rect.x,
				rect.y, audio, getScreenshotPath(), filename);
		System.out.println(cmd);
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		return pb.start();
	}

	/**
	 * Round an integer up to the nearest 16.
	 * 
	 * @param val the integer to round
	 * @return The rounded value
	 */
	public static int roundToNearest16( int val ) {
		return ((val - 1) | 15) + 1;
	}

	/**
	 * Get the ID of the screen the cursor is currently on
	 * 
	 * @return The ID of the screen the cursor is on
	 */
	public static int getCursorScreenNum() {
		GraphicsDevice[] screens = CaptureOperations.screens;
		int i = 0;
		for (i = 0; i < screens.length; i++) {
			GraphicsDevice gd = screens[i];
			if (gd.getDefaultConfiguration().getBounds().contains(MouseInfo.getPointerInfo().getLocation())) {
				break;
			}
		}
		return i;
	}

	/**
	 * Check if window transparency is supported
	 */
	public static void verifySupportForTransparency() {
		// Determine what the default GraphicsDevice can support.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
		boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
		boolean isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);

		System.out.println(isUniformTranslucencySupported);
		System.out.println(isPerPixelTranslucencySupported);
		System.out.println(isShapedWindowSupported);
	}

	public static void setDefault() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
	}

	public static void setGTK() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame.setDefaultLookAndFeelDecorated(false);
	}

	/**
	 * Show the preview window
	 * 
	 * @param img the image to display
	 * @param title the title of the image
	 * @see Preview
	 */
	public static void showPreview( BufferedImage img, String title ) {
		setGTK();
		Preview p = new Preview(img, title);
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
		exec.schedule(() -> {
			p.dispose();
		}, 4, TimeUnit.SECONDS);
	}

	/** Stops ongoing ffmpeg processes */
	public static void stopRecording() { // TODO: Fix so it doesn't kill any and every ffmpeg process
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
		exec.schedule(() -> {
			try {
				Runtime.getRuntime().exec("killall --user $USER --ignore-case --signal SIGTERM  ffmpeg");
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("stopped recording");
		}, 500, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops ongoing ffmpeg processes and destroys the specified process.
	 * 
	 * @param ffmpeg the process to destroy
	 */
	public static void stopRecording( Process ffmpeg ) {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
		exec.schedule(() -> {
			try {
				Runtime.getRuntime().exec("killall --user $USER --ignore-case --signal SIGTERM  ffmpeg");
			} catch (IOException e) {
				e.printStackTrace();
			}
			ffmpeg.destroy();
			System.out.println("stopped recording");
		}, 500, TimeUnit.MILLISECONDS);
	}

	/**
	 * Converts a BufferedImage to an array of integers encoded by RGB.
	 * 
	 * @param img the image to convert
	 * @return An array of integers containing the image data
	 * @see BufferedImage#getRGB(int, int, int, int, int[], int, int)
	 * @see #pixelArrayToImage(int[], int, int)
	 */
	public static int[] imageToPixelArray( BufferedImage img ) {
		int width = img.getWidth(), height = img.getHeight();
		int[] pixels = new int[width * height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		return pixels;
	}

	/**
	 * Converts an integer array to a BufferedImage.
	 * 
	 * @param pixels the integer array to convert
	 * @param width the width of the image
	 * @param height the height of the image
	 * @return The image created from the pixel array
	 * @see BufferedImage#setRGB(int, int, int, int, int[], int, int)
	 * @see #imageToPixelArray(BufferedImage)
	 */
	public static BufferedImage pixelArrayToImage( int[] pixels, int width, int height ) {
		BufferedImage endImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		endImage.setRGB(0, 0, width, height, pixels, 0, width);
		return endImage;
	}

}
