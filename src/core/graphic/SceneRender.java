package core.graphic;

import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL30;

import core.scene.Entity;
import core.scene.Scene;

public class SceneRender {
	private static final String vertexSource = "src/resources/shaders/scene.vert";
	private static final String fragmentSource = "src/resources/shaders/scene.frag";
	
	private ShaderProgram shaderProgram;
	private UniformsMap uniformsMap;
	
	public SceneRender() {
		shaderProgram = new ShaderProgram(vertexSource,fragmentSource);
		createUniforms();
	}
	
	public void cleanup() {
		shaderProgram.cleanup();
	}
	
	public void render(Scene scene) {
		shaderProgram.bind();
		
		uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
		
		Collection<Model> models = scene.getModelMap().values();
		for(Model model: models) {
			model.getMeshList().stream().forEach(mesh -> {
				GL30.glBindVertexArray(mesh.getVaoID());
				List<Entity> entities = model.getEntitiesList();
				for(Entity entity: entities) {
					uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
					GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0);
				}
			});
		}
		
		GL30.glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
	
	private void createUniforms() {
		uniformsMap = new UniformsMap(shaderProgram.getProgramID());
		uniformsMap.createUniform("projectionMatrix");
		uniformsMap.createUniform("modelMatrix");
	}
}
