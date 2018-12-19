package systems;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

public class Logger {

	private static long startTime = java.lang.System.currentTimeMillis();
	private static final PrintStream err = logNameToStream("err"), log = logNameToStream("log"), info = logNameToStream("info");
	private static final Queue<String> errBuffer = new LinkedList<String>(), logBuffer = new LinkedList<String>(), infoBuffer = new LinkedList<String>();
	private static int counter = 0;
	
	public static enum LogType {
		ERR_LOG,
		DATA_LOG,
		INFO_LOG
	}
	
	public static void log(Object o, LogType type) {
		String time = java.lang.System.currentTimeMillis() - startTime + " ";
		if(o == null) {
			return;
		}
		switch(type) {
		case DATA_LOG:
			logBuffer.add(time + o.toString());
			break;
		case INFO_LOG:
			infoBuffer.add(time + o.toString());
			break;
		case ERR_LOG:
		default:
			errBuffer.add(time + o.toString());
			java.lang.System.err.println(">> " + time + o.toString());
			break;
		}
		if(++counter >= 1000) {
			log();
		}
	}
	
	public static void forceLog() {
		log();
	}
	
	private static void log() {
		StringBuffer e = new StringBuffer(), i = new StringBuffer(), d = new StringBuffer();
		while(!errBuffer.isEmpty()) {
			e.append(errBuffer.poll() + "\r\n");
		}
		while(!infoBuffer.isEmpty()) {
			i.append(infoBuffer.poll() + "\r\n");
		}
		while(!logBuffer.isEmpty()) {
			d.append(logBuffer.poll() + "\r\n");
		}
		err.print(e.toString());
		info.print(i.toString());
		log.print(d.toString());
		counter = 0;
	}
	
	private static PrintStream logNameToStream(String filename) {
		return pathToStream(filename + startTime + ".log");
	}
	
	private static PrintStream pathToStream(String filename) {
		try {
			File f = new File(filename);
			if(f.exists()) {
				f.delete();
				f.createNewFile();
			}
			return new PrintStream(f);
		}catch(Exception e) {
			e.printStackTrace();
			return java.lang.System.err;
		}
	}
}
