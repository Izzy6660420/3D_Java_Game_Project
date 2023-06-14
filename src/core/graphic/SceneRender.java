package core.graphic;

import java.util.Collection;
import java.util.List;

import core.scene.Entity;
import core.scene.Scene;

import static org.lwjgl.opengl.GL30.*;

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
		uniformsMap.setUniform("viewMatrix", scene.getCamera().getViewMatrix());
		uniformsMap.setUniform("txtSampler", 0);
		
		Collection<Model> models = scene.getModelMap().values();
		TextureCache textureCache = scene.getTextureCache();
		for(Model model: models) {
			List<Entity> entities = model.getEntitiesList();
			
			for(Material material : model.getMaterialList()) {
				uniformsMap.setUniform("material.diffuse", material.getDiffuseColor());
				Texture texture = textureCache.getTexture(material.getTexturePath());
				glActiveTexture(GL_TEXTURE0);
				texture.bind();
				
				for(Mesh mesh : material.getMeshList()) {
					glBindVertexArray(mesh.getVaoID());
					for(Entity entity : entities) {
						uniformsMap.setUniform("modelMatrix", entity.getModelMatrix());
						glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
					}
				}
			}
		}
		
		glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
	
	private void createUniforms() {
		uniformsMap = new UniformsMap(shaderProgram.getProgramID());
		uniformsMap.createUniform("projectionMatrix");
		uniformsMap.createUniform("modelMatrix");
		uniformsMap.createUniform("viewMatrix");
		uniformsMap.createUniform("txtSampler");
		uniformsMap.createUniform("material.diffuse");
	}
}
