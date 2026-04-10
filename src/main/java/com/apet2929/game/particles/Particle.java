package com.apet2929.game.particles;

import com.apet2929.engine.model.Texture;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;


public abstract class Particle {
    public static Texture DEBUG_TEXTURE;
    private Texture texture;
    private ParticleType type;
    private int gridX, gridY;
    public Vector2f velocity;
    public int debugCountdown = -1; // -1 = debug not queued, 0=debug next update, 1+=debug is queued

    public Particle(int x, int y, ParticleType type) {
        gridX = x;
        gridY = y;
        this.type = type;
        this.texture = ParticleLoader.getParticleTexture(type);
        this.velocity = new Vector2f(0,0);
    }

    public void update(World world) {
        if(debugCountdown >= 0){
            if(debugCountdown == 0) {

                System.out.println(this);
            }
            debugCountdown -= 1;
        }
    }

    public void debugSoon(){
        debugCountdown = 2;
    }

    public boolean willDebugSoon(){
        return debugCountdown != -1;
    }

    public abstract boolean canSwap(ParticleType type);

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
        return canSwap(other.getType());
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

    public ArrayList<Particle> getNeighbors(World world) {
        ArrayList<Particle> neighbors = new ArrayList<>();
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++) {
                if(world.getAt(getGridX()+x, getGridY()+y) != null) {
                    neighbors.add(world.getAt(getGridX()+x, getGridY()+y));
                }
            }
        }
        return neighbors;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "type=" + type +
                ", position=(" + gridX + ", " + gridY + ")" +
                ", velocity=" + velocity +
                '}';
    }
}
