uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
attribute vec4 vPosition;
varying vec4 aColor;
uniform vec4 vColor;
varying vec4 aPos;
varying vec4 normal;
uniform sampler2D info_Texture;

void main() {
   // The matrix must be included as a modifier of gl_Position.
   // Note that the uMVPMatrix factor *must be first* in order
  // for the matrix multiplication product to be correct.
  vec4 info = texture2D(info_Texture, vPosition.xy*0.5+0.5);
  gl_Position = projection * view * model * vPosition.xzyw;
  gl_Position.y += info.r;
  aPos = model * vPosition.xzyw;

  // Get normal
  info.ba *= 0.5;
  vec3 obj_normal = vec3(info.b, sqrt(1.0 - dot(info.ba, info.ba)), info.a);
  normal = model*vec4(obj_normal,1.0);
  //aColor = max( vColor * dot( normalize(vNormal.xzy), normalize((light - model*vPosition).xyz)) ,0.0);
}