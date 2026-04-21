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
            sync((int) FRAMERATE);
            if(window.windowShouldClose())
                stop();
            input();
            update();
            render();
        }
        cleanup();
    }

    private static long variableYieldTime, lastTime;
    private static final Queue<Double> dtQueue = new LinkedList<>(); // dt = time between frames. used to calculate average fps


    /**
     * An accurate sync method that adapts automatically
     * to the system it runs on to provide reliable results.
     *
     * @param fps The desired frame rate, in frames per second
     * @author kappa (On the  <a href="http://forum.lwjgl.org/index.php?topic=4452.msg23997#msg23997">LWJGL Forums</a>)
     */
    private static void sync(int fps) {
        if (fps <= 0) return;

        long sleepTime = NANOSECOND / fps; // nanoseconds to sleep this frame
        // yieldTime + remainder micro & nano seconds if smaller than sleepTime
        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
        long overSleep = 0; // time the sync goes over by

        try {
            while (true) {
                long t = System.nanoTime() - lastTime;

                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);
                }else if (t < sleepTime) {
                    // burn the last few CPU cycles to ensure accuracy
                    Thread.yield();
                }else {
                    overSleep = t - sleepTime;
                    if(dtQueue.size() >= fps) dtQueue.remove();
                    dtQueue.add(((double) overSleep / NANOSECOND) / NANOSECOND); // 0.16
                    break; // exit while loop
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);

            // auto tune the time sync should yield
            if (overSleep > variableYieldTime) {
                // increase by 200 microseconds (1/5 a ms)
                variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
            }
            else if (overSleep < variableYieldTime - 200*1000) {
                // decrease by 2 microseconds
                variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
            }
        }
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

    public static double getDeltaTime() {
        return (Double) dtQueue.peek();
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
