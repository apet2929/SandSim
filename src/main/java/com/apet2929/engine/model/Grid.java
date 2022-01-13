package com.apet2929.engine.model;

import com.apet2929.engine.utils.Consts;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grid {

    private final int id;
    private final int numLines;
    private float x, y, width, height;
    private int numCols, numRows;

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

    public void init(float x, float y, float width, float height, int numCols, int numRows) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.numCols = numCols;
        this.numRows = numRows;
    }

    public void setParticleTransformationMatrix(Matrix4f destination, int gridX, int gridY) {
//        Sets destination to the transformationMatrix used to render the particle
        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f pos = new Vector3f();
        pos.x = calculatePointComponent(gridX, dx, x);
        pos.y = calculatePointComponent(gridY, dy, y);
        pos.z = Consts.GRID_Z;
        destination.identity().translate(pos);
    }

    public Vector3f calculateGridPosition(int gridX, int gridY) {
        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f pos = new Vector3f(0.0f,0.0f,0.0f);
        pos.x = calculatePointComponent(gridX, dx, x) + dx / 2;
        pos.y = calculatePointComponent(gridY, dy, y) + dx / 2;
        pos.z = Consts.GRID_Z;
        return pos;
    }

    public static Vector3f[] calculateGridLines(float x, float y, float width, float height, int numCols, int numRows) {
        int numPoints = (numCols + numRows) * 2;
        Vector3f[] lines = new Vector3f[numPoints];

        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f startPoint = new Vector3f();
        Vector3f endPoint = new Vector3f();
        // TODO : Make grid z variable
        endPoint.z = Consts.GRID_Z;
        startPoint.z = Consts.GRID_Z;
//        Vertical lines, Columns
        int v = 0;
        for (int i = 0; i < numCols; i++) {
            float xP = calculatePointComponent(i, dx, x);
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
            float yP = calculatePointComponent(i, dy, y);
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
        return startI + (di / 2) + (i * di);
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
