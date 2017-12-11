package com.example.vincentzhang.learnandroid.shapes;

/**
 * Created by VincentZhang on 12/7/2017.
 * https://github.com/kibotu/net.gtamps/blob/refactoring3d/android/graphic/src/net/gtamps/android/renderer/graph/scene/primitives/Sphere.java
 */

public class Sphere {
    private int stacks;
    private int slices;
    private float[] vertices;
    private float[] normals;
    private float[] textures;

//    public Sphere(float radius, int stacks, int slices) {
//        this.stacks = stacks;
//        this.slices = slices;
//
//        vertices = new float[(stacks + 1)*(slices+1) * 3];
//        normals = new float[stacks * slices * 3];
//        textures = new float[(stacks + 1)*(slices+1) * 2];
//
//
//        // vertices
//        for (int r = 0; r <= slices; r++) {
//            float v = (float) r / (float) slices; // [0,1]
//            float theta1 = v * (float) Math.PI; // [0,PI]
//
//            float
//            n.rotateZ(theta1);
//
//            for (c = 0; c <= stacks; c++) {
//                float u = (float) c / (float) stacks; // [0,1]
//                float theta2 = u * (float) (Math.PI * 2f); // [0,2PI]
//                pos.set(n);
//                pos.rotateY(theta2);
//
//                posFull.set(pos);
//                posFull.mulInPlace(dimension.x);
//
//                mesh.vertices.addVertex(posFull.x, posFull.y, posFull.z, pos.x, pos.y, pos.z, emissive.r, emissive.g, emissive.b, emissive.a, u, v);
//            }
//        }
//    }
}
