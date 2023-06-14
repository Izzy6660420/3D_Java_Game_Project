package core.engine.io;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
	private final long windowHandle;
	private int width, height;
	private Callable<Void> resizeFunc;
	private String title;
	
	private MouseInput mouseInput;
	private GLFWKeyCallback keyboard;
	
	public static class WindowOptions {
		public boolean compatibleProfile;
		public int fps;
		public int width, height;
		public int ups = Engine.TARGET_UPS;
	}
	
	public Window(String title, WindowOptions opts, Callable<Void> resizeFunc) {
		this.resizeFunc = resizeFunc;
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW!");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		if(opts.compatibleProfile) GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
		else {
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		}
		
		if(opts.width > 0 && opts.height > 0 ) {
			this.height = opts.height;
			this.width = opts.width;
		} else {
			GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			width = vidMode.width();
			height = vidMode.height();
		}
		
		windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(windowHandle == 0) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		
		GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w,h));
		
		GLFW.glfwSetErrorCallback((int errorCode, long msgPtr) -> 
			System.err.println("Error code [{}]" + errorCode)
		);
		
		GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			keyCallback(key, action);
		});
		
		GLFW.glfwMakeContextCurrent(windowHandle);
		
		if(opts.fps > 0) GLFW.glfwSwapInterval(0);
		else GLFW.glfwSwapInterval(1);
		
		GLFW.glfwShowWindow(windowHandle);
		
		int[] arrWidth = new int[1];
		int[] arrHeight = new int[1];
		GLFW.glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
		width = arrWidth[0];
		height = arrHeight[0];
		
		mouseInput = new MouseInput(windowHandle);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
	public void keyCallback(int key, int action) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(windowHandle, true);
	}
	
	public void cleanup() {
		glfwFreeCallbacks(windowHandle);
		GLFW.glfwDestroyWindow(windowHandle);
		GLFW.glfwTerminate();
		
		GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
		if(callback != null) callback.free();
	}


	public long getWindowHandle() {
		return windowHandle;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}
	
	public MouseInput getMouseInput() {
		return mouseInput;
	}

	public boolean isKeyPressed(int keyCode) {
		return GLFW.glfwGetKey(windowHandle, keyCode) == GLFW.GLFW_PRESS;
	}
	
	public void pollEvents() {
		GLFW.glfwPollEvents();
	}
	
	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(windowHandle);
	}
	
	protected void resized(int width, int height) {
		this.width = width;
		this.height = height;
		
		try {
			resizeFunc.call();
		} catch (Exception e) {
			System.err.println("Error calling resize callback");
			e.printStackTrace();
		}
	}
	
	/*public void create() {
		if(!GLFW.glfwInit()) {
			System.err.println("Error: GLFW wasn't initialized!");
			return;
		}
		
		input = new Input();
		window = GLFW.glfwCreateWindow(width, height, title, isFullscreen? GLFW.glfwGetPrimaryMonitor():0, 0);
		
		if(window == 0) {
			System.err.println("Window wasn't created!");
			return;
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		windowPosX[0] = (videoMode.width()- width)/2;
		windowPosY[0] = (videoMode.height() - height)/2;
		GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		createCallbacks();
		
		GLFW.glfwShowWindow(window);
		
		GLFW.glfwSwapInterval(1);
		time = System.currentTimeMillis();
	}
	
	private void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};
		
		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
		GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
	}
	
	public void update() {
		if(isResized) {
			GL11.glViewport(0, 0, width, height);
			isResized = false;
		}
		
		GL11.glClearColor(backgroundR, backgroundG, backgroundB, 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GLFW.glfwPollEvents();
		
		frames++;
		
		if(System.currentTimeMillis() > time + 1000) {
			GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
			//System.out.println(frames);
			time = System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		input.destroy();
		sizeCallback.free();
		GLFW.glfwWindowShouldClose(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	public void setBackgroundColor(float r, float g, float b) {
		this.backgroundR = r;
		this.backgroundG = g;
		this.backgroundB = b;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public long getWindow() {
		return window;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		this.isResized = true;
		
		if(isFullscreen) {
			GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
		}
	}*/
	
	
	
}
