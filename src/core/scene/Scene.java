package core.scene;

import java.util.Map;

import core.graphic.Mesh;

import java.util.HashMap;

public class Scene {
	
	private Map<String, Mesh> meshMap;
	
	public Scene() {
		meshMap = new HashMap<>();
	}
	
	public void addMesh(String meshID, Mesh mesh) {
		meshMap.put(meshID, mesh);
	}
	
	public void cleanup() {

	}

	public Map<String, Mesh> getMeshMap() {
		return meshMap;
	}

}
