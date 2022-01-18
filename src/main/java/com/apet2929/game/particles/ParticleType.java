package com.apet2929.game.particles;

import com.apet2929.game.particles.gas.Smoke;
import com.apet2929.game.particles.liquid.Water;
import com.apet2929.game.particles.solid.Sand;
import com.sun.jdi.ClassType;

import java.util.*;
import java.util.stream.Collectors;

public enum ParticleType {
    SAND(Sand.class, MatterType.MOVABLESOLID) {
        @Override
        public Particle createParticleByMatrix(int x, int y) {
            return new Sand(x, y);
        }
    },
    WATER(Water.class, MatterType.LIQUID) {
        @Override
        public Particle createParticleByMatrix(int x, int y) {
            return new Water(x, y);
        }
    },
    SMOKE(Smoke.class, MatterType.GAS) {
        @Override
        public Particle createParticleByMatrix(int x, int y) {
            return new Smoke(x, y);
        }
    },

    EMPTYPARTICLE(EmptyParticle.class, MatterType.EMPTY) {
        @Override
        public Particle createParticleByMatrix(int x, int y) {
            return new EmptyParticle(x, y);
        }
    };

    public final Class<? extends Particle> particleClassType;
    public final MatterType matterType;

    public static List<ParticleType> IMMOVABLE_SOLIDS;
    public static List<ParticleType> MOVABLE_SOLIDS;
    public static List<ParticleType> SOLIDS;
    public static List<ParticleType> LIQUIDS;
    public static List<ParticleType> GASSES;


    ParticleType(Class<? extends Particle> pClass, MatterType matterType) {
        this.particleClassType = pClass;
        this.matterType = matterType;
    }

    public abstract Particle createParticleByMatrix(int x, int y);


    public static List<ParticleType> getMovableSolids() {
        if (MOVABLE_SOLIDS == null) {
            MOVABLE_SOLIDS = initializeList(MatterType.MOVABLESOLID);
            MOVABLE_SOLIDS.sort(Comparator.comparing(Enum::toString));
        }
        return Collections.unmodifiableList(MOVABLE_SOLIDS);
    }

    public static List<ParticleType> getImmovableSolids() {
        if (IMMOVABLE_SOLIDS == null) {
            IMMOVABLE_SOLIDS = initializeList(MatterType.IMMOVABLESOLID);
            IMMOVABLE_SOLIDS.sort(Comparator.comparing(Enum::toString));
        }
        return Collections.unmodifiableList(IMMOVABLE_SOLIDS);
    }

    public static List<ParticleType> getSolids() {
        if (SOLIDS == null) {
            List<ParticleType> immovables = new ArrayList<>(getImmovableSolids());
            immovables.addAll(getMovableSolids());
            SOLIDS = immovables;
            immovables.sort(Comparator.comparing(Enum::toString));
        }
        return Collections.unmodifiableList(SOLIDS);
    }

    public static List<ParticleType> getLiquids() {
        if (LIQUIDS == null) {
            LIQUIDS = initializeList(MatterType.LIQUID);
            LIQUIDS.sort(Comparator.comparing(Enum::toString));
        }
        return Collections.unmodifiableList(LIQUIDS);
    }

    public static List<ParticleType> getGasses() {
        if (GASSES == null) {
            GASSES = initializeList(MatterType.GAS);
            GASSES.sort(Comparator.comparing(Enum::toString));
        }
        return Collections.unmodifiableList(GASSES);
    }

    private static List<ParticleType> initializeList(MatterType matterType) {
        return Arrays.stream(ParticleType.values()).filter(elementType -> elementType.matterType.equals(matterType)).collect(Collectors.toList());
    }

    public enum MatterType {
        MOVABLESOLID,
        IMMOVABLESOLID,
        LIQUID,
        GAS,
        EMPTY;
    }
}


