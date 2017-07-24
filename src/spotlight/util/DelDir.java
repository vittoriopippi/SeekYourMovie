package spotlight.util;

import java.io.File;

public class DelDir {

	/*
	 * Per eliminare cartelle, eliminando ricorsivamente tutti i suoi elementi
	 */
	public static void delDir(String dir) {
		boolean success = delDirRec(new File(dir));
//		if (success) {
//			System.out.println("Ho cancellato la cartella: " + dir);
//		} else {
//			System.out.println("Impossibile cancellare la cartella: " + dir);
//		}
	}

	/*
	 * Per eliminare cartelle NON vuote, eliminando ricorsivamente tutti i suoi
	 * elementi
	 */
	public static boolean delDirRec(File dir) {
		if (dir.isDirectory()) {
			String[] contenuto = dir.list();
			for (int i = 0; i < contenuto.length; i++) {
				boolean success = delDirRec(new File(dir, contenuto[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
