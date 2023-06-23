package core.graphic;

import java.util.*;

import org.joml.Vector4f;

public class Material {
	
	public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
	
	private Vector4f ambientColor, diffuseColor, specularColor;
	private float reflectance;

	private List<Mesh> meshList;
	private String texturePath;
	
	public Material() {
		ambientColor = DEFAULT_COLOR;
		diffuseColor = DEFAULT_COLOR;
		meshList = new ArrayList<>();
	}
	
	public void cleanup() {
		meshList.stream().forEach(Mesh::cleanup);
	}

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}

	public List<Mesh> getMeshList() {
		return meshList;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}
	
	
	
}
