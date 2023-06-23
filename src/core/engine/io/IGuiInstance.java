package core.engine.io;

import core.scene.Scene;

public interface IGuiInstance {
	void drawGUI();
	
	boolean handleGuiInput(Scene scene, Window window);

}
