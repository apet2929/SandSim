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
import java.util.Map;

import static java.util.Map.entry;

public class SandSim implements ILogic {
    public static final float cameraZoomSpeed = 30f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private AssetCache assetCache;
    public boolean paused;

    Grid grid;
    World world;
    Model particleModel;
    Camera cam;
    KeyboardManager keyboard;

    public ParticleType selectedParticleType = ParticleType.SAND;
    public int brushSize = 5;
    public boolean debug = false;
    public float baseCameraMoveSpeed = 1f;

    public SandSim() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        keyboard = new KeyboardManager();
    }

    public SandSim(RenderManager rm, WindowManager window, ObjectLoader loader, Camera cam, KeyboardManager keyboard){
        this.renderer = rm;
        this.window = window;
        this.loader = loader;
        this.cam = cam;
        this.keyboard = keyboard;
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
        Particle.DEBUG_TEXTURE = assetCache.loadTexture("DEBUG");
        cam.move(new Vector2f(grid.getNumCols()/2f, grid.getNumRows()/2f));
    }

    @Override
    public void input(MouseInput mouseInput) {
        keyboard.update(window.getWindow());
        float delta = EngineManager.getDeltaTime();
        float cameraMoveSpeed = baseCameraMoveSpeed * cam.getPosition().z;

        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
            cam.move(new Vector2f(-cameraMoveSpeed * delta, 0));
        }
        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
            cam.move(new Vector2f(cameraMoveSpeed * delta, 0));
        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            cam.move(new Vector2f(0, -cameraMoveSpeed * delta));
        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_UP))
            cam.move(new Vector2f(0, cameraMoveSpeed * delta));
        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_R))
            world.fillRandomly();

        // vim keybinds - h=left, l=right, j=up, k=down
        Map<Integer, World.ExpandDirection> expandKeybinds = Map.ofEntries(
                entry(GLFW.GLFW_KEY_H, World.ExpandDirection.LEFT),
                entry(GLFW.GLFW_KEY_L, World.ExpandDirection.RIGHT),
                entry(GLFW.GLFW_KEY_J, World.ExpandDirection.DOWN),
                entry(GLFW.GLFW_KEY_K, World.ExpandDirection.UP)
        );
        for(int keycode : expandKeybinds.keySet()) {
            if(keyboard.isKeyJustReleased(keycode)) {
                grid = world.expand(loader, expandKeybinds.get(keycode), 20);
                break;
            }
        }
        if(keyboard.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)) paused = !paused;

        if(keyboard.isKeyJustReleased(GLFW.GLFW_KEY_R)) world.fillRandomly();

        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_E))
            cam.rotate(0.1f * delta);
        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_Q))
            cam.rotate(-0.1f * delta);


        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_PERIOD)) {
            System.out.println("cam.getPosition().z = " + cam.getPosition().z);
            cam.move(new Vector3f(0,0, SandSim.cameraZoomSpeed *delta));
        }
        else if(keyboard.isKeyPressed(GLFW.GLFW_KEY_SLASH)) {
            cam.move(new Vector3f(0,0, -SandSim.cameraZoomSpeed *delta));
        }

        if(keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            addParticles(brushSize * 10, mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight()));
        }

        boolean[] numKeys = keyboard.getNumbersPressed();
        for (int i = 0; i < numKeys.length; i++) {
            if(numKeys[i]){
                selectedParticleType = particleTypeFromNumber(i);
                System.out.println("selected = " + selectedParticleType);
            }
        }

        if(mouseInput.isLeftButtonPressed()) {
            Vector3f mousePos = mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight());
            if(!paused) addParticles(brushSize, mousePos);
            else inspectParticle(mousePos);
        }
        if(mouseInput.isRightButtonJustReleased()) {
            Vector3f mousePos = mouseInput.getNormalizedMousePos(window.getWidth(), window.getHeight());
            if(!paused) addParticles(1, mousePos);
            else inspectParticle(mousePos);
        }

    }

    private void inspectParticle(Vector3f mousePos) {
        Vector2i gridPos = gridPos(new Vector2f(mousePos.x, mousePos.y));
        Particle particle = world.getAt(gridPos);
        particle.debugSoon();
    }

    @Override
    public void update() {
        if(paused) return;
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
            case 6: return ParticleType.OIL;
            case 7: return ParticleType.PLANT;
            case 8: return ParticleType.CRAB;
            case 0: return ParticleType.EMPTYPARTICLE;
            default: return ParticleType.EMPTYPARTICLE;
        }
    }
}
