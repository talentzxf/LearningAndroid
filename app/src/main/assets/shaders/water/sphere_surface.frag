precision mediump float;
varying vec4 aPos;
uniform mat4 model;
const vec3 underwaterColor = vec3(0.4, 0.9, 1.0);
vec4 ambient = vec4(0.5,0.5,0.5,1.0);

#include shaders/functions/helpfunctions.glsl

void main() {
  gl_FragColor = vec4(getSphereColor(aPos, model), 1.0);
  vec4 info = texture2D(info_Texture, aPos.xz * 0.5 + 0.5);
  if (aPos.y < info.r) {
    gl_FragColor.rgb *= underwaterColor * 1.2;
  }
}