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
    private float width, height;
    private int numCols, numRows;

    public Grid(int id, int numCols, int numRows) {
        this.id = id;

        if(numCols > numRows) {
            float aspectRatio = (float) numCols / numRows;
            this.width = 2.0f * aspectRatio;
            this.height = 2.0f;
        } else if(numRows > numCols) {
            float aspectRatio = (float) numRows / numCols;
            this.height = 2.0f * aspectRatio;
            this.width = 2.0f;
        } else {
            this.width = 2.0f;
            this.height = 2.0f;
        }

        this.numCols = numCols;
        this.numRows = numRows;
    }

    public int getId() {
        return id;
    }

    public int getNumLines() {
        return (numCols + numRows + 2);
    }

    public void init( float width, float height, int numCols, int numRows) {
        this.width = width;
        this.height = height;
        this.numCols = numCols;
        this.numRows = numRows;
    }


    public Vector3f calculateGridPosition(float gridX, float gridY) {
        float dx = getDx();
        float dy = getDy();
        Vector3f pos = new Vector3f(0.0f,0.0f,0.0f);
        pos.x = calculatePointComponent(gridX, dx, 0) + dx / 2;
        pos.y = calculatePointComponent(gridY, dy, 0) + dy / 2;
        pos.z = Consts.GRID_Z;
        return pos;
    }

    public Vector2i worldToGridCoordinates(Vector2f normalizedMouseCoord) {
        Vector2f worldCoord = new Vector2f(normalizedMouseCoord.x * -Consts.GRID_Z, normalizedMouseCoord.y * -Consts.GRID_Z/1.75f);
//        Translate bottom left to (0,0)
        float translatedX = worldCoord.x - 0;
        float translatedY = worldCoord.y - 0;

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
            float xP = calculatePointComponent(i, dx, 0);
            startPoint.x = xP;
            startPoint.y = 0;
            endPoint.x = xP;
            endPoint.y = 0 + height;
            lines[v] = new Vector2f(startPoint);
            lines[v + 1] = new Vector2f(endPoint);
            v += 2;
        }

//        Horizontal lines, Rows
        for (int i = 0; i <= numRows; i++) {
            float yP = calculatePointComponent(i, dy, 0);
            startPoint.y = yP;
            startPoint.x = 0;
            endPoint.y = yP;
            endPoint.x = 0 + width;
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
        return 1;
    }
    private float getDy() {
        return 1;
    }

    private static float calculatePointComponent(float i, float di, float startI) {
        return startI + (i * di);
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
