package com.apet2929.game.particles.liquid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import com.apet2929.game.particles.solid.MoveableSolid;

public abstract class Liquid extends Particle {
    protected float dispersion;
    public boolean falling;
    public Liquid(int x, int y) {
        super(x, y);
        this.falling = true;
    }

    @Override
    public void update(World world) {

        int dir = world.getDirectionBias();
        this.falling = updateIsFalling(world);
        boolean moved = fallDown(world);
        if(!moved){
            moved = fallDiagonally(world, dir);
        }
        if(!moved) {
            disperseHorizontally(world, dir);
        }

    }


    @Override
    public boolean canSwap(ParticleType.MatterType t) {
        return (t == ParticleType.MatterType.EMPTY || t == ParticleType.MatterType.GAS);
    }

    private boolean fallDown(World world) {
        this.falling = updateIsFalling(world);

        if(this.falling) {
            this.velocity.add(0, Consts.GRAVITY * Consts.PARTICLE_DELTA);
            fallWithGravity(world);
            return true;
        } else {
            return false;
        }
    }

    private boolean fallDiagonally(World world, int directionBias) {
        if (canSwap(world.getAt(getGridX() - directionBias, getGridY() - 1))) {
            world.swapParticles(this, getGridX() - directionBias, getGridY() - 1);
            return true;
        } else if (canSwap(world.getAt(getGridX() + directionBias, getGridY() - 1))) {
            world.swapParticles(this, getGridX() + directionBias, getGridY() - 1);
            return true;
        }
        return false;
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

    public boolean isFalling() {
        return falling;
    }

    private boolean updateIsFalling(World world) {
        if(getGridY() == 0) return false;

        Particle p = world.getAt(getGridX(), getGridY() - 1);
        if(p.getType().matterType == ParticleType.MatterType.EMPTY || p.getType().matterType == ParticleType.MatterType.GAS) return true;
        else if(p instanceof MoveableSolid) {
            return ((MoveableSolid) p).falling;
        } else if(p instanceof Liquid){
            return ((Liquid) p).falling;
        }
        return false;
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
