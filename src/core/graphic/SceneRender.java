package core.graphic;

import org.lwjgl.opengl.GL30;

import core.scene.Scene;

public class SceneRender {
	private static final String vertexSource = "src/resources/shaders/scene.vert";
	private static final String fragmentSource = "src/resources/shaders/scene.frag";
	
	private ShaderProgram shaderProgram;
	
	public SceneRender() {
		shaderProgram = new ShaderProgram(vertexSource,fragmentSource);
	}
	
	public void cleanup() {
		shaderProgram.cleanup();
	}
	
	public void render(Scene scene) {
		shaderProgram.bind();
		
		scene.getMeshMap().values().forEach(mesh -> {
			GL30.glBindVertexArray(mesh.getVaoID());
			GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, mesh.getNumVertices());
			}
		);
		
		GL30.glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
}
