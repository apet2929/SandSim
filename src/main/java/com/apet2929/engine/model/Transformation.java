package com.apet2929.engine.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos()).
                rotateX((float) Math.toRadians(entity.getRotation().x)).
                rotateY((float) Math.toRadians(entity.getRotation().y)).
                rotateZ((float) Math.toRadians(entity.getRotation().z)).
                scale(entity.getScale());
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f position) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(position);
        return matrix;
    }
}
