package com.apet2929.game;

import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleLoader;
import com.apet2929.game.particles.ParticleType;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SandSim implements ILogic {

    /*
    OUTLINE :
        Particle:
            - Has a type determined by the class
            - All particles inherit from Solid, Liquid, or Gas
            - Particles cannot change their own positions
            - Are passed the World in the update call
            - Contains a texture
        World holds a 2d array of Particles
            - Mediates interactions between particles, any time a particle changes position, it has to go through the World class
            - Stores all the Particles into a temporary 1D array for updating
        RenderManager:
            Takes in a Model and a Vector3f of the position of the particle
        For each particle to be rendered:
            - There is a shared model between all particles
            - The model's texture is set to the current particle's texture
            - That model is passed to the RenderManager with the particle's position

     Where to store the shared Particle Model?
     */

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;

    private Grid grid;
    private World world;
    private Model particleModel;

    int selectedParticleType = 1;

    public SandSim() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        assetCache = new AssetCache(loader);

        grid = loader.loadGrid(Consts.NUM_COLS_GRID, Consts.NUM_ROWS_GRID);
        world = new World(grid);
        renderer.init();

        initParticleModel();
        initParticleTypes();
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

        if(mouseInput.isLeftButtonPressed()) {
//            Particle particle = switch (selectedParticleType) {
//                case 1 -> new SandParticle(particleLoader.getType(ParticleType.SAND));
//                case 2 -> new WaterParticle(particleLoader.getType(ParticleType.WATER));
//                case 3 -> new SmokeParticle(particleLoader.getType(ParticleType.SMOKE));
//                default -> new EmptyParticle();
//            };
        Vector2i gridPos = grid.worldToGridCoordinates(mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
        world.spawnParticle(ParticleType.SAND, gridPos.x, gridPos.y);
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
        if(shouldDrawLines())
            renderer.drawLines(grid.getId(), grid.getNumLines());
        renderer.beginRender();
        world.render(renderer, particleModel);
        renderer.endRender();

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

    private void initParticleModel() {
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
        particleModel = loader.loadModel(grid.getScaledVertices(), textureCoords, indices);
    }
    private void initParticleTypes() {
        List<ParticleType> solids = ParticleType.getSolids();
        for (ParticleType solid : solids) {
            ParticleLoader.initParticleType(solid, assetCache);
        }

        List<ParticleType> liquids = ParticleType.getLiquids();
        for (ParticleType liquid: liquids) {
            ParticleLoader.initParticleType(liquid, assetCache);
        }

        List<ParticleType> gasses = ParticleType.getGasses();
        for (ParticleType gas: gasses) {
            ParticleLoader.initParticleType(gas, assetCache);
        }
    }

    private boolean shouldDrawLines() {
        float minColSize = window.getWidth() / 50.0f;
        float minRowSize = window.getWidth() / 50.0f;
        float colSizePixels, rowSizePixels;
        float x = window.getWidth() / (float) Consts.NUM_COLS_GRID;
        float y = window.getWidth() / (float) Consts.NUM_ROWS_GRID;
        colSizePixels = x / -Consts.GRID_Z;
        rowSizePixels = y / -Consts.GRID_Z;
        return colSizePixels > minColSize && rowSizePixels > minRowSize;
    }
}
