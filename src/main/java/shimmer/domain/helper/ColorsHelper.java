package shimmer.domain.helper;

import java.awt.Color;

/**
 * Helps with color transitions and conversions.
 * @author filip.daca@javatech.com.pl
 */
public class ColorsHelper {
	
	/**
	 * Returns the color in between two colors
	 * represented by values.
	 * @param color1
	 * @param color2
	 * @param value1
	 * @param value2
	 * @param value
	 * @return color between given colors
	 */
	public static Color colorTransition(Color color1, Color color2, 
			float value1, float value2, float value) {
		if (value <= value1) {
			return color1;
		} else if (value >= value2) {
			return color2;
		}
		
		float proportion = value / (value2 - value1);
		
		int r = Math.round((color2.getRed() - color1.getRed()) * proportion);
		int g = Math.round((color2.getGreen() - color1.getGreen()) * proportion);
		int b = Math.round((color2.getBlue() - color1.getBlue()) * proportion);
		
		r = r + color1.getRed();
		g = g + color1.getGreen();
		b = b + color1.getBlue();
		
		r = r < 0 ? 0 : (r > 256 ? 256 : r);
		g = g < 0 ? 0 : (g > 256 ? 256 : g);
		b = b < 0 ? 0 : (b > 256 ? 256 : b);
		
		return new Color(r, g, b);
	}
}
