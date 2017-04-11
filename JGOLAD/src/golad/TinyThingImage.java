package golad;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TinyThingImage extends TinyThing {
	BufferedImage[] textures;
	
	public TinyThingImage(int ticks,String name) {
		super(ticks-1);
		textures = new BufferedImage[ticks];
		for(int i = 0; i < textures.length; i++){
			textures[i] = ImageUtil.loadImage("/"+name+"_"+i+".png");
		}
	}

	@Override
	public void render(Graphics2D g,int lh) {
		BufferedImage bi = textures[tick];
		int width = 200;
		int height = width;
		g.drawImage(bi, 650, 100+lh, width, height, null);
	}
}
