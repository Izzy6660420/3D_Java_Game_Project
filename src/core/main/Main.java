package core.main;

import core.engine.io.Engine;
import core.engine.io.IAppLogic;
import core.engine.io.Window;
import core.graphic.*;
import core.scene.Scene;

public class Main implements IAppLogic{
	public Thread game;
	public Window window;
	public final int WIDTH = 1280, HEIGHT = 760;

	
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
		float[] positions = new float[] {
			 0.0f, 0.5f, 0.0f,
             -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f
		};
		
		Mesh mesh = new Mesh(positions, 3);
		scene.addMesh("Triangle", mesh);
		
	}

	@Override
	public void input(Window window, Scene scene, long diffTimeMillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Window window, Scene scene, long diffTimeMillis) {
		// TODO Auto-generated method stub
		
	}

}
