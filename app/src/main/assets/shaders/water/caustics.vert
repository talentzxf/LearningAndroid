precision mediump float;

varying vec3 oldPos;
varying vec3 newPos;
varying vec3 ray;
attribute vec3 vPosition;

#include shaders/functions/helpfunctions.glsl

void main() {
  vec4 info = texture2D(info_Texture, vPosition.xy * 0.5 + 0.5);
  info.ba *= 0.5;
  vec3 normal = vec3(info.b, sqrt(1.0 - dot(info.ba, info.ba)), info.a);

  /* project the vertices along the refracted vertex ray */
  vec3 refractedLight = refract(-light, vec3(0.0, 1.0, 0.0), IOR_AIR / IOR_WATER);
  ray = refract(-light, normal, IOR_AIR / IOR_WATER);
  oldPos = project(vPosition.xzy, refractedLight, refractedLight);
  newPos = project(vPosition.xzy + vec3(0.0, info.r, 0.0), ray, refractedLight);

  // Not sure what this means???
  gl_Position = vec4(0.75 * (newPos.xz + refractedLight.xz / refractedLight.y), 0.0, 1.0);
}