package golad;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static BufferedImage loadImage(String fileName) {
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
}
