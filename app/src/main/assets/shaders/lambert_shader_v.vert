uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
attribute vec4 vPosition;
attribute vec4 vNormal;
attribute vec2 vTexCoord;
uniform vec4 vColor;
uniform vec4 lightPos;
varying vec4 aColor;
varying vec2 texCoord;
void main() {
   // The matrix must be included as a modifier of gl_Position.
   // Note that the uMVPMatrix factor *must be first* in order
  // for the matrix multiplication product to be correct.
  gl_Position = projection * view * model * vPosition;
  aColor = max( vColor * dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz)) ,0.0);
  texCoord = vTexCoord;
}