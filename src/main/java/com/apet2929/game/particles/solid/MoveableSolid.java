package com.apet2929.game.particles.solid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public abstract class MoveableSolid extends Particle {
    public MoveableSolid(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canSwap(ParticleType.MatterType type) {
        return (type == ParticleType.MatterType.GAS || type == ParticleType.MatterType.LIQUID || type == ParticleType.MatterType.EMPTY);
    }

    @Override
    public void update(World world) {
        if(canSwap(world.getAt(getGridX(), getGridY()-1))) {
            world.swapParticlesInc(this, 0, -1);
        }
        else if(canSwap(world.getAt(getGridX()-1, getGridY()-1))) {
            world.swapParticlesInc(this, -1, -1);
        }
        else if(world.posInGridRange(getGridX()+1, getGridY()-1)) {
            world.swapParticlesInc(this, 1, -1);
        }
    }


}
