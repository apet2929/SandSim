package com.apet2929.game.particles;

import com.apet2929.game.World;

public class EmptyParticle extends Particle{
    public EmptyParticle(int x, int y) {
        super(x, y);

    }

    @Override
    public void update(World world) {}
}
