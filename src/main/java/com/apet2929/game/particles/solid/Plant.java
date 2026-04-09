package com.apet2929.game.particles.solid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

import java.util.ArrayList;

public class Plant extends MoveableSolid{
    public Plant(int x, int y) {
        super(x, y, ParticleType.PLANT);
    }

    @Override
    public void update(World world) {
        super.update(world);

        ArrayList<Particle> particles = super.getNeighbors(world);
        for (Particle p : particles) {
            if (p.getType() == ParticleType.SAND) {
                if(Math.random() < 0.001) {
                    world.spawnParticle(ParticleType.PLANT, getGridX(), p.getGridY());
                }
            }
        }
    }
}
