package com.qendolin.farz;

import org.joml.Matrix4f;

public class Util {

    public static Matrix4f createProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
        if(FarZClient.normal()) {
            if(FarZClient.infinite()) {
                return createNormalInfiniteProjectionMatrix(fov, aspect, zNear, FarZClient.zeroToOne());
            } else {
                return createNormalProjectionMatrix(fov, aspect, zNear, zFar, FarZClient.zeroToOne());
            }
        }
        if(FarZClient.infinite()) {
            return createReverseZInfiniteProjectionMatrix(fov, aspect, zNear, FarZClient.zeroToOne());
        } else {
            return createReverseZProjectionMatrix(fov, aspect, zNear, zFar, FarZClient.zeroToOne());
        }
    }

    private static Matrix4f createNormalProjectionMatrix(float fov, float aspect, float zNear, float zFar, boolean zeroToOne) {
        float tanHalfFovy = (float) Math.tan(fov * Math.PI / 180.0 * 0.5);
        Matrix4f dest = new Matrix4f();

        float C, D;

        if (zeroToOne) {
            // Normal Z, [0, 1] Range
            C = zFar / (zNear - zFar);
            D = (zFar * zNear) / (zNear - zFar);
        } else {
            // Normal Z, [-1, 1] Range
            C = (zFar + zNear) / (zNear - zFar);
            D = (2.0f * zFar * zNear) / (zNear - zFar);
        }

        dest.m00(1.0f / (tanHalfFovy * aspect));
        dest.m11(1.0f / tanHalfFovy);
        dest.m22(C);
        dest.m23(-1.0f);
        dest.m32(D);
        dest.m33(0.0f);

        return dest;
    }

    private static Matrix4f createNormalInfiniteProjectionMatrix(float fov, float aspect, float zNear, boolean zeroToOne) {
        float tanHalfFovy = (float) Math.tan(Math.toRadians(fov * 0.5));
        Matrix4f dest = new Matrix4f();

        float A = 1.0f / (tanHalfFovy * aspect);
        float B = 1.0f / tanHalfFovy;

        dest.m00(A);
        dest.m11(B);

        // Same as normal
        dest.m23(-1.0f);
        dest.m33(0.0f);

        if (zeroToOne) {
            // Normal Z direction, clip range [0, 1], infinite far plane
            dest.m22(-1.0f);
            dest.m32(-zNear);
        } else {
            // Normal Z direction, clip range [-1, 1], infinite far plane
            dest.m22(-1.0f);
            dest.m32(-2.0f * zNear);
        }

        dest.m33(0.0f);

        return dest;
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

    public static boolean isReverseZProjection(Matrix4f proj) {
        return proj.m32() > 0.0f;
    }

    public static boolean isInfiniteProjection(Matrix4f proj) {
        return proj.m22() == 0.0f || proj.m22() == -1.0f;
    }
}
