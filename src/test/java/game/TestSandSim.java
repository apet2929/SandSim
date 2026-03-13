package game;

import com.apet2929.engine.MouseInput;
import com.apet2929.engine.RenderManager;
import com.apet2929.engine.WindowManager;
import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.game.SandSim;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSandSim {

    @Test
    public void testSelectedParticleType() {
        RenderManager rm = EasyMock.mock(RenderManager.class);
        WindowManager window = EasyMock.mock(WindowManager.class);
        ObjectLoader ol = EasyMock.mock(ObjectLoader.class);
        MouseInput mi = EasyMock.mock(MouseInput.class);
        SandSim sim = new SandSim(rm, window, ol);
        EasyMock.expect(window.isKeyPressed(EasyMock.anyInt())).andReturn(false).anyTimes();
        boolean[] keysPressed = {false, false, true };
        EasyMock.expect(window.getNumbersPressed()).andReturn(keysPressed);
        EasyMock.expect(mi.isLeftButtonPressed()).andReturn(false).anyTimes();

        EasyMock.replay(rm, window, ol);
        sim.input(mi);

        EasyMock.verify(rm, window, ol);
        assertEquals(2, sim.selectedParticleType);
    }


}
