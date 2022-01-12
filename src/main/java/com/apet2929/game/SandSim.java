package com.apet2929.game;


import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

import java.io.InputStream;
import java.util.Arrays;

public class SandSim implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Entity entity;
    private Vector3f[] lines;
    private int gridId;

//    TODO : Figure out why color isn't working
    final int numCols = 40;
    final int numRows = 40;

    public SandSim(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();

    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,    //  Top left vertex
                -0.5f, -0.5f, 0f,   //  Bottom left vertex
                0.5f, -0.5f, 0f,    //  Bottom right vertex
                0.5f, 0.5f, 0f,     //  Top right
//                -0.5f, 0.5f, 0f     //  Top left
//                0.5f, -0.5f, 0f,    //  Bottom right vertex
        };



        int[] indices = {
                0,1,3,
                3,1,2
        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        Model model = loader.loadModel(vertices, textureCoords, indices);
        model.setTexture(new Texture(loader.loadTexture("textures/tree.png")));
        entity = new Entity(model);
        entity.setPos(0,0,-2);

        Vector3f lineStart = new Vector3f(-0.5f, -0.5f, 1);
        Vector3f lineEnd = new Vector3f(0.5f, 0.5f, 1);
        Vector3f lineColStart = new Vector3f(0.5f, 1.0f, 0.0f);
        Vector3f lineColEnd = new Vector3f(0.5f, 0.0f, 0.0f);
//        line = loader.loadLine(lineStart, lineEnd, lineColStart, lineColEnd);

        lines = calculateGridLines();
        gridId = loader.loadLines(lines);
    }

    @Override
    public void input() {
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            entity.inc_pos(0,0,-0.05f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            entity.inc_pos(0,0,0.05f);

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            entity.inc_pos(-0.05f, 0, 0f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
            entity.inc_pos(0.05f, 0f, 0f);

        if(window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            entity.incRotation(-0.5f, 0, 0f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            entity.incRotation(0.5f, 0f, 0f);


        if(window.isKeyPressed(GLFW.GLFW_KEY_ENTER))
            System.out.println(direction);
    }

    @Override
    public void update(MouseInput mouseInput) {
        color += direction * 0.001f;

        if(color > 1){
            color = 1.0f;
        }
        else if(color <= 0) {
            color = 0.0f;
        }

    }

    @Override
    public void render() {
        renderer.render(entity);

        renderer.drawLines(gridId, lines.length);


//        renderer.beginRender();
//        renderer.setWindowUniform();
//        renderer.renderEntity(entity);
//        renderer.endRender();

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

    private Vector3f[] calculateGridLines() {
        int numPoints = (numCols + numRows) * 2;
        lines = new Vector3f[numPoints];

        float x = -1.0f;
        float y = -1.4f;
        float width = 2.0f;
        float height = 2.5f;

        float dx = width / numCols;
        float dy = height / numRows;
        Vector3f startPoint = new Vector3f();
        Vector3f endPoint = new Vector3f();
        endPoint.z = 0;
        startPoint.z = 0;
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
