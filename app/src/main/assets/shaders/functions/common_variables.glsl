// constants
const vec3 sphereCenter = vec3(0, -0.5,0.0);
const float sphereRadius = 0.3;
const float poolHeight = 0.5;

// uniforms
uniform vec3 light;
uniform sampler2D tiles;
uniform sampler2D info_Texture;
uniform sampler2D causticTex;
uniform sampler2D sph_Texture;

// attributes