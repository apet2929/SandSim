package com.apet2929.game;


import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.engine.utils.Consts;
import com.apet2929.engine.utils.Utils;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.SandParticle;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class SandSim implements ILogic {

//    TODO : Create World class that holds the list of particles

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;

    private Grid grid;
    private World world;

    private Particle testParticle;
    private Model sandModel;

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

        Vector2f[] lines = Grid.calculateGridLines(-1.0f, -1.0f, Consts.GRID_WIDTH, Consts.GRID_HEIGHT, Consts.NUM_COLS_GRID, Consts.NUM_ROWS_GRID);
        System.out.println("lines[0] = " + lines[0]);
        System.out.println("lines[1] = " + lines[1]);

//        System.out.println("lines = " + Arrays.toString(lines));
        grid = loader.loadGrid(lines);
        grid.init(-1.0f, -1.0f, 2.0f, 2.0f, 20, 20);

        world = new World(grid);
        sandModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
        sandModel.setTexture(assetCache.loadTexture("Sand"));
        testParticle = new SandParticle(sandModel);

        world.setAt(0, grid.getNumRows()-1, testParticle);
    }

    @Override
    public void input(MouseInput mouseInput) {
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            Consts.GRID_Z += 0.05f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            Consts.GRID_Z -= 0.05f;
//
//        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
//            particleX--;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
//            particleX++;

        if(mouseInput.isLeftButtonPressed()) {
//            System.out.println("mouseInput = " + mouseInput.getPos());
//            TODO : Fix mouse to grid coordinates
//            Probably has something to do with the projectionMatrix?
            /*
            The line position in pixels is calculated by openGL using the projectionMatrix
            The lines' x, y, and z values are known.
            So I need a way to go backwards I guess?

             */

            SandParticle particle = new SandParticle(sandModel);
            Vector2i gridPos = grid.worldToGridCoordinates(mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
            world.setAt(gridPos, particle);

            System.out.println("mouseInput = " + mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));

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
