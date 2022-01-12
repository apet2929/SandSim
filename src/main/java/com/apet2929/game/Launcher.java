package com.apet2929.game;



import com.apet2929.engine.EngineManager;
import com.apet2929.engine.ILogic;
import com.apet2929.engine.WindowManager;
import com.apet2929.engine.utils.Consts;

public class Launcher {

    private static WindowManager window;
    private static SandSim game;


    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 1600, 900, false);
        game = new SandSim();
        EngineManager engine = new EngineManager();

        try{
            engine.start();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public static WindowManager getWindow(){
        return window;
    }

    public static ILogic getGame() {
        return game;
    }
}
