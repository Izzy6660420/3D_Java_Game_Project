package core.graphic;

import org.lwjgl.opengl.GL20;

import core.engine.io.Utils;

public class ShaderProgram {
	
	private final int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		programID = GL20.glCreateProgram();
		if(programID == 0) throw new RuntimeException("Could not create Shader");
		
		vertexShaderID = loadShader(Utils.readFile(vertexFile), GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(Utils.readFile(fragmentFile), GL20.GL_FRAGMENT_SHADER);
		
		link();	
		
	}
	
	public void bind() {
		GL20.glUseProgram(programID);
	}
	
	public void cleanup() {
		unbind();
		if(programID != 0) GL20.glDeleteProgram(programID);
	}
	
	protected int loadShader(StringBuilder shaderSource, int type) {
		int shaderID = GL20.glCreateShader(type);
		if(shaderID == 0) throw new RuntimeException("Error creating shader. Type: " + type);
		
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling shader code: " + GL20.glGetShaderInfoLog(shaderID, 1024));
		
		GL20.glAttachShader(programID, shaderID);
		
		return shaderID;
	}
	
	private void link() {
		GL20.glLinkProgram(programID);
		if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) throw new RuntimeException("Error linking shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
		
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDetachShader(programID, vertexShaderID);

	}
	 
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void validate() {
		GL20.glValidateProgram(programID);
		if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) throw new RuntimeException("Error validating Shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
	}

	public int getProgramID() {
		return programID;
	}
	
	
	
}
