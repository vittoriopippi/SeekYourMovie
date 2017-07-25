package spotlight.util;

import java.io.File;

public class CreaDir {
	public static void creaDir(String dir) {
		boolean success = (new File(dir)).mkdirs();
	}
}
