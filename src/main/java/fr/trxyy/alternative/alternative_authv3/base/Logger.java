package fr.trxyy.alternative.alternative_authv3.base;

/**
 * @author Trxyy
 */
public abstract class Logger {

	/**
	 * Log a text
	 * @param s The text to log
	 */
	public static void log(String s) {
		System.out.println(getName() + s);
	}

	/**
	 * Log a text with error
	 * @param s The text to log
	 */
	public static void err(String s) {
		System.err.println(getName() + s);
	}
	
	public static String getName() {
		return "[AAuth]";
	}

}
