package spotlight.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import spotlight.exceptions.ExceptionDialog;

public class ParsePropertiesString {

	public static Properties parse(String s) {
		final Properties p = new Properties();
		try {
			p.load(new StringReader(s));
		} catch (IOException e) {
			ExceptionDialog.show(e);
			e.printStackTrace();
		}
		return p;
	}

}
