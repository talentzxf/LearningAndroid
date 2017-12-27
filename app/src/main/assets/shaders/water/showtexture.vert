uniform mat4 mvpMatrix;
attribute vec4 vPosition;
varying vec2 coord;
void main(){
  coord = vPosition.xy * 0.5 + 0.5;
  gl_Position = mvpMatrix*vPosition;
}