package com.apet2929.game;


import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.particles.*;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

public class SandSim implements ILogic {

//    TODO : Create World class that holds the list of particles

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;

    private Grid grid;
    private World world;

    private int selectedParticleType;

    private Model smokeModel;
    private Model sandModel;
    private Model waterModel;

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

//        System.out.println("lines = " + Arrays.toString(lines));
        grid = loader.loadGrid(Consts.NUM_COLS_GRID, Consts.NUM_ROWS_GRID);
        world = new World(grid);
        waterModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
        waterModel.setTexture(assetCache.loadTexture("Water"));
        sandModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
        sandModel.setTexture(assetCache.loadTexture("Sand"));
        selectedParticleType = 1;
        smokeModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
        smokeModel.setTexture(assetCache.loadTexture("Smoke"));
    }

    @Override
    public void input(MouseInput mouseInput) {
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            Consts.GRID_Z += 0.05f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            Consts.GRID_Z -= 0.05f;
        if(window.isKeyPressed(GLFW.GLFW_KEY_1)) {
            selectedParticleType = 1; // sand
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_2)) {
            selectedParticleType = 2; // water
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_3)) {
            selectedParticleType = 3; // smoke
        }
//
//        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
//            particleX--;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
//            particleX++;

        if(mouseInput.isLeftButtonPressed()) {
            Particle particle = switch (selectedParticleType) {
                case 1 -> new SolidParticle(sandModel, ParticleType.SAND);
                case 2 -> new LiquidParticle(waterModel, ParticleType.WATER);
                case 3 -> new GasParticle(smokeModel, ParticleType.SMOKE);
                default -> new EmptyParticle();
            };
            System.out.println("selectedParticleType = " + selectedParticleType);
            Vector3f normPos = mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight());
            System.out.println("normPos = " + normPos);
            Vector2i gridPos = grid.worldToGridCoordinates(mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
            System.out.println("gridPos = " + gridPos);
            world.setAt(gridPos, particle);
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
