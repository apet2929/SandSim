package com.apet2929.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static float[] concatWithArrayCopy(float[] vertices, float[] vertices1) {
        float[] result = Arrays.copyOf(vertices, vertices.length + vertices1.length);
        System.arraycopy(vertices1, 0, result, vertices.length, vertices1.length);
        return result;
    }

    public static String loadResource(String filename) throws Exception {
        String result;
        try(
            InputStream in = Utils.class.getResourceAsStream(filename);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;

    }

    public static List<String> readAllLines(String filename) throws Exception {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        return lines;
    }

    public static Vector4f matrixVectorMul(Matrix4f mat, Vector4f vec) {
        Vector4f u = new Vector4f();
        /*
        m[0] = left column of m

        u.x = m[0].x * v.x + m[1].x * v.y + m[2].x * v.z;
u.y = m[0].y * v.x + m[1].y * v.y + m[2].y * v.z;
u.z = m[0].z * v.x + m[1].z * v.y + m[2].z * v.z;
         */

        u.x = (mat.get(0, 0) * vec.x) + (mat.get(1, 0) * vec.y) + (mat.get(2, 0) * vec.z) + (mat.get(3, 0) * vec.w);
        u.y = (mat.get(0, 1) * vec.x) + (mat.get(1, 1) * vec.y) + (mat.get(2, 1) * vec.z) + (mat.get(3, 1) * vec.w);
        u.z = (mat.get(0, 2) * vec.x) + (mat.get(1, 2) * vec.y) + (mat.get(2, 2) * vec.z) + (mat.get(3, 2) * vec.w);
        u.w = (mat.get(0, 3) * vec.x) + (mat.get(1, 3) * vec.y) + (mat.get(2, 3) * vec.z) + (mat.get(3, 3) * vec.w);
        return u;
    }
}
