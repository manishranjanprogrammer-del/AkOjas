package com.cslsoftware.util;

/**
 * Insert the type's description here.
 * Creation date: (11/12/2002 02:56:59)
 * @author: Administrator
 */
import java.io.*;
import java.util.Date;
/**
 * @com.register ( clsid=765D5C23-0DC4-11D7-8468-0002442CEF43, typelib=1A12CD61-F6BD-11D6-8ECE-0002442CEF43 )
 */
public class CSystem {
	public static String strPathLogFile;
	public static String DEBUG = "DEBUG";
	public static String INFO = "INFO";
	public static String WARNING = "WARNING";
	public static String ERRO_R = "ERROR";
	protected static boolean DEBUG_STATUS = true;
	protected static boolean INFO_STATUS = false;
	protected static boolean WARNING_STATUS = true;
	protected static boolean ERROR_STATUS = true;
/**
 * CSystem constructor comment.
 */
public CSystem() {
	super();
}
/**
 * CSystem constructor comment.
 */
public static void Log(String logMessage, BufferedWriter w) throws IOException {
	Date date = new Date();
	w.newLine();
	//w.write("\n");
	w.write(" Log Entry : ");
	w.write(date.toString());
	w.write(" : ");
	w.write(logMessage);
	w.flush();
}
/**
 * CSystem constructor comment.
 */
public static void out(String ErrorType, String conStr) {
	try {
		if (ErrorType.equalsIgnoreCase("DEBUG")) {
			BufferedWriter fileDebug = new BufferedWriter(new FileWriter(strPathLogFile + "DebugLog.txt", true));
			Log(conStr, fileDebug);
			fileDebug.close();
			if (DEBUG_STATUS == true)
				System.out.println(conStr);
		} else
			if (ErrorType.equalsIgnoreCase("INFO")) {
				BufferedWriter fileInfo = new BufferedWriter(new FileWriter(strPathLogFile + "InfoLog.txt", true));
				Log(conStr, fileInfo);
				fileInfo.close();
				if (INFO_STATUS == true)
					System.out.println(conStr);
			} else
				if (ErrorType.equalsIgnoreCase("ERROR")) {
					BufferedWriter fileError = new BufferedWriter(new FileWriter(strPathLogFile + "ErrorLog.txt", true));
					Log(conStr, fileError);
					fileError.close();
					if (ERROR_STATUS == true)
						System.out.println(conStr);
				} else
					if (ErrorType.equalsIgnoreCase("WARNING")) {
						BufferedWriter fileWarning = new BufferedWriter(new FileWriter(strPathLogFile + "WarningLog.txt", true));
						Log(conStr, fileWarning);
						fileWarning.close();
						if (WARNING_STATUS == true) {
							System.out.println(conStr);
						}
					}
	} catch (IOException ex) {
		System.out.println(ex);
		ex.printStackTrace();
	}
}
/**
 * CSystem constructor comment.
 */
public void setLogPath(String path) {
	strPathLogFile = path;
}
}
