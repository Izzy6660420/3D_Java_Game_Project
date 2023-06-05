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
