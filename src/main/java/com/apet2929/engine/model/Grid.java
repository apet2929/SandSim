package com.apet2929.engine.model;

import com.apet2929.engine.utils.Consts;
import org.joml.*;
import org.lwjgl.system.CallbackI;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grid {

    private final int id;
    private float x, y, width, height;
    private int numCols, numRows;

    public Grid(int id, int numCols, int numRows) {
        this.id = id;
        this.x = -1f;
        this.y = -1f;
        this.width = Consts.GRID_WIDTH;
        this.height = Consts.GRID_HEIGHT;
        this.numCols = numCols;
        this.numRows = numRows;
    }


    public int getId() {
        return id;
    }

    public int getNumLines() {
        return (numCols + numRows + 2);
    }

    public void init(float x, float y, float width, float height, int numCols, int numRows) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.numCols = numCols;
        this.numRows = numRows;
    }


    public Vector3f calculateGridPosition(float gridX, float gridY) {
        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f pos = new Vector3f(0.0f,0.0f,0.0f);
        pos.x = calculatePointComponent(gridX, dx, x) + dx / 2;
        pos.y = calculatePointComponent(gridY, dy, y) + dx / 2;
        pos.z = Consts.GRID_Z;
        return pos;
    }

    public Vector2i worldToGridCoordinates(Vector3f normalizedMouseCoord) {
        Vector2f worldCoord = new Vector2f(normalizedMouseCoord.x * -Consts.GRID_Z, normalizedMouseCoord.y * -Consts.GRID_Z/1.75f);
//        Translate bottom left to (0,0)
        float translatedX = worldCoord.x - x;
        float translatedY = worldCoord.y - y;

//        Scale by row/col size
        float scaledX = translatedX / getDx();
        float scaledY = translatedY / getDx();
        return new Vector2i(Math.round(scaledX), Math.round(scaledY));
    }

    public Vector2f[] calculateGridLines() {
        int numPoints = getNumLines() * 2;
        Vector2f[] lines = new Vector2f[numPoints];

        float dx = width / numCols;
        float dy = height / numRows;
        Vector2f startPoint = new Vector2f();
        Vector2f endPoint = new Vector2f();
//        Vertical lines, Columns
        int v = 0;
        for (int i = 0; i <= numCols; i++) {
            float xP = calculatePointComponent(i, dx, x);
            startPoint.x = xP;
            startPoint.y = y;
            endPoint.x = xP;
            endPoint.y = y + height;
            lines[v] = new Vector2f(startPoint);
            lines[v + 1] = new Vector2f(endPoint);
            v += 2;
        }

//        Horizontal lines, Rows
        for (int i = 0; i <= numRows; i++) {
            float yP = calculatePointComponent(i, dy, y);
            startPoint.y = yP;
            startPoint.x = x;
            endPoint.y = yP;
            endPoint.x = x + width;
            lines[v] = new Vector2f(startPoint);
            lines[v + 1] = new Vector2f(endPoint);
            v += 2;
        }
        return lines;
    }

    public float[] getScaledVertices() {
        // Returns vertices for a rectangle with width dx and height dy centered at (0,0)
        float dx = getDx() / 2;
        float dy = getDy() / 2;
        return new float[] {
                -dx, dy, 0f,    //  Top left vertex
                -dx, -dy, 0f,   //  Bottom left vertex
                dx, -dy, 0f,    //  Bottom right vertex
                dx, dy, 0f,     //  Top right
        };
    }

    private float getDx() {
        return width / numCols;
    }
    private float getDy() {
        return height / numCols;
    }

    private static float calculatePointComponent(float i, float di, float startI) {
        return startI + (i * di);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }
}
