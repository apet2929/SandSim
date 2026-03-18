package game;

import com.apet2929.engine.MouseInput;
import com.apet2929.engine.RenderManager;
import com.apet2929.engine.WindowManager;
import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.engine.model.Grid;
import com.apet2929.game.SandSim;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSandSim {

    @Test
    public void testSelectedParticleType() {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        SandSim sim = new SandSim(rm, window, ol);
        EasyMock.expect(window.isKeyPressed(EasyMock.anyInt())).andReturn(false).anyTimes();
        boolean[] keysPressed = {false, false, true };
        EasyMock.expect(window.getNumbersPressed()).andReturn(keysPressed);
        EasyMock.expect(mi.isLeftButtonPressed()).andReturn(false).anyTimes();

        EasyMock.replay(rm, window, ol);
        sim.input(mi);

        EasyMock.verify(rm, window, ol);
        assertEquals(2, sim.selectedParticleType);
    }

    private World world;

    @Test
    void testSpawnParticlePlacesCorrectType() {
        Grid grid = new Grid(1, 10, 10);
        world = new World(grid);

        world.spawnParticle(ParticleType.SAND, 4, 4);

        assertEquals(ParticleType.SAND, world.getAt(4, 4).getType());
    }

    @Test
    void testSwapParticles() {
        Grid grid = new Grid(1, 10, 10);
        world = new World(grid);

        Particle p1 = ParticleType.SAND.createParticleByMatrix(1, 1);
        Particle p2 = ParticleType.WATER.createParticleByMatrix(2, 2);

        world.setAt(1, 1, p1);
        world.setAt(2, 2, p2);
        world.swapParticles(1, 1, 2, 2);

        assertEquals(p1, world.getAt(2, 2));
        assertEquals(p2, world.getAt(1, 1));
    }

    @Test
    void testMoveDown() {
        Grid grid = new Grid(1, 10, 10);
        world = new World(grid);

        Particle p = ParticleType.SAND.createParticleByMatrix(5, 5);
        world.setAt(5, 5, p);
        world.moveDown(new org.joml.Vector2i(5, 5));

        assertEquals(p, world.getAt(5, 4));
    }

    @Test
    void testMoveDownAtBottom() {
        Grid grid = new Grid(1, 10, 10);
        world = new World(grid);

        Particle p = ParticleType.SAND.createParticleByMatrix(5, 0);
        world.setAt(5, 0, p);
        world.moveDown(new org.joml.Vector2i(5, 0));

        assertEquals(p, world.getAt(5, 0));
    }

    @Test
    void testSwapParticle() {
        Grid grid = new Grid(1, 10, 10);
        world = new World(grid);

        Particle p1 = ParticleType.SAND.createParticleByMatrix(5, 5);
        Particle p2 = ParticleType.WATER.createParticleByMatrix(5, 4);
        world.setAt(5, 5, p1);
        world.setAt(5, 4, p2);

        world.swapParticles(5, 5, 5, 4);

        assertEquals(p2, world.getAt(5, 5));
        assertEquals(p1, world.getAt(5, 4));
    }

}
