precision mediump float;
varying vec3 position;

const vec3 underwaterColor = vec3(0.4, 0.9, 1.0);

#include shaders/functions/helpfunctions.glsl

void main() {
  gl_FragColor = vec4(getWallColor(position), 1.0);
  vec4 info = texture2D(info_Texture, position.xz * 0.5 + 0.5);
  if (position.y < info.r) {
    gl_FragColor.rgb *= underwaterColor * 1.2;
  }
}