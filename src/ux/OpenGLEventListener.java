package ux;

import com.jogamp.opengl.GLAutoDrawable;

public class OpenGLEventListener implements com.jogamp.opengl.GLEventListener {

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		System.out.println("display called");
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		System.out.println("dispose called");
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		System.out.println("init called");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		System.out.println("reshape called");
	}

}
