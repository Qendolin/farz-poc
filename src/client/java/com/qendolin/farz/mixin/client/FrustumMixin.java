package com.qendolin.farz.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Frustum.class)
public class FrustumMixin {
    @WrapOperation(
        method = "calculateFrustum",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;transformTranspose(Lorg/joml/Vector4f;)Lorg/joml/Vector4f;"
        )
    )
    private Vector4f fixViewVector(Matrix4f instance, Vector4f v, Operation<Vector4f> original, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        // Reverse Z
        v.z = -1.0f;
        // can't use the projection matrix if it's infinite, results in a (0,0,0,w) vector.
        // Hack: use view matrix only, but that doesn't include shearing from nausea and bobbing
        // The real solution isn't too difficult
        return viewMatrix.transformTranspose(v);
    }


}
