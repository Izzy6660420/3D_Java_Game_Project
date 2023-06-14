package core.graphic;

import core.engine.io.Utils;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	private final int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		programID = glCreateProgram();
		if(programID == 0) throw new RuntimeException("Could not create Shader");
		
		vertexShaderID = loadShader(Utils.readFile(vertexFile), GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(Utils.readFile(fragmentFile), GL_FRAGMENT_SHADER);
		
		link();	
		
	}
	
	public void bind() {
		glUseProgram(programID);
	}
	
	public void cleanup() {
		unbind();
		if(programID != 0) glDeleteProgram(programID);
	}
	
	protected int loadShader(StringBuilder shaderSource, int type) {
		int shaderID = glCreateShader(type);
		if(shaderID == 0) throw new RuntimeException("Error creating shader. Type: " + type);
		
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling shader code: " + glGetShaderInfoLog(shaderID, 1024));
		
		glAttachShader(programID, shaderID);
		
		return shaderID;
	}
	
	private void link() {
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == 0) throw new RuntimeException("Error linking shader code: " + glGetProgramInfoLog(programID, 1024));
		
		glDetachShader(programID, fragmentShaderID);
		glDetachShader(programID, vertexShaderID);

	}
	 
	public void unbind() {
		glUseProgram(0);
	}
	
	public void validate() {
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) throw new RuntimeException("Error validating Shader code: " + glGetProgramInfoLog(programID, 1024));
	}

	public int getProgramID() {
		return programID;
	}
	
	
	
}
