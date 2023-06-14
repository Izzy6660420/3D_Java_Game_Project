package core.graphic;

import java.util.List;

import core.scene.Entity;

import java.util.ArrayList;

public class Model {
	
	private final String id;
	private List<Entity> entitiesList;
	private List<Material> materialList;
	
	public Model(String id, List<Material> materialList) {
		this.id = id;
		this.materialList = materialList;
		entitiesList = new ArrayList<>();
	}
	
	public void cleanup() {
		materialList.stream().forEach(Material::cleanup);
	}
	
	public List<Entity> getEntitiesList() {
		return entitiesList;
	}

	public List<Material> getMaterialList() {
		return materialList;
	}
	
	public String getId() {
		return id;
	}
	
	
}
