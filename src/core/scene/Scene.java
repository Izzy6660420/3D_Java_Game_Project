package core.scene;

import java.util.Map;

import core.graphic.Mesh;
import core.graphic.Model;
import core.graphic.TextureCache;

import java.util.HashMap;

public class Scene {
	
	private Map<String, Model> modelMap;
	private Projection projection;
	private TextureCache textureCache;
	private Camera camera;
	
	public Scene(int width, int height) {
		modelMap = new HashMap<>();
		projection = new Projection(width, height);
		textureCache = new TextureCache();
		camera = new Camera();
	}
	
	public void addEntity(Entity entity) {
		String modelId = entity.getModelId();
		Model model = modelMap.get(modelId);
		if(model == null) throw new RuntimeException("Could not find model [" + modelId + "]");
		model.getEntitiesList().add(entity);
	}
	
	public void addModel(Model model) {
		modelMap.put(model.getId(), model);
	}
	
	public void cleanup() {
		modelMap.values().stream().forEach(Model::cleanup);
	}

	public Map<String, Model> getModelMap() {
		return modelMap;
	}

	public Projection getProjection() {
		return projection;
	}
	
	public void resize(int width, int height) {
		projection.updateProjMatrix(width, height);
	}

	public TextureCache getTextureCache() {
		return textureCache;
	}

	public Camera getCamera() {
		return camera;
	}

}
