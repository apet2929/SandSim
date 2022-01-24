package com.apet2929.game;

import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.particles.EmptyParticle;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleLoader;
import com.apet2929.game.particles.ParticleType;
import com.apet2929.game.particles.liquid.Water;
import com.apet2929.game.particles.solid.Sand;
import org.joml.Vector2i;
import org.joml.Vector3f;
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
    int brushSize = 5;
    boolean debug = false;
    float cameraMoveSpeed = 100f;

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
        float delta = EngineManager.getDeltaTime() * 1000;
//        System.out.println("delta = " + delta);
        if(window.isKeyPressed(GLFW.GLFW_KEY_SLASH))
            Consts.GRID_Z += cameraMoveSpeed * delta;
        if(window.isKeyPressed(GLFW.GLFW_KEY_PERIOD))
            Consts.GRID_Z -= cameraMoveSpeed * delta;

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT))
            grid.incPos(cameraMoveSpeed * delta, 0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
            grid.incPos(-cameraMoveSpeed * delta, 0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP))
            grid.incPos(0, -cameraMoveSpeed * delta);
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            grid.incPos(0, cameraMoveSpeed * delta);

        if(window.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
            debug = true;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            addParticles(brushSize * 10, mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
        }

        boolean[] numKeys = window.getNumbersPressed();
        for (int i = 0; i < numKeys.length; i++) {
            if(numKeys[i]){
                selectedParticleType = i;
                System.out.println("i = " + i);
            }
        }



        if(mouseInput.isLeftButtonPressed()) {
            addParticles(brushSize, mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
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

    boolean shouldDrawLines() {
        float minColSize = window.getWidth() / 50.0f;
        float minRowSize = window.getWidth() / 50.0f;
        float colSizePixels, rowSizePixels;
        float x = window.getWidth() / (float) Consts.NUM_COLS_GRID;
        float y = window.getWidth() / (float) Consts.NUM_ROWS_GRID;
        colSizePixels = x / -Consts.GRID_Z;
        rowSizePixels = y / -Consts.GRID_Z;
        return colSizePixels > minColSize && rowSizePixels > minRowSize;
    }

    void addParticles(int brushSize, Vector3f cursorPos) {
        Vector2i gridPos = grid.worldToGridCoordinates(cursorPos);
        int x0 = gridPos.x();
        int y0 = gridPos.y();
        if(brushSize == 1) world.setAt(x0, y0, fromSelectedType(x0, y0));
        Particle particle;
        int x, y;
        for (int i = -brushSize/2; i < brushSize/2; i++) {
            for (int j = -brushSize/2; j < brushSize/2; j++) {
                x = x0 + i;
                y = y0 + j;
                particle = fromSelectedType(x, y);
                spawnParticle(particle, x, y);

            }
        }

    }

    void spawnParticle(Particle particle, int x, int y) {
        Particle replaced = world.getAt(x, y);
        if(replaced != null && replaced.getType() != particle.getType()){
            world.setAt(x, y, particle);
        }
    }

    Particle fromSelectedType(int x, int y) {
        return switch (selectedParticleType) {
            case 1 -> ParticleType.SAND.createParticleByMatrix(x, y);
            case 2 -> ParticleType.WATER.createParticleByMatrix(x, y);
            case 3 -> ParticleType.SMOKE.createParticleByMatrix(x, y);
            case 0 -> ParticleType.EMPTYPARTICLE.createParticleByMatrix(x, y);
            default -> ParticleType.EMPTYPARTICLE.createParticleByMatrix(x, y);
        };
    }
}
