package spotlight.util;

import java.io.File;

public class CreaDir {
	public static void creaDir(String dir) {
		boolean success = (new File(dir)).mkdirs();
//		if (success) {
//			System.out.println("Ho creato: " + dir);
//		} else {
//			System.out.println("Impossibile creare: " + dir);
//		}
	}
}
