package com.apet2929.game.particles.solid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public class Crab extends MoveableSolid {
    boolean headingRight;
    int moveTimer;
    public boolean flipSprite;
    static final int TIME_BETWEEN_MOVES = 20;
    static final float BURROW_UP_CHANCE = 0.1f;
    public Crab(int x, int y) {
        super(x, y, ParticleType.CRAB);
        headingRight = Math.random() > 0.5f;
        flipSprite = false;
    }

    @Override
    public void update(World world) {
        // select a direction and head in that direction until it cant
        // fall
        // else try to diagonally move up
        // else try to move in desired direction
        if(debugCountdown >= 0){
            if(debugCountdown == 0) {
                System.out.println(this);
            }
            debugCountdown -= 1;
        }

        int directionX = headingRight ? 1 : -1;
        boolean moved = fallDown(world);

        if(moved) {
            moveTimer = TIME_BETWEEN_MOVES;
            return;
        }

        moveTimer--;

        if(moveTimer == 0) {
            // there is ground beneath us
            Particle diagonalDown = world.getAt(getGridX() + directionX, getGridY() - 1);
            Particle sideways = world.getAt(getGridX() + directionX, getGridY());
            Particle diagonalUp = world.getAt(getGridX() + directionX, getGridY() + 1); // can step up 1 space
            Particle up = world.getAt(getGridX(), getGridY()+1);
            if (canSwap(diagonalDown) && canSwap(sideways)) {
                this.doMove(world, directionX, -1);
            }
            else if (canSwap(sideways)) this.doMove(world, directionX, 0);
            else if (canSwap(diagonalUp)) this.doMove(world, directionX, 1);
            else if(up.getType() == ParticleType.SAND){
                double roll = Math.random();
                if(roll < BURROW_UP_CHANCE) {
                    this.doMove(world, 0, 1);
                }
            }
            else {
                headingRight = !headingRight;
            }
            moveTimer = TIME_BETWEEN_MOVES;
        }
    }

    private void doMove(World world, int incX, int incY) {
        world.swapParticles(this, getGridX() + incX, getGridY() + incY);
        this.flipSprite = !flipSprite;
    }
}
