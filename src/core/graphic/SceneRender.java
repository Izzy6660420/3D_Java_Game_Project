package core.graphic;

import java.util.Collection;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector3f;

import core.scene.Entity;
import core.scene.Scene;
import core.scene.lights.AmbientLight;
import core.scene.lights.DirectionalLight;
import core.scene.lights.PointLight;
import core.scene.lights.SpotLight;
import core.scene.lights.SceneLights;

import static org.lwjgl.opengl.GL30.*;

public class SceneRender {
	private static final int MAX_POINT_LIGHTS = 5;
	private static final int MAX_SPOT_LIGHTS = 5;
	
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
		
		updateLights(scene);
		
		Collection<Model> models = scene.getModelMap().values();
		TextureCache textureCache = scene.getTextureCache();
		for(Model model: models) {
			List<Entity> entities = model.getEntitiesList();
			
			for(Material material : model.getMaterialList()) {
				uniformsMap.setUniform("material.ambient", material.getAmbientColor());
                uniformsMap.setUniform("material.diffuse", material.getDiffuseColor());
                uniformsMap.setUniform("material.specular", material.getSpecularColor());
                uniformsMap.setUniform("material.reflectance", material.getReflectance());
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
	
	private void updateLights(Scene scene) {
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		
		SceneLights sceneLights = scene.getSceneLights();
		AmbientLight ambientLight = sceneLights.getAmbientLight();
		uniformsMap.setUniform("ambientLight.factor", ambientLight.getIntensity());
		uniformsMap.setUniform("ambientLight.color", ambientLight.getColor());
		
		DirectionalLight dirLight = sceneLights.getDirLight();
		Vector4f auxDir = new Vector4f(dirLight.getDirection(), 0);
		auxDir.mul(viewMatrix);
		Vector3f dir = new Vector3f(auxDir.x, auxDir.y, auxDir.z);
		uniformsMap.setUniform("dirLight.color", dirLight.getColor());
		uniformsMap.setUniform("dirLight.direction", dir);
		uniformsMap.setUniform("dirLight.intensity", dirLight.getIntensity());

		List<PointLight> pointLights = sceneLights.getPointLights();
		int numPointLights = pointLights.size();
		PointLight pointLight;
		for(int i = 0; i < MAX_POINT_LIGHTS; i++) {
			if(i < numPointLights) pointLight = pointLights.get(i);
			else pointLight = null;
			
			String name = "pointLights[" + i + "]";
			updatePointLight(pointLight, name, viewMatrix);
		}
		
		List<SpotLight> spotLights = sceneLights.getSpotLights();
		int numSpotLights = spotLights.size();
		SpotLight spotLight;
		for(int i = 0; i < MAX_SPOT_LIGHTS; i++) {
			if(i < numSpotLights) spotLight = spotLights.get(i);
			else spotLight = null;
			
			String name = "spotLights[" + i + "]";
			updateSpotLight(spotLight, name, viewMatrix);
		}

	}
	
	private void updatePointLight(PointLight pL, String prefix, Matrix4f viewMatrix) {
		Vector4f aux = new Vector4f();
		Vector3f lightPosition = new Vector3f();
		Vector3f color = new Vector3f();
		float intensity = 0.0f;
		float constant = 0.0f;
		float linear = 0.0f;
		float exponent = 0.0f;
		if(pL != null) {
			aux.set(pL.getPosition(), 1);
			aux.mul(viewMatrix);
			lightPosition.set(aux.x, aux.y, aux.z);
			color.set(pL.getColor());
			intensity = pL.getIntensity();
			PointLight.Attenuation attenuation = pL.getAttenuation();
			constant = attenuation.getConstant();
			linear = attenuation.getLinear();
			exponent = attenuation.getExponent();
		}
		
		uniformsMap.setUniform(prefix + ".position", lightPosition);
		uniformsMap.setUniform(prefix + ".color", color);
		uniformsMap.setUniform(prefix + ".intensity", intensity);
		uniformsMap.setUniform(prefix + ".att.constant", constant);
		uniformsMap.setUniform(prefix + ".att.linear", linear);
		uniformsMap.setUniform(prefix + ".att.exponent", exponent);
	}
	
	private void updateSpotLight(SpotLight sL, String prefix, Matrix4f viewMatrix) {
		PointLight pointLight = null;
		Vector3f coneDirection = new Vector3f();
		float cutoff = 0.0f;
		if(sL != null) {
			coneDirection = sL.getConeDirection();
			cutoff = sL.getCutOff();
			pointLight = sL.getPointLight();
		}
		
		uniformsMap.setUniform(prefix + ".conedir", coneDirection);
		uniformsMap.setUniform(prefix + ".cutoff", cutoff);
		updatePointLight(pointLight, prefix + ".pl", viewMatrix);

	}
	
	private void createUniforms() {
		uniformsMap = new UniformsMap(shaderProgram.getProgramID());
		uniformsMap.createUniform("projectionMatrix");
		uniformsMap.createUniform("modelMatrix");
		uniformsMap.createUniform("viewMatrix");
		uniformsMap.createUniform("txtSampler");
		uniformsMap.createUniform("material.diffuse");
		uniformsMap.createUniform("material.ambient");
		uniformsMap.createUniform("material.diffuse");
		uniformsMap.createUniform("material.specular");
		uniformsMap.createUniform("material.reflectance");
		uniformsMap.createUniform("ambientLight.factor");
		uniformsMap.createUniform("ambientLight.color");

		for(int i = 0; i < MAX_POINT_LIGHTS; i++) {
			String name = "pointLights[" + i + "]";
			uniformsMap.createUniform(name + ".position");
			uniformsMap.createUniform(name + ".color");
			uniformsMap.createUniform(name + ".intensity");
			uniformsMap.createUniform(name + ".att.constant");
			uniformsMap.createUniform(name + ".att.linear");
			uniformsMap.createUniform(name + ".att.exponent");
		}
		
		for(int i = 0; i < MAX_SPOT_LIGHTS; i++) {
			String name = "spotLights[" + i + "]";
			uniformsMap.createUniform(name + ".pl.position");
			uniformsMap.createUniform(name + ".pl.color");
			uniformsMap.createUniform(name + ".pl.intensity");
			uniformsMap.createUniform(name + ".pl.att.constant");
			uniformsMap.createUniform(name + ".pl.att.linear");
			uniformsMap.createUniform(name + ".pl.att.exponent");
			uniformsMap.createUniform(name + ".conedir");
			uniformsMap.createUniform(name + ".cutoff");
		}
		
		uniformsMap.createUniform("dirLight.color");
		uniformsMap.createUniform("dirLight.direction");
		uniformsMap.createUniform("dirLight.intensity");

	}
}
