precision mediump float;
uniform vec3 cameraPos;
varying vec4 aColor;
varying vec4 fragPos;
varying vec4 aPos;
varying vec4 normal;
vec4 ambient = vec4(0.3,0.3,0.3,0.1);
const vec3 sphereCenter = vec3(0, -0.5,0.0);
const float sphereRadius = 0.3;
const float IOR_AIR = 1.0;
const float IOR_WATER = 1.333;


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
        return vec3(1.0,0.0,1.0);
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