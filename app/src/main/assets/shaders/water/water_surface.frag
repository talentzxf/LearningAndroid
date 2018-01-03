precision mediump float;

const vec3 abovewaterColor = vec3(0.25, 1.0, 1.25);
const vec3 underwaterColor = vec3(0.4, 0.9, 1.0);
uniform vec3 cameraPos;
uniform mat4 sphere_model;

varying vec4 fragPos;
varying vec4 aPos;
varying vec4 normal;

const vec4 ambient = vec4(0.5,0.5,0.5,0.1);

#include shaders/functions/helpfunctions.glsl

void main() {
    vec3 incomingRay = normalize(aPos.xyz - cameraPos);
    vec3 refractedRay = refract(incomingRay, normal.xyz, IOR_AIR/IOR_WATER);
    vec3 reflectedRay = reflect(incomingRay, normal.xyz);
    float fresnel = mix(0.25, 1.0, pow(1.0 - dot(normal.xyz, -incomingRay), 3.0));
    vec3 refractedColor = getSurfaceRayColor(aPos.xyz,refractedRay,abovewaterColor.rgb, sphere_model);
    vec3 reflectedColor = getSurfaceRayColor(aPos.xyz,reflectedRay,abovewaterColor.rgb, sphere_model);

    gl_FragColor = vec4(mix(refractedColor, reflectedColor, fresnel), 1.0);
}