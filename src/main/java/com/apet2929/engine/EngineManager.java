package com.apet2929.engine;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static com.apet2929.engine.utils.Consts.FRAMERATE;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    private static int fps;

    private boolean isRunning;
    private Sync sync;
    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;
    private MouseInput mouseInput;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        gameLogic.init();
        mouseInput.init();
        fps = (int) FRAMERATE;
        sync = new Sync();
    }

    public void start() throws Exception {
        init();

        if(isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;

        while(isRunning) {
            sync.sync((int) FRAMERATE);
            if(window.windowShouldClose())
                stop();
            input();
            update();
            render();
        }
        cleanup();
    }

    public static float getDeltaTime() {
        return 1.0f / FRAMERATE;
    }

    private void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input(){
        mouseInput.input();
        gameLogic.input(mouseInput);
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update() {
        gameLogic.update();
    }

    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

    public static class MemoryUsage {
        public float memoryUsage;
        public float maxMemory;
        public float freeMemory;

        public MemoryUsage(float memoryUsage, float maxMemory, float freeMemory) {
            this.memoryUsage = memoryUsage;
            this.maxMemory = maxMemory;
            this.freeMemory = freeMemory;
        }
    }

    public static MemoryUsage getMemoryUsage(){
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return new MemoryUsage(allocatedMemory, maxMemory, freeMemory);
    }
}
