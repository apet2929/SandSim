package com.apet2929.engine.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grid {

    private final int id;
    private final int numLines;

    public Grid(int id, int numLines) {
        this.id = id;
        this.numLines = numLines;
    }

    public int getId() {
        return id;
    }

    public int getNumLines() {
        return numLines;
    }

    public static Vector3f[] calculateGridLines(float x, float y, float width, float height, int numCols, int numRows) {
        int numPoints = (numCols + numRows) * 2;
        Vector3f[] lines = new Vector3f[numPoints];

        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f startPoint = new Vector3f();
        Vector3f endPoint = new Vector3f();
        endPoint.z = -2;
        startPoint.z = -2;
//        Vertical lines, Columns
        int v = 0;
        for (int i = 0; i < numCols; i++) {
            float xP = x + (dx / 2) + (i * dx);
            startPoint.x = xP;
            startPoint.y = y;
            endPoint.x = xP;
            endPoint.y = y + height;
            lines[v] = new Vector3f(startPoint);
            lines[v + 1] = new Vector3f(endPoint);
            v += 2;
        }


//        Horizontal lines, Rows
        for (int i = 0; i < numRows; i++) {
            float yP = y + (dy / 2) + (i * dy);
            startPoint.y = yP;
            startPoint.x = x;
            endPoint.y = yP;
            endPoint.x = x + width;
            lines[v] = new Vector3f(startPoint);
            lines[v + 1] = new Vector3f(endPoint);
            v += 2;
        }
        return lines;

    }
}
