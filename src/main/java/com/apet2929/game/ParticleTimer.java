package com.apet2929.game;

import com.apet2929.engine.EngineManager;
import com.apet2929.engine.utils.Consts;

public class ParticleTimer {

    public static float currentTime = 0.0f;
    public static final float GOAL_FRAMETIME = 1.0f / Consts.PARTICLE_TPS;

    public static boolean update() {
        currentTime += 1.0f / EngineManager.getFps();
        if(currentTime > GOAL_FRAMETIME) {
            currentTime -= GOAL_FRAMETIME;
            return true;
        }
        return false;
    }

}
