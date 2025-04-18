package com.qendolin.farz;

import org.joml.Matrix4f;

public class Util {

    private static final boolean USE_INFINITE_FAR = "infinite".equalsIgnoreCase(System.getProperty("farz.mode"));

    public static Matrix4f createProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
        if(USE_INFINITE_FAR) {
            return createReverseZInfiniteProjectionMatrix(fov, aspect, zNear, FarZClient.ZERO_TO_ONE);
        } else {
            return createReverseZProjectionMatrix(fov, aspect, zNear, zFar, FarZClient.ZERO_TO_ONE);
        }
    }

    public static Matrix4f createReverseZProjectionMatrix(float fov, float aspect, float zNear, float zFar, boolean zeroToOne) {
        float tanHalfFovy = (float) Math.tan(fov * (Math.PI / 180.0) * 0.5);
        Matrix4f dest = new Matrix4f();

        // Target Matrix (Finite):
        // [ 1/(tan*aspect)  0        0              0        ]
        // [       0      1/tan       0              0        ]
        // [       0         0    zNear/(zFar-zNear)  zNear*zFar/(zFar-zNear) ]
        // [       0         0       -1              0        ]

        float C, D; // Coefficients for m22 and m32

        if (zeroToOne) {
            // Reverse-Z, [0, 1] Range
            // Near maps to 1, Far maps to 0
            C = zNear / (zFar - zNear);
            D = zNear * zFar / (zFar - zNear);
        } else {
            // Reverse-Z, [-1, 1] Range
            // Near maps to 1, Far maps to -1
            C = (zFar + zNear) / (zFar - zNear);
            D = (2.0f * zNear * zFar) / (zFar - zNear);
        }

        dest.m00(1.0f / (tanHalfFovy * aspect));
        dest.m11(1.0f / tanHalfFovy);
        dest.m22(C);
        dest.m23(-1.0f); // Maps eye-space Z into W component
        dest.m32(D);     // Maps near and far planes correctly
        dest.m33(0.0f);

        return dest;
    }

    public static Matrix4f createReverseZInfiniteProjectionMatrix(float fov, float aspect, float zNear, boolean zeroToOne) {
        float tanHalfFovy = (float) Math.tan(fov * (Math.PI / 180.0) * 0.5);
        Matrix4f dest = new Matrix4f();

        // Target Matrix (Infinite):
        // [ 1/(tan*aspect)  0     0    0 ]
        // [       0      1/tan    0    0 ]
        // [       0         0     0  zNear ]
        // [       0         0    -1    0 ]

        float C, D;

        if (zeroToOne) {
            // Reverse-Z, [0, 1] Range (Infinite Far)
            // Near maps to 1, Far (infinity) maps to 0
            C = 0.0f;
            D = zNear;
        } else {
            // Reverse-Z, [-1, 1] Range (Infinite Far)
            // Near maps to 1, Far (infinity) maps to -1
            C = 1.0f;
            D = 2.0f * zNear;
        }

        dest.m00(1.0f / (tanHalfFovy * aspect));
        dest.m11(1.0f / tanHalfFovy);
        dest.m22(C);
        dest.m23(-1.0f); // Maps eye-space Z into W component
        dest.m32(D); // Maps near plane correctly
        dest.m33(0.0f);

        return dest;
    }
}
