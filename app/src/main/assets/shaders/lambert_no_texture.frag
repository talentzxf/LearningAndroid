precision mediump float;
varying vec4 aColor;
varying vec4 fragPos;
vec4 ambient = vec4(0.5,0.5,0.5,1.0);
void main() {
    gl_FragColor = ambient + aColor;
}