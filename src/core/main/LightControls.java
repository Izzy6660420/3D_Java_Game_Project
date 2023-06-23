package core.main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import core.engine.io.IGuiInstance;
import core.engine.io.MouseInput;
import core.engine.io.Window;
import core.scene.Scene;
import core.scene.lights.*;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;

public class LightControls implements IGuiInstance{
	
	private float[] ambientColor, ambientFactor;
	private float[] dirConeX, dirConeY, dirConeZ;
	private float[] dirLightColor, dirLightIntensity, dirLightX, dirLightY, dirLightZ;
	private float[] pointLightColor, pointLightIntensity, pointLightX, pointLightY, pointLightZ;
	private float[] spotLightColor, spotLightIntensity, spotLightCutoff, spotLightX, spotLightY, spotLightZ;
	
	public LightControls(Scene scene) {
		SceneLights sceneLights = scene.getSceneLights();
		AmbientLight ambientLight = sceneLights.getAmbientLight();
		Vector3f color = ambientLight.getColor();
		
		ambientFactor = new float[] {ambientLight.getIntensity() };
		ambientColor = new float[] {color.x, color.y, color.z};
		
		PointLight pointLight = sceneLights.getPointLights().get(0);
		color = pointLight.getColor();
		Vector3f pos = pointLight.getPosition();
		pointLightColor = new float[] {color.x, color.y, color.z};
		pointLightX = new float[] {pos.x};
		pointLightY = new float[] {pos.y};
		pointLightZ = new float[] {pos.z};
		pointLightIntensity = new float[] {pointLight.getIntensity()};
		
		SpotLight spotLight = sceneLights.getSpotLights().get(0);
		pointLight = spotLight.getPointLight();
		color = pointLight.getColor();
		pos = pointLight.getPosition();
		spotLightColor = new float[] {color.x, color.y, color.z};
		spotLightX = new float[] {pos.x};
		spotLightY = new float[] {pos.y};
		spotLightZ = new float[] {pos.z};
		spotLightIntensity = new float[] {pointLight.getIntensity()};
		spotLightCutoff = new float[] {spotLight.getCutoffAngle()};
		Vector3f coneDir = spotLight.getConeDirection();
		dirConeX = new float[] {coneDir.x};
		dirConeY = new float[] {coneDir.y};
		dirConeZ = new float[] {coneDir.z};
		
		DirectionalLight dirLight = sceneLights.getDirLight();
		color = dirLight.getColor();
		pos = dirLight.getDirection();
		dirLightColor = new float[] {color.x, color.y, color.z};
		dirLightX = new float[] {pos.x};
		dirLightY = new float[] {pos.y};
		dirLightZ = new float[] {pos.z};
		dirLightIntensity = new float[] {dirLight.getIntensity()};
		
	}

	@Override
	public void drawGUI() {
		ImGui.newFrame();
		ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
		ImGui.setNextWindowSize(450, 400);
		
		ImGui.begin("Lights Controls");
		if(ImGui.collapsingHeader("Ambient Light")) {
			ImGui.sliderFloat("Ambient Factor", ambientFactor, 0.0f, 1.0f, "%.2f");
			ImGui.colorEdit3("Ambient Color", ambientColor);
		}
		
		if(ImGui.collapsingHeader("Point Light")) {
			ImGui.sliderFloat("Point Light - x", pointLightX, -10.0f, 10.0f, "%.2f");
			ImGui.sliderFloat("Point Light - y", pointLightY, -10.0f, 10.0f, "%.2f");
			ImGui.sliderFloat("Point Light - z", pointLightZ, -10.0f, 10.0f, "%.2f");
			ImGui.colorEdit3("Point Light Color", pointLightColor);
			ImGui.sliderFloat("Point Light Intensity", pointLightIntensity, 0.0f, 1.0f, "%.2f");
		}
		
		if(ImGui.collapsingHeader("Spot Light")) {
			ImGui.sliderFloat("Spot Light - x", spotLightX, -10.0f, 10.0f, "%.2f");
			ImGui.sliderFloat("Spot Light - y", spotLightY, -10.0f, 10.0f, "%.2f");
			ImGui.sliderFloat("Spot Light - z", spotLightZ, -10.0f, 10.0f, "%.2f");
			ImGui.colorEdit3("Spot Light Color", spotLightColor);
			ImGui.sliderFloat("Spot Light Intensity", spotLightIntensity, 0.0f, 1.0f, "%.2f");
			ImGui.sliderFloat("Spot Light Cutoff", spotLightCutoff, 0.0f, 360.0f, "%.2f");
			ImGui.sliderFloat("Dir Cone - x", dirConeX, -1.0f, 1.0f, "%.2f");
			ImGui.sliderFloat("Dir Cone - y", dirConeY, -1.0f, 1.0f, "%.2f");
			ImGui.sliderFloat("Dir Cone - z", dirConeZ, -1.0f, 1.0f, "%.2f");
		}
		
		if(ImGui.collapsingHeader("Directional Light")) {
			ImGui.sliderFloat("Directional Light - x", dirLightX, -1.0f, 1.0f, "%.2f");
			ImGui.sliderFloat("Directional Light - y", dirLightY, -1.0f, 1.0f, "%.2f");
			ImGui.sliderFloat("Directional Light - z", dirLightZ, -1.0f, 1.0f, "%.2f");
			ImGui.colorEdit3("Directional Light Color", dirLightColor);
			ImGui.sliderFloat("Directional Light Intensity", dirLightIntensity, 0.0f, 1.0f, "%.2f");
		}
		
		ImGui.end();
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
		
		boolean consumed = imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
		if(consumed) {
			SceneLights sceneLights = scene.getSceneLights();
			AmbientLight ambientLight = sceneLights.getAmbientLight();
			ambientLight.setIntensity(ambientFactor[0]);
			ambientLight.setColor(ambientColor[0], ambientColor[1], ambientColor[2]);
			
			PointLight pointLight = sceneLights.getPointLights().get(0);
			pointLight.setPosition(pointLightX[0], pointLightY[0], pointLightZ[0]);
			pointLight.setColor(pointLightColor[0], pointLightColor[1], pointLightColor[2]);
			pointLight.setIntensity(pointLightIntensity[0]);
			
			SpotLight spotLight = sceneLights.getSpotLights().get(0);
			pointLight = spotLight.getPointLight();
			pointLight.setPosition(spotLightX[0], spotLightY[0], spotLightZ[0]);
			pointLight.setColor(spotLightColor[0], spotLightColor[1], spotLightColor[2]);
			pointLight.setIntensity(spotLightIntensity[0]);
			spotLight.setCutoffAngle(spotLightCutoff[0]);
			spotLight.setConeDirection(dirConeX[0], dirConeY[0], dirConeZ[0]);
			
			DirectionalLight dirLight = sceneLights.getDirLight();
			dirLight.setDirection(dirLightX[0], dirLightY[0], dirLightZ[0]);
			dirLight.setColor(dirLightColor[0], dirLightColor[1], dirLightColor[2]);
			dirLight.setIntensity(dirLightIntensity[0]);
		}
		return consumed;
	}
	
}
