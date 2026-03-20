package com.apet2929.game;

import com.apet2929.engine.MouseInput;
import com.apet2929.engine.RenderManager;
import com.apet2929.engine.WindowManager;
import com.apet2929.engine.model.Camera;
import com.apet2929.engine.model.ObjectLoader;
import org.easymock.EasyMock;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

class SandSimTest {

    @Test
    public void testSelectedParticleType() {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        Camera cam = new Camera();
        SandSim sim = new SandSim(rm, window, ol, cam);
        EasyMock.expect(window.isKeyPressed(EasyMock.anyInt())).andReturn(false).anyTimes();
        boolean[] keysPressed = {false, false, true };
        EasyMock.expect(window.getNumbersPressed()).andReturn(keysPressed);
        EasyMock.expect(mi.isLeftButtonPressed()).andReturn(false).anyTimes();

        EasyMock.replay(rm, window, ol, mi);
        sim.input(mi);

        EasyMock.verify(rm, window, ol, mi);
        assertEquals(2, sim.selectedParticleType);
    }
}