package shimmer.domain.helper;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Helps with providing names and captions.
 * 
 * @author Filip Daca
 */
public class NamesHelper {

	public static String enumToNiceString(Enum<?> e) {
		if (e != null) {
			return  StringUtils.capitalize(e.name().toLowerCase().replace("_", " "));
		} else {
			return "";
		}
	}
	
	public static List<String> packageNamesFromFilePath(String path) {
		List<String> packageNames = new LinkedList<String>();
		if (StringUtils.hasText(path)) {
			path = path.replace(File.separatorChar, ':');
			String[] parts = path.split(":");
			String name = parts[parts.length - 2];
			packageNames.add(name);
			
			for (int i = parts.length - 3; i > 0; i--) {
				name = parts[i] + "." + name;
				packageNames.add(name);
			}
			
			Collections.reverse(packageNames);
		}
		return packageNames;
	}
	
}
