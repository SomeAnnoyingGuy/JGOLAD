package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static BufferedImage loadImage(String fileName) {
		fileName = "/img" + fileName;
		System.out.println("Loading " + fileName);
		BufferedImage buff = null;
		try {
			buff = ImageIO.read(ImageUtil.class.getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return buff;
	}

	public static BufferedImage loadImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			WinUtil.display("Warning! Image " + file.getAbsolutePath() + " does not exist.");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void saveImage(BufferedImage img, File f) {
		try {
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			WinUtil.display("Saving image failed: " + e.getMessage() +"\n"
					+ "Caused by: " + e.getCause().getMessage());
			e.printStackTrace();
		}
	}
}
