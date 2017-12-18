uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;\
attribute vec4 vPosition;
attribute vec4 vNormal;
uniform vec4 lightPos;
varying vec4 aColor;
uniform vec4 vColor;
void main() {
   // The matrix must be included as a modifier of gl_Position.
   // Note that the uMVPMatrix factor *must be first* in order
  // for the matrix multiplication product to be correct.
  gl_Position = projection * view * model * vPosition;
  aColor = max( vColor * dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz)) ,0.0);
}