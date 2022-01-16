package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public class SolidParticle extends Particle{

    public SolidParticle(Model model, ParticleType type) {
        super(model, type);
    }

    @Override
    public void update(World world, Vector2i pos) {
        if(this.canPass(world.getAt(pos.x, pos.y-1))) {
            world.moveDown(pos);
        } else if(this.canPass(world.getAt(pos.x - 1, pos.y - 1))) {
            world.swapParticles(pos,pos.x - 1, pos.y - 1);
        } else if(this.canPass(world.getAt(pos.x + 1, pos.y - 1))) {
            world.swapParticles(pos,pos.x + 1, pos.y - 1);
        }
    }

}
