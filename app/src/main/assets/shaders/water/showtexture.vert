uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
attribute vec4 vPosition;
attribute vec4 vNormal;
uniform vec4 lightPos;
varying vec4 aColor;
uniform vec4 vColor;
varying vec4 aPos;
varying vec4 normal;
//uniform sampler2D info_Texture;

void main() {
   // The matrix must be included as a modifier of gl_Position.
   // Note that the uMVPMatrix factor *must be first* in order
  // for the matrix multiplication product to be correct.
  // vec4 info = texture2D(info_Texture, vPosition.xy*0.5+0.5);
  // gl_Position = projection * view * model * ( vPosition + 0.001*vNormal + info.r);
  gl_Position = projection * view * model * ( vPosition + 0.001*vNormal);
  aPos = model * vPosition;
  normal = model*vNormal;
  aColor = max( vColor * dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz)) ,0.0);
}