package core.scene.lights;

import org.joml.Vector3f;

public class PointLight {
	
	private Attenuation attenuation;
	private Vector3f color, position;
	private float intensity;
	
	public PointLight(Vector3f color, Vector3f position, float intensity) {
		attenuation = new Attenuation(0, 0, 1);
		this.color = color;
		this.position = position;
		this.intensity = intensity;
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
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

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public static class Attenuation {
		private float constant, exponent, linear;

		public Attenuation(float constant, float exponent, float linear) {
			this.constant = constant;
			this.exponent = exponent;
			this.linear = linear;
		}

		public float getConstant() {
			return constant;
		}

		public void setConstant(float constant) {
			this.constant = constant;
		}

		public float getExponent() {
			return exponent;
		}

		public void setExponent(float exponent) {
			this.exponent = exponent;
		}

		public float getLinear() {
			return linear;
		}

		public void setLinear(float linear) {
			this.linear = linear;
		}
		
		
	}
	

}
