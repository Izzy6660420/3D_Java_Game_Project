package core.graphic;

import java.util.List;

import core.scene.Entity;

import java.util.ArrayList;

public class Model {
	
	private final String id;
	private List<Entity> entitiesList;
	private List<Mesh> meshList;
	
	public Model(String id, List<Mesh> meshList) {
		this.id = id;
		this.meshList = meshList;
		entitiesList = new ArrayList<>();
	}
	
	public void cleanup() {
		meshList.stream().forEach(Mesh::cleanup);
	}
	
	public List<Entity> getEntitiesList() {
		return entitiesList;
	}

	public List<Mesh> getMeshList() {
		return meshList;
	}
	
	public String getId() {
		return id;
	}
	
	
}
