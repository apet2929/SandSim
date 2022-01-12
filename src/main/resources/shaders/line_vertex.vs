#version 400 core

in vec2 aPos;
in vec3 iColor;

out vec3 oColor;

void main() {
    oColor = iColor;
    gl_Position = vec4(aPos.x, aPos.y, 0.0, 1.0f);
}