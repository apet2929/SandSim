package com.apet2929.engine.model;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    @Test
    public void testGetViewMatrix(){
        Camera cam = new Camera();
        Matrix4f viewMatrix1 = cam.getViewMatrix();
        assertEquals(cam.getViewMatrix(), viewMatrix1);
        assertNotSame(cam.getViewMatrix(), viewMatrix1, "getViewMatrix returns new object every time");
    }

    @Test
    public void testNotMoving(){
        Camera cam = new Camera();
        Matrix4f transformBefore = cam.getViewMatrix();
        cam.move(new Vector3f(0.0f, 0.0f, 0.0f));
        assertEquals(transformBefore, cam.getViewMatrix(), cam.getViewMatrix().toString());
    }

    @Test
    public void testMoveX(){
        Camera cam = new Camera();
        Matrix4f transformBefore = cam.getViewMatrix();
        cam.move(new Vector3f(0.0f, 0.0f, 0.0f));
        assertEquals(transformBefore, cam.getViewMatrix(), cam.getViewMatrix().toString());
        cam.move(new Vector3f(1.0f, 0.0f, 0.0f));
        assertEquals(transformBefore.m30() - 1.0f, cam.getViewMatrix().m30(), // camera should move in opposite direction of specified vector
                cam.getViewMatrix().toString());
    }

    @Test
    public void testMoveTwoArgs(){
        Camera cam = new Camera();
        Matrix4f transformBefore = cam.getViewMatrix();
        cam.move(new Vector2f(0.0f, 1.0f));
        assertEquals(transformBefore.m31() - 1.0f, cam.getViewMatrix().m31(), // camera should move in opposite direction of specified vector
                cam.getViewMatrix().toString());

    }
}