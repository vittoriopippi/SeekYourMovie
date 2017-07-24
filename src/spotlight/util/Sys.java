package spotlight.util;

public class Sys {
	/* Nome Utente */
	public final static String user_name=System.getProperty("user.name");
	
	/* Nome cartella HOME */
	public final static String user_home=System.getProperty("user.home");
	
	/* Nome cartella corrente */
	public final static String user_dir=System.getProperty("user.dir");
	
	/* Separatore dei file  */
	public final static String file_separator=System.getProperty("file.separator");
	
	/* Carattere di a capo */
	public final static String line_separator=System.getProperty("line.separator");
	
	/* Separatore nel PATH  */
	public final static String path_separator=System.getProperty("path.separator");
	
	/* Nome S.O. */
	public final static String os_name=System.getProperty("os.name");
	
}
