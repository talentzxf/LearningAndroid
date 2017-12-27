precision mediump float;
uniform sampler2D texture;
uniform vec2 delta;
varying vec2 coord;

// The data in the texture is (position.y, velocity.y, normal.x, normal.z)
void main() {
  /* get vertex info */
  vec4 info = texture2D(texture, coord);

  if(info.b > 0.9){
    info.g = -info.g;
  }

  /* calculate average neighbor height */
  vec2 dx = vec2(delta.x, 0.0);
  vec2 dy = vec2(0.0, delta.y);
  float average = (
    texture2D(texture, coord - dx).r +
    texture2D(texture, coord - dy).r +
    texture2D(texture, coord + dx).r +
    texture2D(texture, coord + dy).r
  ) * 0.25;

  /* change the velocity to move toward the average */
  info.g += (average - info.r) * 2.0;

  /* attenuate the velocity a little so waves do not last forever */
  info.g *= 0.995;

  /* move the vertex along the velocity */
  info.r += info.g;

  if(info.g < 0.0){
    info.b = 1.0;
    info.g = -info.g;
  }else{
    info.b = 0.0;
  }

  gl_FragColor = info;
}