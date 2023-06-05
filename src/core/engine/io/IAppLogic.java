package core.engine.io;

import core.graphic.Render;
import core.scene.Scene;

public interface IAppLogic {
	void cleanup();
	void init(Window window, Scene scene, Render render);
	void input(Window window, Scene scene, long diffTimeMillis);
	void update(Window window, Scene scene, long diffTimeMillis);
}
