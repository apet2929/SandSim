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
        /*
        worldCoord: A normalized Vector4f in range (-1, -1) to (1,1 ) where (-1,-1) is the bottom left
        Returns: A Vector2i of which square you clicked on in the grid or null if the mouse is outside the grid
        Problem : How do I get the size of each square after the projection matrix is applied?
        Solution : I am multiplying the vertices by the projectionMatrix in the line shader,
            So I can just divide the worldCoord by the projectionMatrix
            LinePosReal = LinePos * projectionMatrix
            worldCoord / projectionMatrix = ??
         */

        Vector2f worldCoord = new Vector2f(normalizedMouseCoord.x * -Consts.GRID_Z, normalizedMouseCoord.y * -Consts.GRID_Z / 2);
//        Translate bottom left to (0,0)
        float translatedX = worldCoord.x - x;
        float translatedY = worldCoord.y - y;

//        Scale by row/col size
        float scaledX = translatedX / getDx();
        float scaledY = translatedY / getDy();
        return new Vector2i((int) (scaledX), (int)(scaledY));
    }

    public static Vector2f[] calculateGridLines(float x, float y, float width, float height, int numCols, int numRows) {
        int numPoints = (numCols + numRows + 2) * 2;
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
