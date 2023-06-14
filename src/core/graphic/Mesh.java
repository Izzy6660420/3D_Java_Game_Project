package core.graphic;

import java.util.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	
	private int numVertices;
	private int vaoID;
	private List<Integer> vboIDList;
	
	public Mesh(float[] positions, float[] texCoords, int[] indices) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			this.numVertices = indices.length;
			vboIDList = new ArrayList<>();
			
			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);
			
			//positions VBO
			int vboID = glGenBuffers();
			vboIDList.add(vboID);
			FloatBuffer positionsBuffer = storeDataInFloatBuffer(positions);
			glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID);
			glBufferData(GL30.GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//color VBO
			vboID = glGenBuffers();
			vboIDList.add(vboID);
			FloatBuffer texCoordsBuffer = storeDataInFloatBuffer(texCoords);
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			//index VBO
			vboID = glGenBuffers();
			vboIDList.add(vboID);
			IntBuffer indicesBuffer = storeDataInIntBuffer(indices);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		}
	}
	
	public void cleanup() {
        vboIDList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoID);
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
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
