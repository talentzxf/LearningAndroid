precision mediump float;

uniform mat4 project;
uniform mat4 model;
uniform mat4 view;
attribute vec3 vPos;
attribute vec3 vNormal;
attribute vec2 texCoord;
varying vec4 pos;
varying vec4 normal;
varying vec2 texcoord;

void main() {
  gl_Position = project * view * model * vec4(vPos,1.0);
  normal = model * vec4(vNormal,1.0);
  pos = model * vec4(vPos, 1.0);
  texcoord = texCoord;
}