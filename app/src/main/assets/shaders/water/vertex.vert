varying vec2 coord;
attribute vec4 vPosition;
void main(){
  coord = vPosition.xz * 0.5 + 0.5;
  gl_Position = vec4(vPosition.xyz, 1.0);
}
