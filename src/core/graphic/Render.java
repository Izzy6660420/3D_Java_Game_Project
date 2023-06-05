package core.graphic;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import core.engine.io.Window;
import core.scene.Scene;

public class Render {
	
	private SceneRender sceneRender;
	
	public Render() {
		GL.createCapabilities();
		sceneRender = new SceneRender();
	}
	
	public void cleanup() {
		sceneRender.cleanup();
	}
	
	public void render(Window window, Scene scene) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
		sceneRender.render(scene);
	}
}
