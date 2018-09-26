package eu.bcvsolutions.winrm.checker;

import java.util.Date;

/**
 * Should be a singleton. No reason to do that in the first version.
 * TODO: make singleton when the need arises
 * @author fiisch
 *
 */
public class CheckerLogger {
	
	public void log(String message) {
		logInfo(message);
	}
	
	private void logInfo(String message) {
		System.out.println((new Date()).toString() + " [INFO]: " + message);
	}

}
