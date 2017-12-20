precision mediump float;
#define M_PI 3.1415926535897932384626433832795

uniform vec3 cameraPos;
uniform mat4 sphere_model;
varying vec4 aColor;
varying vec4 fragPos;
varying vec4 aPos;
varying vec4 normal;
vec4 ambient = vec4(0.3,0.3,0.3,0.1);
const vec3 sphereCenter = vec3(0, -0.5,0.0);
const float sphereRadius = 0.3;
const float IOR_AIR = 1.0;
const float IOR_WATER = 1.333;
// Sphere texture
uniform sampler2D sph_Texture;

mat4 inverse(mat4 m) {
  float
      a00 = m[0][0], a01 = m[0][1], a02 = m[0][2], a03 = m[0][3],
      a10 = m[1][0], a11 = m[1][1], a12 = m[1][2], a13 = m[1][3],
      a20 = m[2][0], a21 = m[2][1], a22 = m[2][2], a23 = m[2][3],
      a30 = m[3][0], a31 = m[3][1], a32 = m[3][2], a33 = m[3][3],

      b00 = a00 * a11 - a01 * a10,
      b01 = a00 * a12 - a02 * a10,
      b02 = a00 * a13 - a03 * a10,
      b03 = a01 * a12 - a02 * a11,
      b04 = a01 * a13 - a03 * a11,
      b05 = a02 * a13 - a03 * a12,
      b06 = a20 * a31 - a21 * a30,
      b07 = a20 * a32 - a22 * a30,
      b08 = a20 * a33 - a23 * a30,
      b09 = a21 * a32 - a22 * a31,
      b10 = a21 * a33 - a23 * a31,
      b11 = a22 * a33 - a23 * a32,

      det = b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;

  return mat4(
      a11 * b11 - a12 * b10 + a13 * b09,
      a02 * b10 - a01 * b11 - a03 * b09,
      a31 * b05 - a32 * b04 + a33 * b03,
      a22 * b04 - a21 * b05 - a23 * b03,
      a12 * b08 - a10 * b11 - a13 * b07,
      a00 * b11 - a02 * b08 + a03 * b07,
      a32 * b02 - a30 * b05 - a33 * b01,
      a20 * b05 - a22 * b02 + a23 * b01,
      a10 * b10 - a11 * b08 + a13 * b06,
      a01 * b08 - a00 * b10 - a03 * b06,
      a30 * b04 - a31 * b02 + a33 * b00,
      a21 * b02 - a20 * b04 - a23 * b00,
      a11 * b07 - a10 * b09 - a12 * b06,
      a00 * b09 - a01 * b07 + a02 * b06,
      a31 * b01 - a30 * b03 - a32 * b00,
      a20 * b03 - a21 * b01 + a22 * b00) / det;
}


float intersectSphere(vec3 origin, vec3 ray, vec3 sphereCenter, float sphereRadius) {
  vec3 toSphere = origin - sphereCenter;
  float a = dot(ray, ray);
  float b = 2.0 * dot(toSphere, ray);
  float c = dot(toSphere, toSphere) - sphereRadius * sphereRadius;
  float discriminant = b*b - 4.0*a*c;
  if (discriminant > 0.0) {
    float t = (-b - sqrt(discriminant)) / (2.0 * a);
    if (t > 0.0) return t;
  }
  return 1.0e6;
}

vec3 getSurfaceRayColor(vec3 origin, vec3 ray, vec3 waterColor){
    // Intersect with sphere first
    vec3 color = waterColor;
    float q = intersectSphere(origin, ray, sphereCenter, sphereRadius);
    if(q<1.0e6){
        vec4 hitPoint_world = vec4(origin + ray*q,1.0);
        // Calculate sphere color
        // 1. Convert world space coord to sphere local space.
        mat4 sphere_model_inverse = inverse(sphere_model);
        // 2. Position_world = model * local => local = model_inverse*position_world
        vec4 hitPoint_sphere_local = sphere_model_inverse * hitPoint_world;
        // 3. Calculte theta1 (rotation around z)
        float u = acos(hitPoint_sphere_local.y / sphereRadius) / M_PI;
        // 4. calculate theta2 (rotation around y)
        float theta1 = -1.0;
        if(hitPoint_sphere_local.z>0.0){
            theta1 = acos(-hitPoint_sphere_local.x/sphereRadius);
        }else{
            theta1 = acos(-hitPoint_sphere_local.x/sphereRadius)+M_PI;
        }
        float v = theta1/2.0*M_PI;
        return texture2D(sph_Texture,vec2(u,v)).rgb;
    }
    return vec3(1.0,1.0,0.0);
}
void main() {
    vec3 incomingRay = normalize(aPos.xyz - cameraPos);
    vec3 refractedRay = refract(incomingRay, normal.xyz, IOR_AIR/IOR_WATER);
    vec3 rayColor = getSurfaceRayColor(aPos.xyz,refractedRay,aColor.rgb);
    gl_FragColor = vec4(rayColor,1.0);
     // gl_FragColor = vec4(aPos.xyz,1.0);// + vec4(,1.0) - vec4(refractedRay,1.0);
}