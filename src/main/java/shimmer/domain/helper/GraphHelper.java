package shimmer.domain.helper;

import org.springframework.util.StringUtils;

/**
 * Helps with small Graph problems.
 * 
 * @author filip.daca@javatech.com.pl
 */
public class GraphHelper {

	public static String getFullPackageName(String parentNameList, 
			String childName) {
		if (StringUtils.hasText(parentNameList)) {
			return parentNameList + "." + childName;
		} else {
			return childName;
		}
	}
	
}
