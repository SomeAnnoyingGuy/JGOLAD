package golad;

import java.awt.Color;

public class HueSine {
	private static Color[] colorArr;
	
	static {
		float frequency = 0.05f;
		int colors = (int) (63 / (frequency * 10));

		float amplitude = 255f / 2f;
		float center = amplitude;

		colorArr = new Color[colors];

		for (float i = 0f; i < colors; i++) {
			int r = (int) (Math.sin((double) (frequency * i + 0)) * amplitude + center);
			int g = (int) (Math.sin((double) (frequency * i + 2)) * amplitude + center);
			int b = (int) (Math.sin((double) (frequency * i + 4)) * amplitude + center);
			Color c = rgb(r, g, b);
			colorArr[(int) i] = c;
		}
	}
	
	public static Color get(int id){
		return colorArr[id%(colorArr.length-1)];
	}
	
	public static int getMax(){
		return colorArr.length-1;
	}
	
	public static Color rgb(int r, int g, int b) {
		return new Color(r, g, b);
	}
}