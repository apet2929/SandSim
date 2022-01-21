package com.apet2929.game.particles.solid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public abstract class MoveableSolid extends Particle {

    public boolean falling = false;

    public MoveableSolid(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canSwap(ParticleType.MatterType type) {
        return (type == ParticleType.MatterType.GAS || type == ParticleType.MatterType.LIQUID || type == ParticleType.MatterType.EMPTY);
    }

    @Override
    public void update(World world) {
        this.velocity.add(0, Consts.GRAVITY * Consts.PARTICLE_DELTA);
        this.falling = isFalling(world);
        int dir = world.getDirectionBias();
        if(this.falling)
            fallWithGravity(world);
        else {
            if (canSwap(world.getAt(getGridX() - dir, getGridY() - 1))) {
                world.swapParticlesInc(this, -dir, -1);
            } else if (world.posInGridRange(getGridX() + dir, getGridY() - 1)) {
                world.swapParticlesInc(this, dir, -1);
            }
        }
    }

    private boolean fallWithGravity(World world) {
        /*
        Checks each position along the path until it reaches a particle, then swaps 1 place before
         */
        boolean moved = false;
        int x0 = getGridX();
        int y0 = getGridY();
        int distY = Math.abs(Math.round(this.velocity.y));
        for (int i = 1; i <= distY; i++) {
            if(world.posInGridRange(x0, y0 - i)) {
                if(world.getAt(x0, y0 - i).isEmpty() || world.getAt(x0, y0-i).isGas()) {
                    this.falling = true;
                    world.swapParticles(this, x0, y0 - i);
                    moved = true;
                } else { // particle below isn't empty, we hit the ground
                    if(!world.getAt(x0, y0-i).isLiquid()) this.falling = false;
                    return moved;
                }
            }
        }
        return moved;
    }

    private boolean isFalling(World world) {
        if(getGridY() == 0) return false;
        ParticleType temp = world.getAt(getGridX(), getGridY() - 1).getType();
        return temp.matterType == ParticleType.MatterType.EMPTY || temp.matterType == ParticleType.MatterType.GAS;
    }

}
