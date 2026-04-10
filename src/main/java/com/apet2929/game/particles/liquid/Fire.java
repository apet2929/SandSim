package com.apet2929.game.particles.liquid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.joml.Vector2i;

import java.util.ArrayList;

public class Fire extends Liquid{
    public static final double smokeRate = 10.0/Consts.FRAMERATE;
    public static final double lifeSpan = 1.0/(Consts.FRAMERATE);

    public Fire(int x, int y) {
        super(x, y, ParticleType.FIRE);
        this.dispersion = 10.0f;
    }

    @Override
    public void update(World world) {
        super.update(world);
        if(Math.random() <= smokeRate){
            Vector2i rightNeighbor = new Vector2i(getGridX(), getGridY()+1);
            if(world.posInGridRange(rightNeighbor) && world.getAt(getGridX(), getGridY()+1).isEmpty()) {
                world.spawnParticle(ParticleType.SMOKE, getGridX(), getGridY()+1);
            }
        }
        if(Math.random() <= lifeSpan){
            world.spawnParticle(ParticleType.EMPTYPARTICLE, this.getGridX(), this.getGridY());
        }
        ArrayList<Particle> neighbors = getNeighbors(world);
        for(Particle p : neighbors){
            if(p.getType() == ParticleType.WATER){
                world.spawnParticle(ParticleType.EMPTYPARTICLE, this.getGridX(), this.getGridY());
            }
            if(p.getType() == ParticleType.OIL){
                if(Math.random() <= 0.01){
                    world.spawnParticle(ParticleType.FIRE, p.getGridX(), p.getGridY());
                }
            }
            if(p.getType() == ParticleType.PLANT){
                if(Math.random() <= 0.001){
                    world.spawnParticle(ParticleType.FIRE, p.getGridX(), p.getGridY());
                }
            }
        }
    }
}

