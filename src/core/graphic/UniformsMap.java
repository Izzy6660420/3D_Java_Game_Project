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
