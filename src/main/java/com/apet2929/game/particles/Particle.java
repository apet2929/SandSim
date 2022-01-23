package com.apet2929.game.particles;

import com.apet2929.engine.model.Texture;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import org.joml.Vector2f;
import org.joml.Vector2i;


public abstract class Particle {

    private Texture texture;
    private ParticleType type;
    private int gridX, gridY;
    public Vector2f velocity;

    public Particle(int x, int y) {
        gridX = x;
        gridY = y;
        this.type = getEnumType();
        this.texture = ParticleLoader.getParticleTexture(type);
        this.velocity = new Vector2f(0,0);
    }
    public abstract void update(World world);
    public abstract boolean canSwap(ParticleType.MatterType type);

    public boolean isEmpty() {
        return this.type == ParticleType.EMPTYPARTICLE;
    }

    public boolean isSolid() {
        return this.type.matterType == ParticleType.MatterType.IMMOVABLESOLID || this.type.matterType == ParticleType.MatterType.MOVABLESOLID;
    }

    public boolean isLiquid() {
        return this.type.matterType == ParticleType.MatterType.LIQUID;
    }

    public boolean isGas() {
        return this.type.matterType == ParticleType.MatterType.GAS;
    }

    public boolean canSwap(Particle other) {
        if(other == null) return false;
        return canSwap(other.getType().matterType);
    }

    public ParticleType getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }


    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setPositionByWorld(Vector2i position) {
        this.gridX = position.x;
        this.gridY = position.y;
    }

    public void moveWithVelocity(World world) {
        int xModifier = this.velocity.x > 0 ? 1 : -1;
        int yModifier = this.velocity.y > 0 ? 1 : -1;

        float delta = 1.0f / Consts.PARTICLE_TPS;

        float xSlope = Math.abs(this.velocity.x * delta);
        float ySlope = Math.abs(this.velocity.y * delta);

        float xCounter = 0;
        float yCounter = 0;
        int xInc = 0;
        int yInc = 0;

        boolean cont = true;
        if(xSlope > ySlope) {
            while(cont) {
                xCounter += xSlope * xModifier;
                if(xCounter > xInc * 0.5f) {

                }
            }
        }

    }

    public void setPositionByWorld(int x, int y) {
        this.gridX = x;
        this.gridY = y;
    }

    public Particle getParticleBelow(World world) {
        return world.getAt(getGridX(), getGridY()-1);
    }

    private ParticleType getEnumType() {
        return ParticleType.valueOf(this.getClass().getSimpleName().toUpperCase());
    }

    @Override
    public String toString() {
        return "Particle{" +
                "type=" + type +
                ", position=(" + gridX + ", " + gridY + ")" +
                '}';
    }
}
