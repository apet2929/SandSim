package com.apet2929.game;


import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.game.particles.Particle;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class SandSim implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;

    private Entity entity;
    private Grid grid;
    private Particle[][] world;

    public SandSim(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();

    }

    @Override
    public void init() throws Exception {
        renderer.init();
        assetCache = new AssetCache(loader);

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
        model.setTexture(assetCache.loadTexture("Sand"));
//        model.setTexture(new Texture(loader.loadTexture("textures/tree.png")));
        entity = new Entity(model);
        entity.setPos(0,0,-2);

        Vector3f[] lines = Grid.calculateGridLines(-1.0f, -1.0f, 2.0f, 2.0f, 20, 20);
        grid = loader.loadGrid(lines);
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
        renderer.clear();
        renderer.drawLines(grid.getId(), grid.getNumLines());
        renderer.beginRender();
        renderer.renderEntity(entity);
        renderer.endRender();

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }


}
