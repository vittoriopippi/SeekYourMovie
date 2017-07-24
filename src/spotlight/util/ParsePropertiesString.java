package spotlight.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class ParsePropertiesString {
	
	public static Properties parse(String s) {
	    final Properties p = new Properties();
	    try {
			p.load(new StringReader(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return p;
	}
	
}
