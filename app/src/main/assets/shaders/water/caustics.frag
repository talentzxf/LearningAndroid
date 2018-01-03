#extension GL_OES_standard_derivatives : enable
precision mediump float;

#include shaders/functions/constants.glsl
#include shaders/functions/common_variables.glsl

varying vec3 oldPos;
varying vec3 newPos;
varying vec3 ray;

void main() {
    /* if the triangle gets smaller, it gets brighter, and vice versa */
    float oldArea = length(dFdx(oldPos)) * length(dFdy(oldPos));
    float newArea = length(dFdx(newPos)) * length(dFdy(newPos));
    gl_FragColor = vec4(oldArea / newArea * 0.2, 1.0, 0.0, 0.0);

    vec3 refractedLight = refract(-light.xyz, vec3(0.0, 1.0, 0.0), IOR_AIR / IOR_WATER);
    /* compute a blob shadow and make sure we only draw a shadow if the player is blocking the light */
    vec3 dir = (sphereCenter - newPos) / sphereRadius;
    vec3 area = cross(dir, refractedLight);
    float shadow = dot(area, area);
    float dist = dot(dir, -refractedLight);
    shadow = 1.0 + (shadow - 1.0) / (0.05 + dist * 0.025);
    shadow = clamp(1.0 / (1.0 + exp(-shadow)), 0.0, 1.0);
    shadow = mix(1.0, shadow, clamp(dist * 2.0, 0.0, 1.0));
    gl_FragColor.g = shadow;
}