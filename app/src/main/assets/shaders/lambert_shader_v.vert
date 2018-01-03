precision mediump float;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
attribute vec4 vPosition;
varying vec4 aPos;
void main() {
   // The matrix must be included as a modifier of gl_Position.
   // Note that the uMVPMatrix factor *must be first* in order
  // for the matrix multiplication product to be correct.
  gl_Position = projection * view * model * vPosition;
  aPos = model * vPosition;
}