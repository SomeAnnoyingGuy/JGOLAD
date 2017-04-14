package util;

public class MathUtil {
	public static int randInt(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public static double randDouble(double max, double min) {
		return min + (Math.random() * ((max - min) + 1));
	}

	public static int unboxInteger(Integer i) {
		if(i == null){
			return 0;
		}
		return Integer.valueOf(i);
	}

	public static int unboxInteger(Object value) {
		if(value instanceof Integer){
			return unboxInteger((Integer)value);
		}
		return 0;
	}
}
