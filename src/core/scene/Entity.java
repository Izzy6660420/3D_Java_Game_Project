package core.scene;

import org.joml.*;

public class Entity {
	
	private final String id;
	private final String modelId;
	private Matrix4f modelMatrix;
	private Vector3f position;
	private Quaternionf rotation;
	private float scale;
	
	public Entity(String id, String modelId) {
		this.id = id;
		this.modelId = modelId;
		modelMatrix = new Matrix4f();
		position = new Vector3f();
		rotation = new Quaternionf();
		scale = 1;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z, float angle) {
		this.rotation.fromAxisAngleRad(x, y, z, angle);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getId() {
		return id;
	}

	public String getModelId() {
		return modelId;
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
	
	public void updateModelMatrix() {
		modelMatrix.translationRotateScale(position, rotation, scale);
	}

}
