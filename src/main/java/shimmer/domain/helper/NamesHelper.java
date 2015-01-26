package shimmer.domain.helper;

import org.springframework.util.StringUtils;

/**
 * Helps with providing names and captions.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class NamesHelper {

	public static String enumToNiceString(Enum<?> e) {
		if (e != null) {
			return  StringUtils.capitalize(e.name().toLowerCase().replace("_", " "));
		} else {
			return "";
		}
	}
	
}