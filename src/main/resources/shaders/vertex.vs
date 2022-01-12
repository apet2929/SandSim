#version 400 core

in vec3 position;
in vec2 textureCoord;

out vec2 fragTextureCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 1.0);
    fragTextureCoord = textureCoord;
}
