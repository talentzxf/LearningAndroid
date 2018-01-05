precision mediump float;

uniform vec3 light;
uniform sampler2D texture;
varying vec2 texcoord;
varying vec4 normal;
varying vec4 pos;

const vec4 ambient = vec4(0.3,0.3,0.3,1.0);

void main() {
    float diffuse = dot(normalize( pos.xyz - light ), normalize(normal.xyz));
    gl_FragColor = texture2D(texture, texcoord) * vec4(diffuse,diffuse,diffuse,1.0) + ambient;
}