package com.apet2929.engine;


import com.apet2929.engine.model.Entity;
import com.apet2929.engine.model.Grid;
import com.apet2929.engine.model.Transformation;
import com.apet2929.engine.utils.Utils;
import com.apet2929.game.Launcher;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

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
        lineShader.link();
        lineShader.createUniform("projectionMatrix");
    }

    public void beginRender() {
        shader.bind();
        window.updateProjectionMatrix();
    }

    public void endRender() {
        shader.unbind();
    }

    public void renderEntity(Entity entity) {
        shader.setUniform("projectionMatrix", window.getProjectionMatrix());
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

    public void drawLines(int id, int numLines) {
        lineShader.bind();
        lineShader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        glBindVertexArray(id);
        glEnableVertexAttribArray(0); // start/end point
        glEnableVertexAttribArray(1); // start/end color
        glDrawArrays(GL_LINES, 0, numLines * 2);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
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
