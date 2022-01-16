package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public abstract class Particle {

    private static final Map<ParticleType, MatterState> particleTypeMatterStates = setupMatterStates();

    private final Model model;
    private final ParticleType type;

    public Particle(Model model, ParticleType type) {
        this.model = model;
        this.type = type;
    }

    public abstract void update(World world, Vector2i pos);

    public ParticleType getType() {
        return type;
    }

    public Model getModel() {
        return model;
    }

    public boolean canPass(Particle other) {
        return canPass(getMatterState(other));
    }

    public static MatterState getMatterState(Particle particle) {
        if(particle == null) return null;
        return getMatterState(particle.getType());
    }

    public static MatterState getMatterState(ParticleType type) {
        if(type == null) return null;
        MatterState state = Particle.particleTypeMatterStates.get(type);
        if(state != null)
            return state;
        else
            System.err.println("ParticleType not implemented! Add it to the stateMap in Particle.java");
            return null;
    }

    public boolean canPass(MatterState other) {
        MatterState myState = getMatterState(this);
        if(myState == other || other == null){
            return false;
        } else if(other == MatterState.NONE){
            return true;
        } else if(myState == MatterState.SOLID) {
            return true;
        } else if(myState == MatterState.LIQUID){
            return other == MatterState.GAS;
        } else if(myState == MatterState.GAS) {
            return false;
        }
        return false;
    }

    private static HashMap<ParticleType, MatterState> setupMatterStates() {
        HashMap<ParticleType, MatterState> stateMap = new HashMap<>();
        stateMap.put(ParticleType.NONE, MatterState.NONE);

//        Gasses
        stateMap.put(ParticleType.SMOKE, MatterState.GAS);

//        Liquids
        stateMap.put(ParticleType.WATER, MatterState.LIQUID);

//        Solids
        stateMap.put(ParticleType.SAND, MatterState.SOLID);

        return stateMap;
    }



}
