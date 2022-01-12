package com.apet2929.engine;


import com.apet2929.engine.model.Entity;
import com.apet2929.engine.model.Line;
import com.apet2929.engine.model.Model;
import com.apet2929.engine.model.Transformation;
import com.apet2929.engine.utils.Utils;
import com.apet2929.game.Launcher;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static com.apet2929.engine.utils.Consts.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;
    private ShaderManager lineShader;

    public RenderManager(){
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");

        lineShader = new ShaderManager();
        lineShader.createVertexShader(Utils.loadResource("/shaders/line_vertex.vs"));
        lineShader.createFragmentShader(Utils.loadResource("/shaders/line_fragment.fs"));
    }

    public void beginRender() {
        shader.bind();
    }

    public void endRender() {
        shader.unbind();
    }

    public void setWindowUniform() {
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
    }

    public void renderEntity(Entity entity) {
        clear();
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
        glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void render(Entity entity){
        clear();

        if (window.isResize()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(false);
        }

        shader.bind();

        // Update projection Matrix
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());

        // Render each gameItem

        Matrix4f transMatrix = Transformation.createTransformationMatrix(entity);
        shader.setUniform("transformationMatrix", transMatrix);
        // Draw the mesh
        glBindVertexArray(entity.getModel().getId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
        // Restore state
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        shader.unbind();

    }

    public void drawLine(Line line) {
        glBindVertexArray(line.getId());
        glEnableVertexAttribArray(0); // start/end point
        glEnableVertexAttribArray(1); // start/end color
        glDrawArrays(GL_LINES, 0, 2);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void beginDrawLines() {
        lineShader.bind();
    }

    public void endDrawLines() {
        lineShader.unbind();
    }


    public boolean canRender() {
        return shader.isBound();
    }
    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }
}
