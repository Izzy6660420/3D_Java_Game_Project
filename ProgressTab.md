# 3D Java Game Project Progress Report

## Day 1
### Setting up the project
- Imported LWJGL3 library in order to kickstart the project off.
- The project is created and ran within Eclipse IDE environment, and for consistency sake, will be executed in 1.8 Java Environment.
- Using **Threads** to run and execute the main functions for the game.
    - **Threads** allows a program to operate more efficiently by doing multiple things at the same time, basically running different tasks on different threads allows these tasks to be ran concurrently along with the main program without interrupting the main thread and slowing down the program.

### Setting up Window.java and Input.java classes
- Setup the `Window.java` class so that the main program can set up a window that can pop up on the screen, although there is an error where the window is non-responsive and can't be closed unless the program is forcefully terminated.
    - The solution to this problem is to just ensure that within the `run()` method that the loop is checking whether the window should close or not with the built-in `.shouldClose()` method.
- The window created by `Window.java` would also not be rendering anything.
    - This is caused by not having any specifications within the class to actually render graphics, a fix is to just call `GLFW.glfwSwapBuffers(window)` so that the program actually knows to render graphics within the window.
- Setup `Input.java`, this class processes all the possible user inputs, mouseButtons, keyboardKeys and mousePositions in order to allow for the main program to accept user inputs and processes them through methods setup within the class itself.

### Finalizing Window.java and Input.java classes
- Implemented additional feature within the `Window.java` class so that it is resizable, by using `GLFWWindowSizeCallback()`, we can ensure that the windows are properly resizable and everything rendered within should resize accordingly. And by implementing resizability, we can also make fullscreen capability for the window.
- Also enabled `GL11.GL_DEPTH_TEST` in preparation for future 3D capability.
- Implemented mouse scrolling check so that mouse scrolls is a function in the program, through the inclusion of `GLFWScrollCallback()`

## Day 2
### Revamping the project
- Although the project started off on a great note, unfortunately the [CodingAP](https://www.youtube.com/@CodingAP) channel has discontinued their LWJGL3 tutorial and it's proven difficult to follow a none future-proof tutorial playlist. 
    - Therefore to remedy the situation, all codes are now written through a hybrid style of following both [ThinMatrix](https://www.youtube.com/@ThinMatrix/featured) and [3D Game Development with LWJGL3 by Antonio Hernández Bejarano](https://github.com/lwjglgamedev/lwjglbook-bookcontents).

### Rendering, Shaders and Meshes
- Following Antonio's guide, and introducing small reworks of Antonio's implementation throughout the project, utilizing ThinMatrix's codes, I am able to render a basic yellow triangle onto the window opened on our screen. Throughout today's journey, I was able to learn a plethora of interesting points and new things to add to my arsenal.
- Within OpenGL 2.0, we are introduced a programmable graphics pipelines, in which our program follows in order to render anything onto the screen.

![Rendering Pipeline](./markdownFilePictures/renderingPipeline.png)

```
The rendering starts taking as its input a list of vertices in the form of Vertex Buffers. But, what is a vertex? A vertex is any data structure that can be used as an input to render a scene. By now you can think as a structure that describes a point in 2D or 3D space. And how do you describe a point in a 3D space? By specifying its x, y and z coordinates. And what is a Vertex Buffer? A Vertex Buffer is another data structure that packs all the vertices that need to be rendered, by using vertex arrays, and makes that information available to the shaders in the graphics pipeline.

Those vertices are processed by the vertex shader whose main purpose is to calculate the projected position of each vertex into the screen space. This shader can generate also other outputs related to color or texture, but its main goal is to project the vertices into the screen space, that is, to generate dots. 

The geometry processing stage connects the vertices that are transformed by the vertex shader to form triangles. It does so by taking into consideration the order in which the vertices were stored and grouping them using different models. Why triangles? A triangle is like the basic work unit for graphic cards. It’s a simple geometric shape that can be combined and transformed to construct complex 3D scenes. This stage can also use a specific shader to group the vertices.

The rasterization stage takes the triangles generated in the previous stages, clips them and transforms them into pixel-sized fragments. Those fragments are used during the fragment processing stage by the fragment shader to generate pixels assigning them the final color that gets written into the framebuffer. The framebuffer is the final result of the graphics pipeline. It holds the value of each pixel that should be drawn to the screen.
```

- Within Antonio's guide, he utilizes a library import **(ShaderModuleData)**, which I have no access over as it requires an execution environment I failed to import(due to following a different guide early on, CodingAP required a JavaSE-1.8, whereas Antonio required JavaSE-17 or beyond). This became a problem as I couldn't identify early on what the purpose of this library was, as I was about to give up, I decided instead of just blindly following Antonio's guide step-by-step, I would just read the entire chapter through and see what the **ShaderModuleData** object was being used for. I quickly realize that the object was used to read in fragment and vertex shaders data from file and to store each of these read data to its respective GL20 type integers.
    - Identifying this similarity, I quickly opened one of [ThinMatrix videos on "Coloring using Shaders"](https://youtu.be/4w7lNF8dnYw) and immediately found a solution to this. His code is extremely similar to Antonio's in handling their shaders, the only difference is that Antonio is using a pre-existing library available to him. Therefore after identifying this, I quickly got to work on implementing and reading the shader files, and was finally able to replicate ThinMatrix's shader code and incorporate them into Antonio's shader codes.

- Within Antonio's guide and codes, instead of a RawModel, as named and identified by ThinMatrix in his video, [VAOs and VBOs](https://youtu.be/WMiggUPst-Q), Antonio named them as [Meshes](https://en.wikipedia.org/wiki/Polygon_mesh), which is a more familiar terminology since I have worked with meshes quite a lot when I was doing Unity/UE. The utilisation of the terminology meshes allowed me to identify very quickly that we are working with VAOs and VBOs, and that I will be able to cross-reference Antonio's guide to ThinMatrix's video, which allowed me to amalgamate their codes into what currently is within the `Mesh.java` class.

- Another problem that was present when I was trying to replicate Antonio's code within my project, is that for the line:
    ```java
    FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
    positionsBuffer.put(0, positions);
    ```
    The compiler was having trouble attempting to compile the 2nd line as for some reason whenever `positionsBuffer.put(0,positions)` is resolved, the compiler keeps throwing out complaints that it does not take in a float array as a 2nd input. Reading through the OracleDoc for `FloatBuffer`, there definitely do not exist a `put()` method for FloatBuffer that takes in both an integer and a float array input, therefore to remedy this problem, I replicated ThinMatrix's code on how he loads `RawModel(s)` onto his scene and stores data in float buffer and the problem was resolved immediately.


```java
package core.graphic;

import java.util.*;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class Mesh {
	
	private int numVertices;
	private int vaoID;
	private List<Integer> vboIDList;
	
	public Mesh(float[] positions, int numVertices) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			this.numVertices = numVertices;
			vboIDList = new ArrayList();
			
			vaoID = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoID);
			
			//positions VBO
			int vboID = GL30.glGenBuffers();
			vboIDList.add(vboID);
			FloatBuffer positionsBuffer = storeDataInFloatBuffer(positions);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, positionsBuffer, GL30.GL_STATIC_DRAW);
			GL30.glEnableVertexAttribArray(0);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		}
	}
	
	public void cleanup() {
        vboIDList.forEach(GL30::glDeleteBuffers);
        GL30.glDeleteVertexArrays(vaoID);
	}

	public int getNumVertices() {
		return numVertices;
	}

	public final int getVaoID() {
		return vaoID;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
```

- After the meshes, the only thing remaining is to render the meshes onto a `Scene` object and have a `SceneRender` object to render, and then include the `SceneRender` object within the `Render` class.

## Day 3
### Extension on Mesh rendering, specifically rendering a quad

Within this section, this is to extend upon the concept of rendering meshes. For my last endaevour, I manage to follow guides and understood how to easily render a basic triangle by just specifying vertices and their positiions, and that extends to rendering a quad, as a quad is basically just 2 triangles combined. And can be easily rendered if we just specified specific positions where the triangles needs to be drawn.

```java
float[] positions = new float[] {
    -0.5f,  0.5f, 0.0f,
    -0.5f, -0.5f, 0.0f,
     0.5f,  0.5f, 0.0f,
     0.5f,  0.5f, 0.0f,
    -0.5f, -0.5f, 0.0f,
     0.5f, -0.5f, 0.0f,
}
```

The problem with this approach however is that we are repetitively recalling the same positions again in order to specify a specific vertex in order to draw two triangles, this is less of a problem with a simple shape such as our quad, but when we move it to a 3D object that will most likely be composed of more than 2 triangles, we could end up requiring more memories and code-type repetition in order to render a single object.

**That is inefficient!**

This is where the concept of index buffers are introduced following Antonio's and/or TM's guide. Utilizing index buffers, we can put an index to our vertices, or "positions", and then all we have to do is reference the order in which these vertices need to be drawn by referring to the index of their positions.

| V1 | V2 | V3 | V4 |
| --- | --- | --- | --- |
| 0 | 1 | 2 | 3 |

| 0 | 1 | 3 | 3 | 1 | 2 |
| --- | --- | --- | --- | --- | --- |
| V1 | V2 | V4 | V4 | V2 | V3 |

Therefore our first step within this section of my progress tab is to modify the `Mesh` class such that it accepts color and indices arrays, and create vbos for each of these parameters like we do for our vertices. The VAO will now contain three VBOs, one for positions, colors and indices respectively.

And then within the `SceneRender` class, as we aren't utilizing just an array of positions float buffer, we have to refactor the drawing call from `glDrawArrays` to `glDrawElements`. And then minor modifications within the vertex and fragment shaders to also accept color inputs and we have ourselves the simplest colored quad mesh.

![ColoredQuad](/3D_Java_Game_Project/markdownFilePictures/renderedColoredQuad.png)

### Introducing the 3D aspect of this project
- Perspective and uniforms
	- Currently, although our program understand the coordinate systems and have the ability to render quads, if we tweak the z-coordinate of our quads, it will still remain the same size. This is because OpenGL does not have the concept of sizes. Therefore to correct this, we need to introduce the ability to project our coordinates, and we can amend that problem by introducing a perspective projection matrix to our program so that it can handle the aspect ratio of our drawing area to prevent distortion of our object while also be able to handle the concept of distance and size of our objects.

	```java
	package core.scene;

	import org.joml.Matrix4f;

	public class Projection {

		private static final float FOV = (float) Math.toRadians(60.0f);
		private static final float Z_FAR = 1000.0f;
		private static final float Z_NEAR = 0.01f;
		
		private Matrix4f projMatrix;
		
		public Projection(int width, int height) {
			projMatrix = new Matrix4f();
			updateProjMatrix(width, height);
		}

		public Matrix4f getProjMatrix() {
			return projMatrix;
		}
		
		public void updateProjMatrix(int width, int height) {
			projMatrix.setPerspective(FOV, (float) width/height, Z_NEAR, Z_FAR);
		}
	}
	```
	This class takes care of the idea of a projection matrix, and we can just implement it as an object to our `Scene` class, and now our program contains the concept of perspective.

	- Even though we have the infrastructure to calculate a projection matrix, this is still required to be implemented for our shaders.

	```
	We need to use it in our shader, and it should be applied to all the vertices. At first, you could think of bundling it in the vertex input (like the coordinates and the colors). In this case we would be wasting lots of space since the projection matrix is common to any vertex. You may also think of multiplying the vertices by the matrix in the java code. But then, our VBOs would be useless and we will not be using the process power available in the graphics card.

	The answer is to use “uniforms”. Uniforms are global GLSL variables that shaders can use and that we will employ to pass data that is common to all elements or to a model. So, let's start with how uniforms are used in shader programs. We need to modify our vertex shader code and declare a new uniform called projectionMatrix and use it to calculate the projected position.
	```

	Therefore we need to introduce our projectionMatrix that we have created within our java program into the shaders code in order for the shader program to actually be able to calculate the distance for each pixels in accordance to our projectionMatrix, through the utilisation of the power of our graphics card. But if we are taking uniforms within our shader codes, the concept of the projectionMatrix and its values will also have to be introduced within our java code through unimplemented means, which is why I followed Antonio's guide and created a class that will help setup that uniform variable for our shader code.

	```java
	package core.graphic;

	import java.util.Map;

	import org.joml.Matrix4f;
	import org.lwjgl.opengl.GL20;
	import org.lwjgl.system.MemoryStack;

	import java.util.HashMap;

	public class UniformsMap {
		
		private int programID;
		private Map<String, Integer> uniforms;
		
		public UniformsMap(int programID) {
			this.programID = programID;
			uniforms = new HashMap<>();
		}
		
		public void createUniform(String uniformName) {
			int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
			
			if(uniformLocation < 0) throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" + programID + "]");
			
			uniforms.put(uniformName, uniformLocation);
		}
		
		public void setUniform(String uniformName, Matrix4f value) {
			try(MemoryStack stack = MemoryStack.stackPush()) {
				Integer location = uniforms.get(uniformName);
				if(location == null) throw new RuntimeException("Could not find uniform [" + uniformName + "]");
				GL20.glUniformMatrix4fv(location.intValue(), false, value.get(stack.mallocFloat(16)));
			}
		}
	}
	```

	Now with this new class, we can easily create and introduce a new uniform whenever we require one, and for now we will have `projectionMatrix` for our first uniform. After this part of the day, if we tweak the z-value of the coordinate within our `positions` array, we can now see a stark difference as the quad will now be able to zoom in and out.

- Implementation of `Model` and `Entity`
	- To quote Antonio,
		```
		Let us first define the concept of a 3D model. Up to now, we have been working with meshes (a collection of vertices). A model is an structure which glues together vertices, colors, textures and materials. A model may be composed of several meshes and can be used by several game entities. A game entity represents a player and enemy, and obstacle, anything that is part of the 3D scene. In this book we will assume that an entity always is related to a model (although you can have entities that are not rendered and therefore cannot have a model). An entity has specific data such a position, which we need to use when rendering. You will see later on that we start the render process by getting the models and then draw the entities associated to that model. This is because efficiency, since several entities can share the same model is better to set up the elements that belong to the model once and later on handle the data that is specific for each entity.
		```
		Therefore, instead of just the simple and most fundamental concept of a `Mesh`, we now move on to a `Model`, which is an instance of a list of `Mesh` instances, with its own unique identifier. 
		
	- Within a 3D, or even a 2D space, models or objects have a concept of transformations, which means they can **Translate**(Move back and forth, left and right, up and down), **Rotate**(Literaly, just rotating the object) and **Scale**(Bigger or smaller), and in order to introduce this concept to our program, we have to introduce a new matrix, the `modelMatrix`, which is basically `translationMatrix * rotationMatrix * scaleMatrix`, in their correct, respective order, as matrix multiplication is not commutative. Therefore we introduce this concept into our `Entity` class and ensure that the `modelMatrix` is updated accordingly for this class.
	
		- To note, the modelMatrix concept is introduced within the `Entity` class rather than the `Model` class is because the `Model` class is to simply allow the program to understand the concept, parameters and data of imported 3D models in its own Model Space, therefore it is not required to understand its coordinates, rotation nor size within the world.

			The `Entity` class however, is a class that's created to pertain as actual objects within a 2D/3D game-world spaces, therefore this is where the program, regardless of what its model is, is required to begin to understand its existence, and therefore its coordinates, size and rotation within an actual world space, which is why the modelMatrix is calculated here.

	- After all of the above, all we have to do is add and change codes within `Scene`, `SceneRender`, `Main` and the vertex shader to accommodate for the introduction of the concept of an actual 3D model and 3D space. Within the `Main` class, we have also included the ability for the user to control the application, allowing them to move the object, which in the case of this part of our progress, is a multicolored, rotating cubic object.

