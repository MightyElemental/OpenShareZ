package mightyelemental.opensharez;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AfterCaptureOperations {

	/**
	 * The actions to run after a capture is complete
	 * 
	 * @param img the image that was captured
	 * @param name the name of the image
	 * @throws IOException If an error occurs during writing or when not able to create required ImageOutputStream.
	 * @see Utils#saveImage(BufferedImage, String)
	 */
	public static void runAfterCaptureOps( BufferedImage img, String name ) throws IOException {
		String path = Utils.saveImage(img, name);// TODO: config file
		copyImageToClipboard(img);// TODO: add config file
		Utils.showPreview(img, path);
		OpenShareZ.TASK_COMPLETE.play();
	}

	/**
	 * Copy the image to the clipboard to allow the user to paste it elsewhere.
	 * 
	 * @param img the image to copy to the clipboard
	 * @see TransferableImage
	 */
	public static void copyImageToClipboard( BufferedImage img ) {
		TransferableImage trans = new TransferableImage(img);
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(trans, null);
	}

}
