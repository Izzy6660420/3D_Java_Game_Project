#version 330

layout (location=0) in vec2 inPos;
layout (location=1) in vec2 inTexCoords;
layout (location=2) in vec4 inColor;

out vec2 fragTexCoords;
out vec4 fragColor;

uniform vec2 scale;

void main()
{
	fragTexCoords = inTexCoords;
	fragColor = inColor;
	gl_Position = vec4(inPos * scale + vec2(-1.0, 1.0), 0.0, 1.0);
}