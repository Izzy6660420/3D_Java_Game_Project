package core.graphic;

import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class UniformsMap {
	
	private int programID;
	private Map<String, Integer> uniforms;
	
	public UniformsMap(int programID) {
		this.programID = programID;
		uniforms = new HashMap<>();
	}
	
	public void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programID, uniformName);
		
		if(uniformLocation < 0) throw new RuntimeException("Could not find uniform [" + uniformName + "] in shader program [" + programID + "]");
		
		uniforms.put(uniformName, uniformLocation);
	}
	
	private int getUniformLocation(String uniformName) {
		Integer location = uniforms.get(uniformName);
		
		if(location == null) throw new RuntimeException("Could not find uniform [" + uniformName + "]");
		
		return location.intValue();
	}
	
	public void setUniform(String uniformName, int value) {
		glUniform1i(getUniformLocation(uniformName), value);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			glUniformMatrix4fv(getUniformLocation(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}
	
	public void setUniform(String uniformName, Vector4f value) {
		glUniform4f(getUniformLocation(uniformName), value.x, value.y, value.z, value.w);
	}
}
