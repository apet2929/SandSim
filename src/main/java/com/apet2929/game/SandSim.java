package com.apet2929.game;


import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.SandParticle;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class SandSim implements ILogic {

//    TODO : Create World class that holds the list of particles

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;

    private Entity entity;
    private Grid grid;
    private World world;
//    private Particle[][] world;

    private Particle testParticle;
    private int particleX, particleY;

    public SandSim(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();

    }

    @Override
    public void init() throws Exception {

        renderer.init();
        assetCache = new AssetCache(loader);

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

        Vector3f[] lines = Grid.calculateGridLines(-1.0f, -1.0f, 2.0f, 2.0f, 20, 20);
        grid = loader.loadGrid(lines);
        grid.init(-1.0f, -1.0f, 2.0f, 2.0f, 20, 20);

        world = new World(grid);
        Model particleModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
        particleModel.setTexture(assetCache.loadTexture("Sand"));
        testParticle = new SandParticle(particleModel);

        world.setAt(0, grid.getNumRows()-1, testParticle);
    }

    @Override
    public void input(MouseInput mouseInput) {
//        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
//            particleY++;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
//            particleY--;
//
//        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
//            particleX--;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
//            particleX++;

        if(window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            entity.incRotation(-0.5f, 0, 0f);
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            entity.incRotation(0.5f, 0f, 0f);

        if(mouseInput.isLeftButtonPressed()) {
            System.out.println("mouseInput = " + mouseInput.getPos());
        }
    }

    @Override
    public void update() {
        if(ParticleTimer.update()) {
            world.update();
        }
    }

    @Override
    public void render() {
        renderer.clear();
        renderer.drawLines(grid.getId(), grid.getNumLines());
        renderer.beginRender();
        world.render(renderer);
        renderer.endRender();

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }




}
