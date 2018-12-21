package messages;

import java.util.Scanner;

public abstract class Message {

	public static Class<? extends Message> clazz = messages.types.Null.class;
	private static Scanner scanner;
	
	protected final static String type = "";
	
	public abstract String toString();
	
	public static Message readMessage(String s) {
		s = s.toLowerCase();
		scanner = new Scanner(s);
		try {
			String type = scanner.next();
			System.out.println(type);
			if(type.equals(CameraMessage.type)) {
				return new CameraMessage(scanner);
			}else if(type.equals(Start.type)) {
				return new Start();
			}else if(type.equals(Exit.type)) {
				return new Exit();
			}
			System.out.println("failed to find type");
		}catch(Exception e) {
			systems.Logger.log(e.getMessage(), systems.Logger.LogType.ERR_LOG);
			e.printStackTrace();
		}
		return null;
	}
	
	public String type() {
		return this.type;
	}
	
	public void log() {
		systems.Logger.log(this, systems.Logger.LogType.DATA_LOG);
	}
	public void errlog() {
		systems.Logger.log("Could not handle message type " + this.getClass(), systems.Logger.LogType.ERR_LOG);
	}
}
