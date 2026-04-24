package com.apet2929.game.particles.liquid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.joml.Vector2i;

import java.util.ArrayList;

public class Fire extends Liquid{
    public static final double smokeRate = 2.0/Consts.FRAMERATE;
    public static final double lifeSpan = 1.0/(Consts.FRAMERATE);

    public Fire(int x, int y) {
        super(x, y, ParticleType.FIRE);
        this.dispersion = 10.0f;
    }

    @Override
    public void update(World world) {
        if(Math.random() <= smokeRate) {
            Vector2i rightNeighbor = new Vector2i(getGridX(), getGridY() + 1);
            if (world.posInGridRange(rightNeighbor) && world.getAt(getGridX(), getGridY() + 1).isEmpty()) {
                world.spawnParticle(ParticleType.SMOKE, getGridX(), getGridY() + 1);
            }
        }
        ArrayList<Particle> neighbors = getNeighbors(world);
        boolean flammableNeighbor = false;
        for(Particle p : neighbors){
            if(p.getType() == ParticleType.WATER){
                world.spawnParticle(ParticleType.EMPTYPARTICLE, this.getGridX(), this.getGridY());
            }
            if(p.getType() == ParticleType.OIL){
                flammableNeighbor = true;
                if(Math.random() <= 0.01){
                    world.spawnParticle(ParticleType.FIRE, p.getGridX(), p.getGridY());
                }
            }
            if(p.getType() == ParticleType.PLANT){
                flammableNeighbor = true;
                if(Math.random() <= 0.002){
                    world.spawnParticle(ParticleType.FIRE, p.getGridX(), p.getGridY());
                }
            }
        }

        if(!flammableNeighbor) super.update(world);
        if(!flammableNeighbor && Math.random() <= lifeSpan){
            world.spawnParticle(ParticleType.EMPTYPARTICLE, this.getGridX(), this.getGridY());
        }

    }
}

