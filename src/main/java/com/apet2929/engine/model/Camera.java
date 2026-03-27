package com.apet2929.engine.model;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {
    private Matrix4f viewMatrix;
    private float zoom;
    private Vector3f position;

    public Camera() {
        this.zoom = 1;
        this.position = new Vector3f(0,0,15);
        this.viewMatrix = createViewMatrix();
    }

    public Vector2f inverse(Vector2f position) {
        Vector4f pos = new Vector4f(position, 0, 1);
        Matrix4f inverted = new Matrix4f();
        this.viewMatrix.invert(inverted);
        Vector4f result = pos.mul(inverted);
        return new Vector2f(result.x, result.y);
    }

    public void rotate(float theta) {
        this.viewMatrix.rotate(theta, new Vector3f(0,1,0));
    }

    public void move(Vector2f offset) {
        this.position = this.position.add(offset.x, offset.y, 0);
        this.viewMatrix = createViewMatrix();
    }

    public void move(Vector3f offset) {
        this.position = this.position.add(offset);
        this.viewMatrix = createViewMatrix();
    }

    public Vector3f getPosition() {
        return new Vector3f(this.position);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }

    private Matrix4f createViewMatrix() {
        Vector3f pos = this.position;
        Vector3f rot = new Vector3f(); // camera.getRotation();

        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1,0,0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0,1,0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0,0,1));
        matrix.translate(-pos.x, -pos.y, -pos.z);   //  Move the world opposite the camera
        return matrix;
    }
}
