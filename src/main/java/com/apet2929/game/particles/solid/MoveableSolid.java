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

        /*
        I want my sand particle to fall down when:
        - There is space below
        - The particle below is also falling

        I want my particle to fall diagonally when:
        - There is a particle below
        - The particle directly below is not falling
        - There is space diagonally down

         */

        boolean moved = fallDown(world);
        if(!moved) fallDiagonally(world);
    }

    public boolean isFalling(World world) {
        if(getGridY() == 0) return false;

        Particle p = world.getAt(getGridX(), getGridY() - 1);
        if(p.getType().matterType == ParticleType.MatterType.EMPTY || p.getType().matterType == ParticleType.MatterType.GAS) return true;
        else if(p instanceof MoveableSolid) {
            return ((MoveableSolid) p).falling;
        } else {
            return p.isLiquid();
        }
    }

    private boolean fallDown(World world) {
        this.falling = this.isFalling(world);
        if(this.falling) {
            addGravity();
            fallWithGravity(world);
            return true;
        }
        return false;
    }

    private void fallDiagonally(World world) {
        int directionBias = world.getDirectionBias();
        boolean firstTry = tryFallDiagonally(world, directionBias);
        if(!firstTry) tryFallDiagonally(world, -directionBias);
    }

    private boolean tryFallDiagonally(World world, int directionBias) {
        if (canSwap(world.getAt(getGridX() + directionBias, getGridY() - 1))) {
            world.swapParticles(this, getGridX() + directionBias, getGridY() - 1);
            return true;
        }
        return false;
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

    private boolean fallingGravityStep(World world) {
        // Returns whether the falling should continue
        Particle p = getParticleBelow(world);
        if(p == null) return false;
        else if(p.isLiquid()){
            this.falling = false;
            fallThroughLiquid(world);
            return false;
        } else if(p.isSolid()) {
            this.falling = false;
            return false;
        }
        else if(p.isEmpty() || p.isGas()) {
            this.falling = true;
            world.swapParticles(this, getGridX(), getGridY()-1);
            return true;
        }
        return false;
    }

    private void addGravity(){
        this.velocity.y += Consts.GRAVITY * Consts.PARTICLE_DELTA;
    }







    void fallThroughLiquid(World world) {
        world.swapParticles(this, getGridX(), getGridY()-1);
    }



}
