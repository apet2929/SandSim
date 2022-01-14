#version 400 core

in vec2 pos;

uniform mat4 projectionMatrix;
uniform float grid_z;

void main() {
    gl_Position = projectionMatrix * vec4(pos, grid_z, 1.0f);
}