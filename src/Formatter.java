import java.util.Arrays;
import java.util.List;


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

	//Java replace function being a real a-hole
	private static String replaceTabs(String str){ 
		List<String> splitList = Arrays.asList(str.split("\\\\t"));
		String strSum = "";
		for(String strElement : splitList){
			strSum += strElement + "\t";
		}
		return strSum;
	}
}
