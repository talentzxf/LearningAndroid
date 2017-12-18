precision mediump float;
varying vec2 texCoord;
varying vec4 aColor;
varying vec4 fragPos;
uniform sampler2D u_Texture;
vec4 ambient = vec4(0.5,0.5,0.5,1.0);
void main() {
  gl_FragColor = (ambient + aColor) * texture2D(u_Texture, texCoord);
}