package core.main;

import core.engine.io.Engine;
import core.engine.io.IAppLogic;
import core.engine.io.IGuiInstance;
import core.engine.io.MouseInput;
import core.engine.io.Window;
import core.graphic.*;
import core.scene.Camera;
import core.scene.Entity;
import core.scene.ModelLoader;
import core.scene.Scene;
import core.scene.lights.*;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;

import java.util.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic{
	
	private static final float MOUSE_SENSITIVITY = 0.1f;
	private static final float MOVEMENT_SPEED = 0.005f;
	
	public Thread game;
	public Window window;
	public final int WIDTH = 2560, HEIGHT = 1440;
	
	private Entity cubeEntity;
	private Vector4f displInc = new Vector4f();
	private float rotation;
	
	private LightControls lightControls;
	
	public static void main(String[] args) {
		Main main = new Main();
		Engine gameEng = new Engine("3D Java Game Project", new Window.WindowOptions(), main);
		gameEng.start();
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Window window, Scene scene, Render render) {
		Model cubeModel = ModelLoader.loadModel("cube-model", "src/resources/models/cube/cube.obj", 
					scene.getTextureCache());
		scene.addModel(cubeModel);
		
		cubeEntity = new Entity("cube-entity", cubeModel.getId());
		cubeEntity.setPosition(0, 0, -2);
		scene.addEntity(cubeEntity);
		
		SceneLights sceneLights = new SceneLights();
		sceneLights.getAmbientLight().setIntensity(0.3f);
		scene.setSceneLights(sceneLights);
		sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
				new Vector3f(0, 0, -1.4f), 1.0f));
		
		Vector3f coneDir = new Vector3f(0, 0, -1);
		sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(1, 1, 1),
				new Vector3f(0, 0, -1.4f), 0.0f), coneDir, 140.0f));
		
		lightControls = new LightControls(scene);
		scene.setGuiInstance(lightControls);
		
	}
	
	@Override
	public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
		if(inputConsumed) return;
		
		float move = diffTimeMillis * MOVEMENT_SPEED;
		Camera camera = scene.getCamera();
		
		if(window.isKeyPressed(GLFW_KEY_W)) {
			camera.moveFoward(move);
		} else if(window.isKeyPressed(GLFW_KEY_S)) {
			camera.moveBackwards(move);
		}
		
		if(window.isKeyPressed(GLFW_KEY_A)) {
			camera.moveLeft(move);
		} else if(window.isKeyPressed(GLFW_KEY_D)) {
			camera.moveRight(move);
		}
		
		if (window.isKeyPressed(GLFW_KEY_E)) {
            camera.moveDown(move);
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            camera.moveUp(move);
        }
		
		MouseInput mouseInput = window.getMouseInput();
		if(mouseInput.isRightButtonPressed()) {
			Vector2f displVec = mouseInput.getDisplVec();
			camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
		}
		
	}

	@Override
	public void update(Window window, Scene scene, long diffTimeMillis) {
		rotation += 1.5;
		if(rotation > 360) rotation = 0;
		
		cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
		cubeEntity.updateModelMatrix();
	}

	/*
	 * This block of code is to showcase the demo window that comes with implementing ImGUI and is
	 * utilized during Chapter 10 to ensure all the code is written appropriately and the program
	 * runs without any issues.
	 * 
		@Override
		public void drawGUI() {
			ImGui.newFrame();
			ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
			ImGui.showDemoWindow();
			ImGui.endFrame();
			ImGui.render();
			
		}

		@Override
		public boolean handleGuiInput(Scene scene, Window window) {
			ImGuiIO imGuiIO = ImGui.getIO();
			MouseInput mouseInput = window.getMouseInput();
			Vector2f mousePos = mouseInput.getCurrentPos();
			imGuiIO.setMousePos(mousePos.x, mousePos.y);
			imGuiIO.setMouseDown(0, mouseInput.isLeftButtonPressed());
			imGuiIO.setMouseDown(1, mouseInput.isRightButtonPressed());
			
			return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
		}
	*/
}
