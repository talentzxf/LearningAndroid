#include shaders/functions/constants.glsl
#include shaders/functions/common_variables.glsl

vec2 intersectCube(vec3 origin, vec3 ray, vec3 cubeMin, vec3 cubeMax) {
  vec3 tMin = (cubeMin - origin) / ray;
  vec3 tMax = (cubeMax - origin) / ray;
  vec3 t1 = min(tMin, tMax);
  vec3 t2 = max(tMin, tMax);
  float tNear = max(max(t1.x, t1.y), t1.z);
  float tFar = min(min(t2.x, t2.y), t2.z);
  return vec2(tNear, tFar);
}

/* project the ray onto the plane */
vec3 project(vec3 origin, vec3 ray, vec3 refractedLight) {
  vec2 tcube = intersectCube(origin, ray, vec3(-1.0, -1.0, -1.0), vec3(1.0, poolHeight, 1.0));
  origin += ray * tcube.y;
  float tplane = (-origin.y - 1.0) / refractedLight.y;
  return origin + refractedLight * tplane;
}

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

vec3 getWallColor(vec3 point) {
  float scale = 0.5;

  vec3 wallColor;
  vec3 normal;
  if (abs(point.x) > 0.999) {
    wallColor = texture2D(tiles, point.yz * 0.5 + vec2(1.0, 0.5)).rgb;
    normal = vec3(-point.x, 0.0, 0.0);
  } else if (abs(point.z) > 0.999) {
    wallColor = texture2D(tiles, point.yx * 0.5 + vec2(1.0, 0.5)).rgb;
    normal = vec3(0.0, 0.0, -point.z);
  } else {
    wallColor = texture2D(tiles, point.xz * 0.5 + 0.5).rgb;
    normal = vec3(0.0, 1.0, 0.0);
  }

  scale /= length(point); /* pool ambient occlusion */
  // scale *= 1.0 - 0.9 / pow(length(point - sphereCenter) / sphereRadius, 4.0); /* sphere ambient occlusion */

  /* caustics */
  vec3 refractedLight = -refract(-light.xyz, vec3(0.0, 1.0, 0.0), IOR_AIR / IOR_WATER);
  float diffuse = max(0.0, dot(refractedLight, normal));
  vec4 info = texture2D(info_Texture, point.xz * 0.5 + 0.5);
  if (point.y < info.r) {
    vec4 caustic = texture2D(causticTex, 0.75 * (point.xz - point.y * refractedLight.xz / refractedLight.y) * 0.5 + 0.5);
    scale += diffuse * caustic.r * 2.0 * caustic.g;
  }
//  else {
//    /* shadow for the rim of the pool */
//    vec2 t = intersectCube(point, refractedLight, vec3(-1.0, -poolHeight, -1.0), vec3(1.0, 2.0, 1.0));
//    diffuse *= 1.0 / (1.0 + exp(-200.0 / (1.0 + 10.0 * (t.y - t.x)) * (point.y + refractedLight.y * t.y - 2.0 / 12.0)));
//
//    scale += diffuse * 0.5;
//  }

  return wallColor * scale;
}

// Get sphere color given a world point.
vec3 getSphereColor(vec4 hitPoint_world, mat4 model) {
    mat4 sphere_model = model;
   // Calculate sphere color
   // 1. Convert world space coord to sphere local space.
   mat4 sphere_model_inverse = inverse(sphere_model);
   // 2. Position_world = model * local => local = model_inverse*position_world
   vec4 hitPoint_sphere_local = sphere_model_inverse * hitPoint_world;
   // 3. Calculte theta1 (rotation around z)
   float v = acos(hitPoint_sphere_local.y / sphereRadius) / M_PI;
   // 4. calculate theta2 (rotation around y)
   float theta2 = -atan(hitPoint_sphere_local.z,hitPoint_sphere_local.x) + M_PI;
   float u = theta2/(2.0*M_PI);
   vec3 sph_Color = texture2D(sph_Texture,vec2(u,v)).rgb;

   // 5. get caustics
   vec3 sphereNormal = (hitPoint_world.xyz - sphereCenter) / sphereRadius;
   vec3 refractedLight = refract(-light.xyz, vec3(0.0, 1.0, 0.0), IOR_AIR / IOR_WATER);
   float diffuse = max(0.0, dot(-refractedLight, sphereNormal)) * 0.2;
   vec4 info = texture2D(info_Texture, hitPoint_world.xz * 0.5 + 0.5);

   if (hitPoint_world.y < info.r) {
   vec4 caustic = texture2D(causticTex,
       0.75 * (hitPoint_world.xz - hitPoint_world.y * refractedLight.xz / refractedLight.y) * 0.5 + 0.5);
   diffuse *= caustic.r;
   }
   sph_Color = sph_Color + diffuse;
   return sph_Color;
}

vec3 getSurfaceRayColor(vec3 origin, vec3 ray, vec3 waterColor, mat4 sphere_model){
    vec3 color = waterColor;

    // Intersect with sphere first
    float q = intersectSphere(origin, ray, sphereCenter, sphereRadius);
    if(q<1.0e6){
        vec4 hitPoint_world = vec4(origin + ray*q,1.0);
        color = getSphereColor(hitPoint_world, sphere_model);
    } else if(ray.y < 0.0){
            vec2 t = intersectCube(origin, ray, vec3(-1.0, -1.0, -1.0), vec3(1.0, poolHeight, 1.0));
            color = getWallColor(origin+ray*t.y);
    } else{
        vec2 t = intersectCube(origin, ray, vec3(-1.0, -1.0, -1.0), vec3(1.0, 2.0, 1.0));
        vec3 hit = origin + ray*t.y;
        if(hit.y < 2.0/12.0){
            color = getWallColor(hit);
        }else{
            color = vec3(0.2,0.2,0.5);
            color += vec3(pow(max(0.0, dot(light.xyz, ray)), 5000.0)) * vec3(10.0, 8.0, 6.0);
        }
    }

    if (ray.y < 0.0) color *= waterColor;
    return color;
}