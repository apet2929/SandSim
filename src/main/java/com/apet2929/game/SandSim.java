package com.apet2929.game;

import com.apet2929.engine.*;
import com.apet2929.engine.model.*;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleLoader;
import com.apet2929.game.particles.ParticleType;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.lang.Math;
import java.util.List;

public class SandSim implements ILogic {
    public static final float cameraZoomSpeed = 0.1f;

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

    Grid grid;
    World world;
    Model particleModel;
    Camera cam;

    public ParticleType selectedParticleType = ParticleType.SAND;
    public int brushSize = 5;
    public boolean debug = false;
    public float cameraMoveSpeed = 0.01f;

    public SandSim() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
    }

    public SandSim(RenderManager rm, WindowManager window, ObjectLoader loader, Camera cam){
        this.renderer = rm;
        this.window = window;
        this.loader = loader;
        this.cam = cam;
    }

    @Override
    public void init() throws Exception {
        this.cam = new Camera();
        this.renderer.setCamera(cam);
        assetCache = new AssetCache(loader);

        grid = loader.loadGrid(Consts.NUM_COLS_GRID, Consts.NUM_ROWS_GRID);
        world = new World(grid);
        renderer.init();

        initParticleModel();
        initParticleTypes();
        cam.move(new Vector2f(grid.getNumCols()/2f, grid.getNumRows()/2f));
    }

    @Override
    public void input(MouseInput mouseInput) {
        float delta = 16.0f; //EngineManager.getDeltaTime() * 1000;

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
            cam.move(new Vector2f(-cameraMoveSpeed * delta, 0));
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
            cam.move(new Vector2f(cameraMoveSpeed * delta, 0));
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP))
            cam.move(new Vector2f(0, -cameraMoveSpeed * delta));
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            cam.move(new Vector2f(0, cameraMoveSpeed * delta));
        if(window.isKeyPressed(GLFW.GLFW_KEY_R))
            world.fillRandomly();

        if(window.isKeyPressed(GLFW.GLFW_KEY_R)) world.fillRandomly();

        if(window.isKeyPressed(GLFW.GLFW_KEY_E))
            cam.rotate(0.01f * delta);
        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            cam.rotate(-0.01f * delta);


        if(window.isKeyPressed(GLFW.GLFW_KEY_PERIOD)) {
            System.out.println("cam.getPosition().z = " + cam.getPosition().z);
            cam.move(new Vector3f(0,0,cameraZoomSpeed*delta));
        }
        else if(window.isKeyPressed(GLFW.GLFW_KEY_SLASH)) {
            cam.move(new Vector3f(0,0, -cameraZoomSpeed *delta));
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            addParticles(brushSize * 10, mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
        }


        boolean[] numKeys = window.getNumbersPressed();
        for (int i = 0; i < numKeys.length; i++) {
            if(numKeys[i]){
                selectedParticleType = particleTypeFromNumber(i);
                System.out.println("selected = " + selectedParticleType);
            }
        }

        if(mouseInput.isLeftButtonPressed()) {
//            Vector2d mp = mouseInput.getPos();
            addParticles(brushSize, mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
        }
    }

    @Override
    public void update() {
//        this.cam.rotate(0.1f);
        if(ParticleTimer.update()) {
            world.update();
        }
    }

    @Override
    public void render() {
        renderer.clear();
        if(shouldDrawLines()) renderer.drawLines(grid.getLinesId(), grid.getNumLines());
        else renderer.drawLines(grid.getOutlineId(), grid.getOutlineNumLines());
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
        return cam.getPosition().z < 50.0f;
    }

    Vector2i gridPos(Vector2f mousePos) {
        // https://antongerdelan.net//opengl/raycasting.html
        Vector4f ray_clip = new Vector4f(mousePos, -1, 1);

        Vector4f ray_eye = new Vector4f();
        Matrix4f invertedProjection = new Matrix4f();
        window.getProjectionMatrix().invert(invertedProjection);
        ray_clip.mul(invertedProjection, ray_eye);

        Vector4f ray_world = new Vector4f();
        Matrix4f invertedView = new Matrix4f();
        cam.getViewMatrix().invert(invertedView);
        ray_eye.mul(invertedView, ray_world);
        Vector3f rayWorld = new Vector3f(ray_eye.x, ray_eye.y, ray_eye.z);
        rayWorld.normalize();

        Vector3f result = ray_plane_intersect(cam.getPosition(), rayWorld);
        return new Vector2i(Math.round(result.x), Math.round(result.y));
    }

    Vector3f ray_plane_intersect(Vector3f rayOrigin, Vector3f rayDirection) {
        Vector3f planePos = new Vector3f(0,0,0);
        Vector3f planeNormal = new Vector3f(0,0,1);
        float numerator = planePos.sub(rayOrigin).dot(planeNormal);
        float denom = rayDirection.dot(planeNormal);
        if(denom == 0) {
            return null;
        }
        float t = numerator / denom;
        return rayOrigin.add(rayDirection.mul(t));
    }


    void addParticles(int brushSize, Vector3f cursorPos) {
        Vector2i gridPos = gridPos(new Vector2f(cursorPos.x, cursorPos.y));
        int x0 = gridPos.x();
        int y0 = gridPos.y();
        System.out.println("(" + cursorPos.x + "," + cursorPos.y + ") =>" + "(" + x0 + "," + y0 + ")");
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
        return selectedParticleType.createParticleByMatrix(x, y);
    }

    private ParticleType particleTypeFromNumber(int i) {
        switch (i) {
            case 1: return ParticleType.SAND;
            case 2: return ParticleType.WATER;
            case 3: return ParticleType.SMOKE;
            case 4: return ParticleType.STONE;
            case 5: return ParticleType.FIRE;
            case 0: return ParticleType.EMPTYPARTICLE;
            default: return ParticleType.EMPTYPARTICLE;
        }
    }
}
