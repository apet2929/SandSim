package com.apet2929.game.particles.solid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;

public abstract class MoveableSolid extends Particle {
    public MoveableSolid(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(World world) {
        if(world.posInGridRange(getGridX(), getGridY()-1)) {
            world.swapParticles(getGridX(), getGridY(), getGridX(), getGridY()-1);
        }
    }
}
