uniform mat4 project;
uniform mat4 view;
uniform mat4 model;
attribute vec3 vPos;
varying vec3 position;

void main() {
  position = vPos.xyz;
  gl_Position = project * view * model * vec4(position, 1.0);
}