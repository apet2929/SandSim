package com.apet2929.game;

import com.apet2929.engine.model.Grid;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.easymock.EasyMock;
import org.joml.Vector2i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @Test
    void testUpdate() {
        Grid grid = EasyMock.mock(Grid.class);
        EasyMock.expect(grid.getNumCols()).andReturn(2);
        EasyMock.expect(grid.getNumRows()).andReturn(2);
        EasyMock.replay(grid);
        World world = new World(grid);
        EasyMock.verify(grid);
        EasyMock.reset(grid);

        Particle fakeParticle = EasyMock.mock(Particle.class);
        fakeParticle.setPositionByWorld(0,0);
        EasyMock.expectLastCall();
        EasyMock.expect(fakeParticle.getType()).andReturn(ParticleType.SAND);
        fakeParticle.update(world);
        EasyMock.expectLastCall().once();
        EasyMock.replay(grid,fakeParticle);
        world.setAt(new Vector2i(0,0), fakeParticle);
        world.update();
        EasyMock.verify(grid,fakeParticle);
    }
}