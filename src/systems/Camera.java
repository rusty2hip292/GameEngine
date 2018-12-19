package systems;

import game.Application;

public class Camera extends System {

	public Camera(Application owner) {
		super(owner);
	}

	@Override
	public void run() {
		while(running);
	}

	@Override
	protected void handleMessage(messages.Message m) {
		// TODO Auto-generated method stub
		
	}

	protected Class<? extends messages.Message> messageTypeToHandle() {
		return messages.types.HandleByCamera.class;
	}

}
