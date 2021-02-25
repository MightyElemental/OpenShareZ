package mightyelemental.opensharez;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

public class CaptureOperations {

	// public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Robot					robot;
	public static GraphicsDevice[]	screens				= GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	public static Rectangle				maxWindowBounds	= new Rectangle(0, 0, 0, 0);

	private static boolean isRecording;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		System.out.println(getMonitorSizes());
		for (GraphicsDevice gd : screens) {
			maxWindowBounds = maxWindowBounds.union(gd.getDefaultConfiguration().getBounds());
		}
	}

	/**
	 * Creates a string containing the monitor IDs and their sizes
	 * 
	 * @return The monitor size information
	 */
	private static String getMonitorSizes() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < screens.length; i++) {
			DisplayMode dm = screens[i].getDisplayMode();
			sb.append(String.format("Screen %d: width: %d, height: %d\n", i, dm.getWidth(), dm.getHeight()));
		}
		return sb.toString();
	}

	/**
	 * Gets the boundaries of a specific monitor
	 * 
	 * @param screenNum the monitor number/ID
	 * @return The boundaries of a specific monitor
	 */
	public static Rectangle getScreenBounds( int screenNum ) {
		if (screenNum > screens.length - 1 || screenNum < 0) return null;
		return screens[screenNum].getDefaultConfiguration().getBounds();
	}

	/**
	 * Takes a screenshot of a specified monitor
	 * 
	 * @param screenNum the monitor number/ID
	 * @return A screenshot of the specified monitor
	 */
	public static BufferedImage captureScreen( int screenNum ) {
		return robot.createScreenCapture(getScreenBounds(screenNum));
	}

	/**
	 * Takes a single screenshot that contains all monitors
	 * 
	 * @return A screenshot of all screens
	 */
	public static BufferedImage captureAllDisplays() {
		return robot.createScreenCapture(maxWindowBounds);
	}

	/**
	 * Returns a section of the image
	 * 
	 * @param img the image to crop
	 * @param rect the selection to crop the image to
	 * @return The cropped image
	 */
	public static BufferedImage subImage( BufferedImage img, Rectangle rect ) {
		return img.getSubimage(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Displays the img in a window and allows the user to select a region
	 * 
	 * @param img the image to capture the region of (usually a capture of a full monitor)
	 * @param showEditGUI whether or not the edit GUI should be shown
	 * @return the selected region
	 * @see FullscreenRegionSelectionWindow
	 */
	public static Rectangle promptUserForRegion( BufferedImage img, boolean showEditGUI ) {
		Utils.setGTK();
		JFrame.setDefaultLookAndFeelDecorated(false);
		int i = Utils.getCursorScreenNum();
		FullscreenRegionSelectionWindow frame = new FullscreenRegionSelectionWindow(img);
		screens[i].setFullScreenWindow(frame);

		while ((frame == null || !frame.regionSelected) && !frame.cancelled) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		screens[i].setFullScreenWindow(null);
		Rectangle sel = frame.selection;
		frame.dispose();
		if (frame.cancelled) sel = null;
		return sel;
	}

	/**
	 * Calls the {@link #captureRegion(boolean)} method with value false.
	 */
	public static BufferedImage captureRegion() {
		return captureRegion(false);
	}

	/**
	 * Presents the user with a window to choose a region of the current monitor.
	 * 
	 * @param showEditGUI whether or not to show the extra options to allow capture editing
	 * @return The region capture with edits
	 */
	public static BufferedImage captureRegion( boolean showEditGUI ) {
		int i = Utils.getCursorScreenNum();
		BufferedImage img = captureScreen(i);
		Rectangle sel = promptUserForRegion(img, showEditGUI);

		if (sel == null) {
			return null;
		} else {
			return subImage(img, sel);
		}
	}

	private static RecordHUDFrame	hud;
	private static Process			ffmpeg;

	/**
	 * Stops any previous recordings that may be ongoing, then starts a new screen recording based on a user-selected region
	 * of the current monitor.
	 */
	public static void startScreenRecord() {
		if (isRecording) {
			if (hud != null) {
				hud.setVisible(false);
				hud.dispose();
				hud = null;
			}
			Utils.stopRecording(ffmpeg);
			isRecording = false;
			return;
		}
		int i = Utils.getCursorScreenNum();
		BufferedImage img = captureScreen(i);
		Rectangle sel = promptUserForRegion(img, false);
		sel.width = Utils.roundToNearest16(sel.width);
		sel.height = Utils.roundToNearest16(sel.height);

		isRecording = true;
		sel.x += getScreenBounds(i).x;
		sel.y += getScreenBounds(i).y;

		Utils.setDefault();
		try {
			ffmpeg = Utils.recordScreen(sel, 25, "recording_" + Utils.generateTimeStamp() + ".mp4");
			hud = new RecordHUDFrame(sel, ffmpeg);
			hud.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
