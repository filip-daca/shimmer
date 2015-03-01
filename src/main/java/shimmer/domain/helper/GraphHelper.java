package shimmer.domain.helper;

import org.springframework.util.StringUtils;

/**
 * Helps with small Graph problems.
 * 
 * @author Filip Daca
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
