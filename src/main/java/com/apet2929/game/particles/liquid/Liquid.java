package com.apet2929.game.particles.liquid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public abstract class Liquid extends Particle {
    protected float dispersion;
    protected boolean falling;
    public Liquid(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(World world) {
        int dir = world.getDirectionBias();
        this.velocity.add(0, Consts.GRAVITY * Consts.PARTICLE_DELTA);

        this.falling = isFalling(world);

        if(this.falling)
            fallWithGravity(world);
        else {
            boolean moved = false;
            if (canSwap(world.getAt(getGridX() - dir, getGridY() - 1))) {
                world.swapParticles(this, getGridX() - dir, getGridY() - 1);
                moved = true;
            } else if (canSwap(world.getAt(getGridX() + dir, getGridY() - 1))) {
                world.swapParticles(this, getGridX() + dir, getGridY() - 1);
                moved = true;
            }

            if (!moved) {
                if (!disperseHorizontally(world, dir))
                    disperseHorizontally(world, dir * -1);
            }
        }

    }


    @Override
    public boolean canSwap(ParticleType.MatterType t) {
        return (t == ParticleType.MatterType.EMPTY || t == ParticleType.MatterType.GAS);
    }

    private boolean disperseHorizontally(World world, int directionBias) {
        /*
        Checks each position along the path until it reaches a particle, then swaps 1 place before
         */
        int x0 = getGridX();
        int y0 = getGridY();
        // If we can't move even once, return false
        if(!this.canSwap(world.getAt(x0+directionBias, y0))) return false;
        for (int i = 1; i <= this.dispersion; i++) {
            int realI = i * directionBias;
            if(world.posInGridRange(x0+realI, y0)) {
                if(world.getAt(x0+realI, y0).isEmpty() || world.getAt(x0+realI, y0).isGas()) {
                    world.swapParticles(this, x0+realI, y0);
                } else { // particle below isn't empty, we hit the ground
                    if(world.getAt(x0+realI, y0).isSolid()) return true;

                }
            }
        }
        return true;
    }

    private boolean fallWithGravity(World world) {
        /*
        Checks each position along the path until it reaches a particle, then swaps 1 place before
         */
        boolean moved = false;
        int distY = Math.abs(Math.round(this.velocity.y));

        for (int i = 0; i < distY; i++) {
            if(!fallingGravityStep(world)) return moved;
            else{
                moved = true;
            }
        }

        return moved;
    }

    private boolean isFalling(World world) {
        if(getGridY() == 0) return false;
        ParticleType temp = world.getAt(getGridX(), getGridY() - 1).getType();
        return temp.matterType == ParticleType.MatterType.EMPTY || temp.matterType == ParticleType.MatterType.GAS;
    }

    boolean fallingGravityStep(World world) {
        // Returns whether the falling should continue
        Particle p = getParticleBelow(world);
        if(p == null || p.isLiquid() || p.isSolid()){
            this.falling = false;
            return false;
        } else if(p.isEmpty() || p.isGas()) {
            this.falling = true;
            world.swapParticles(this, getGridX(), getGridY()-1);
            return true;
        }
        return false;
    }



}
