package mightyelemental.opensharez;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageManipulator {

	/**
	 * Applies a blur to the image
	 * 
	 * @param imgToBlur the image to apply the blur to
	 * @param radius the blur radius
	 * @return The blurred image
	 */
	public static BufferedImage blurImage( BufferedImage imgToBlur, int radius ) {
		int[] pixels = Utils.imageToPixelArray(imgToBlur);
		int[] endPixels = Arrays.copyOf(pixels, pixels.length);
		int width = imgToBlur.getWidth();
		for (int x = radius; x < width - radius; x++) {
			for (int y = radius; y < imgToBlur.getHeight() - radius; y++) {
				int totalR = 0, totalG = 0, totalB = 0;
				for (int i = -radius; i <= radius; i++) {
					for (int j = -radius; j <= radius; j++) {
						int rgb = pixels[(y + i) * width + x + j];
						int red = (rgb >> 16) & 0xFF;
						int green = (rgb >> 8) & 0xFF;
						int blue = rgb & 0xFF;
						totalR += red;
						totalG += green;
						totalB += blue;
					}
				}
				int rgb = totalR / ((radius + 1) * (radius + 1));
				rgb = (rgb << 8) + totalG / ((radius + 1) * (radius + 1));
				rgb = (rgb << 8) + totalB / ((radius + 1) * (radius + 1));
				endPixels[y * width + x] = rgb;
			}
		}
		return Utils.pixelArrayToImage(endPixels, width, imgToBlur.getHeight());
	}

	/**
	 * Used to blur a section of an image.
	 * 
	 * @see #blurImage(BufferedImage, int)
	 * 
	 * @param orig the original image
	 * @param blurRec the region of the image to blur
	 * @param radius the radius of the blur
	 * @return The image with a section blurred
	 */
	public static BufferedImage blurRegion( BufferedImage orig, Rectangle blurRec, int radius ) {
		BufferedImage result = orig;
		BufferedImage sub = orig.getSubimage(blurRec.x, blurRec.y, blurRec.width, blurRec.height);
		BufferedImage subBlur = blurImage(sub, radius);
		Graphics g = result.getGraphics();
		g.drawImage(subBlur, blurRec.x, blurRec.y, null);
		g.dispose();
		return result;
	}

	/**
	 * Pixelates an image
	 * 
	 * @param img the image to apply the pixelation to
	 * @param radius the pixelation radius
	 * @return The pixelated image
	 */
	public static BufferedImage pixelateImage( BufferedImage img, int rad ) {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = result.getGraphics();
		for (int x = 0; x < img.getWidth(); x += rad) {
			for (int y = 0; y < img.getHeight(); y += rad) {
				g.setColor(new Color(img.getRGB(x, y)));
				g.fillRect(x, y, rad, rad);
			}
		}
		g.dispose();
		return result;
	}

}
