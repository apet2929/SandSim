#version 400 core

in vec3 pos;
in vec3 iColor;

out vec3 oColor;

uniform mat4 projectionMatrix;

void main() {
    oColor = iColor;
    gl_Position = projectionMatrix * vec4(pos, 1.0f);
}