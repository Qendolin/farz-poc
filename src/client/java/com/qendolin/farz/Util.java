package com.qendolin.farz;

import org.joml.Matrix4f;

public class Util {

    private static final boolean USE_INFINITE_FAR = "infinite".equalsIgnoreCase(System.getProperty("farz.mode"));

    public static Matrix4f createProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
        if(USE_INFINITE_FAR) {
            return createReverseZInfiniteProjectionMatrix(fov, aspect, zNear);
        } else {
            return createReverseZProjectionMatrix(fov, aspect, zNear, zFar);
        }
    }

    public static Matrix4f createReverseZProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
        float tanHalfFovy = (float) Math.tan(fov * (Math.PI / 180.0) * 0.5);
        Matrix4f dest = new Matrix4f();

        // Target Matrix (Finite):
        // [ 1/(tan*aspect)  0        0              0        ]
        // [       0      1/tan       0              0        ]
        // [       0         0    zNear/(zFar-zNear)  zNear*zFar/(zFar-zNear) ]
        // [       0         0       -1              0        ]

        float C = zNear / (zFar - zNear); // Term for m22
        float D = zNear * zFar / (zFar - zNear); // Term for m32

        dest.m00(1.0f / (tanHalfFovy * aspect));
        dest.m11(1.0f / tanHalfFovy);
        dest.m22(C);
        dest.m23(-1.0f); // Maps eye-space Z into W component
        dest.m32(D);     // Maps near and far planes correctly
        dest.m33(0.0f);

        return dest;
    }

    // TODO: Fix viewVector calculation in Frustum#calculateFrustum
    public static Matrix4f createReverseZInfiniteProjectionMatrix(float fov, float aspect, float zNear) {
        float tanHalfFovy = (float) Math.tan(fov * (Math.PI / 180.0) * 0.5);
        Matrix4f dest = new Matrix4f();

        // Target Matrix (Infinite):
        // [ 1/(tan*aspect)  0     0    0 ]
        // [       0      1/tan    0    0 ]
        // [       0         0     0  zNear ]
        // [       0         0    -1    0 ]

        dest.m00(1.0f / (tanHalfFovy * aspect));
        dest.m11(1.0f / tanHalfFovy);
        dest.m22(0.0f);
        dest.m23(-1.0f); // Maps eye-space Z into W component
        dest.m32(zNear); // Maps near plane correctly
        dest.m33(0.0f);

        return dest;
    }
}
