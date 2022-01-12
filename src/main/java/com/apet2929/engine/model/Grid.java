package com.apet2929.engine.model;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grid {

    public static void drawLine(float x0, float y0, float x1, float y1) {
        glVertex2f(x0, y0);
        glVertex2f(x1, y1);
    }

    public static void drawGrid(float x, float y, float width, float height, int numRows, int numCols) {
        glBegin(GL_LINES);
        float x0, y0, x1, y1;
        float dx = width / numRows;
        float dy = height / numCols;

        y0 = y;
        y1 = y + height;
        for (int xi = 0; xi < numRows; xi++) {
            x0 = x + (xi * dx);
            drawLine(x0, y0, x0, y1);
        }

        x0 = x;
        x1 = x + width;
        for (int yi = 0; yi < numRows; yi++) {
            y0 = y + (yi * dy);
            drawLine(x0, y0, x1, y0);
        }

        glEnd();
    }

//    public static float[] calculateVertices(float x, float y, float width, float height, int numRows, int numCols) {
//        List<Float> vertices = new ArrayList<>();
//        float dx = width / numRows;
//        float dy = height / numCols;
//        for (int xi = 0; xi < numRows; xi++) {
//            for (int yi = 0; yi < numCols; yi++) {
//                vertices.add(x + (xi * dx)); // x
//                vertices.add(y + (y * dy)); // y
//                vertices.add(0.0f); // z
//            }
//        }
//
//        float[] v = new float[vertices.size()];
//
//        for (int i = 0; i < vertices.size(); i++) {
//            v[i] = vertices.get(i);
//        }
//        return v;
//    }
//



}
