import java.util.Arrays;
import java.util.List;

import org.gitective.core.Assert;


public class Formatter {
	public static String formatDiff(String diff){
		String formattedDiff = "";

		diff = replaceTabs(diff);
		
		String lines[] = diff.split("\\\\n");
		for(String jsonElement : lines) {
			formattedDiff += (jsonElement + "\n");
		}
		
		return formattedDiff;
	}

	//Java replace function being a real a-hole, so I wrote my own
	private static String replaceTabs(String str){ 
		Assert.notNull(str);
		List<String> splitList = Arrays.asList(str.split("\\\\t"));
		String strSum = "";
		for(String strElement : splitList){
			strSum += strElement + "\t";
		}
		return strSum;
	}
}
