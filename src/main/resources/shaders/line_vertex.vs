#version 400 core

in vec3 aPos;
in vec3 iColor;

out vec3 oColor;

void main() {
    oColor = iColor;
    gl_Position = vec4(aPos, 1.0f);
}