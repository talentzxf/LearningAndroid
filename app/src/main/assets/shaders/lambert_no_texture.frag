precision mediump float;
varying vec4 aColor;
varying vec4 fragPos;
vec4 ambient = vec4(0.3,0.3,0.3,0.1);
void main() {
    gl_FragColor = ambient + aColor).xyz;
}