package core.scene.lights;

import org.joml.Vector3f;

public class SpotLight {

	private Vector3f coneDirection;
	private float cutOff, cutoffAngle;
	private PointLight pointLight;
	
	public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutoffAngle) {
		this.coneDirection = coneDirection;
		this.cutoffAngle = cutoffAngle;
		this.pointLight = pointLight;
		setCutoffAngle(cutoffAngle);
	}

	public Vector3f getConeDirection() {
		return coneDirection;
	}

	public void setConeDirection(Vector3f coneDirection) {
		this.coneDirection = coneDirection;
	}
	
	public void setConeDirection(float r, float g, float b) {
		this.coneDirection.set(r, g, b);
	}

	public float getCutOff() {
		return cutOff;
	}

	public float getCutoffAngle() {
		return cutoffAngle;
	}

	public void setCutoffAngle(float cutoffAngle) {
		this.cutoffAngle = cutoffAngle;
		cutOff = (float) Math.cos(Math.toRadians(cutoffAngle));
	}

	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}
	
	
}
