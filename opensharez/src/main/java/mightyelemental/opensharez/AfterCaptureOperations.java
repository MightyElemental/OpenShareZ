package mightyelemental.opensharez;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AfterCaptureOperations {

	public static void runAfterCaptureOps(BufferedImage img, String name) throws IOException {
		String path = Utils.saveImage( img, name );// TODO: config file
		copyImageToClipboard( img );// TODO: add config file
		Utils.showPreview( img, path );
		OpenShareZ.TASK_COMPLETE.play();
	}

	public static void copyImageToClipboard(BufferedImage img) {
		TransferableImage trans = new TransferableImage( img );
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents( trans, null );
	}

}
