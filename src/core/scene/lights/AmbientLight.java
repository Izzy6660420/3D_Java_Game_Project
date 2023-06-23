package core.scene.lights;

import org.joml.Vector3f;

public class AmbientLight {
	
	private Vector3f color;
	
	private float intensity;

	public AmbientLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}

	public AmbientLight() {
		this(new Vector3f(1.0f, 1.0f, 1.0f), 1.0f);
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setColor(float r, float g, float b) {
		this.color.set(r, g, b);	
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	
	
}
