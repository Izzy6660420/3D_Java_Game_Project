package core.scene.lights;

import java.util.*;

import org.joml.Vector3f;

public class SceneLights {
	
	private AmbientLight ambientLight;
	private DirectionalLight dirLight;
	private List<PointLight> pointLights;
	private List<SpotLight> spotLights;
	
	public SceneLights() {
		this.ambientLight = new AmbientLight();
		this.dirLight = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(0, 1, 0), 1.0f);
		this.pointLights = new ArrayList<>();
		this.spotLights = new ArrayList<>();
	}

	public AmbientLight getAmbientLight() {
		return ambientLight;
	}

	public DirectionalLight getDirLight() {
		return dirLight;
	}

	public List<PointLight> getPointLights() {
		return pointLights;
	}

	public List<SpotLight> getSpotLights() {
		return spotLights;
	}

	public void setSpotLights(List<SpotLight> spotLights) {
		this.spotLights = spotLights;
	}

}
