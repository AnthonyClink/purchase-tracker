package purchasetracker;

import java.util.regex.Pattern;

public class Constants {
	
	  public static final Pattern IS_FLOATING_NUMBER_PATTERN = Pattern.compile("[-+]?(\\d*[.])?\\d+");
	  public static final Pattern IS_INTEGER_PATTERN = Pattern.compile("[-+]?\\d+");
	  
}
