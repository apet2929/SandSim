package com.apet2929.game;

import com.apet2929.engine.EngineManager;
import com.apet2929.engine.MouseInput;
import com.apet2929.engine.RenderManager;
import com.apet2929.engine.WindowManager;
import com.apet2929.engine.model.Camera;
import com.apet2929.engine.model.Grid;
import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.game.particles.ParticleType;
import com.apet2929.game.particles.Particle;
import org.easymock.EasyMock;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

class SandSimTest {

    @Test
    public void testSelectedParticleType() {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        Camera cam = new Camera();
        SandSim sim = new SandSim(rm, window, ol, cam);
        EasyMock.expect(window.isKeyPressed(EasyMock.anyInt())).andReturn(false).anyTimes();
        boolean[] keysPressed = {false, false, true };
        EasyMock.expect(window.getNumbersPressed()).andReturn(keysPressed);
        EasyMock.expect(mi.isLeftButtonPressed()).andReturn(false).anyTimes();

        EasyMock.replay(rm, window, ol, mi);
        sim.input(mi);

        EasyMock.verify(rm, window, ol, mi);
        assertEquals(ParticleType.WATER, sim.selectedParticleType);
    }

    @Test
    public void testCameraMovesWhenLeftKeyPressed() throws Exception {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        Camera cam = new Camera();
        SandSim sim = new SandSim(rm, window, ol, cam);

        // Make LEFT key appear pressed, all other key checks return false
        EasyMock.expect(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)).andReturn(true);
        EasyMock.expect(window.isKeyPressed(EasyMock.anyInt())).andReturn(false).anyTimes();
        EasyMock.expect(window.getNumbersPressed()).andReturn(new boolean[10]);
        EasyMock.expect(mi.isLeftButtonPressed()).andReturn(false).anyTimes();

        // Force a non-zero delta time by setting the private EngineManager.lastFrameTime via reflection
        java.lang.reflect.Field lastFrameField = com.apet2929.engine.EngineManager.class.getDeclaredField("lastFrameTime");
        lastFrameField.setAccessible(true);
        // set to 1 second in nanoseconds so getDeltaTime() -> 1.0f
        lastFrameField.setFloat(null, 1000000000f);

        EasyMock.replay(rm, window, ol, mi);
        sim.input(mi);

        EasyMock.verify(rm, window, ol, mi);

        // Camera initial z is 15, x/y start at 0. Expect x moved left by cameraMoveSpeed * delta * -1
        float delta = EngineManager.getDeltaTime() * 1000f;
        float expectedX = -sim.cameraMoveSpeed * delta;
        assertEquals(expectedX, cam.getPosition().x, 0.0001f);
    }

    @Test
    public void testWorldSpawnParticleByType() {
        Grid grid = new Grid(0, 5, 5);
        World world = new World(grid);

        // spawn sand at (2,2)
        world.spawnParticle(ParticleType.SAND, 2, 2);
        assertNotNull(world.getAt(2,2));
        assertEquals(ParticleType.SAND, world.getAt(2,2).getType());
    }

    @Test
    public void testSandSimAddsSelectedParticleType() {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        Camera cam = new Camera();
        SandSim sim = new SandSim(rm, window, ol, cam);

        Grid grid = new Grid(0, 5, 5);
        World world = new World(grid);
        // inject a simple world and grid into the sim
        sim.grid = grid;
        sim.world = world;

        sim.selectedParticleType = ParticleType.WATER;
        // create particle from selected type and spawn into world
        Particle p = sim.fromSelectedType(1,1);
        sim.spawnParticle(p, 1, 1);

        assertNotNull(world.getAt(1,1));
        assertEquals(ParticleType.WATER, world.getAt(1,1).getType());
    }
}