#extension GL_OES_standard_derivatives : enable

precision mediump float;

varying vec3 oldPos;
varying vec3 newPos;
varying vec3 ray;

void main() {
    /* if the triangle gets smaller, it gets brighter, and vice versa */
    float oldArea = length(dFdx(oldPos)) * length(dFdy(oldPos));
    float newArea = length(dFdx(newPos)) * length(dFdy(newPos));
    gl_FragColor = vec4(oldArea / newArea * 0.2, 1.0, 0.0, 0.0);
}