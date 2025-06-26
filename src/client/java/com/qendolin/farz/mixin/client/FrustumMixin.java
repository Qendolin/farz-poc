package com.qendolin.farz.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.qendolin.farz.Util;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Frustum.class)
public class FrustumMixin {

    @Unique
    private int planeMask = ~0;

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/culling/Frustum;)V",
        at = @At(value = "TAIL"))
    private void copyPlaneMask(Frustum frustum, CallbackInfo ci) {
        this.planeMask = ((FrustumMixin) (Object) frustum).planeMask;
    }

    @WrapOperation(
        method = "calculateFrustum",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;transformTranspose(Lorg/joml/Vector4f;)Lorg/joml/Vector4f;",
            remap = false
        )
    )
    private Vector4f fixViewVector(Matrix4f instance, Vector4f v, Operation<Vector4f> original, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        // If this method returns the wrong value, the game gets stuck in an infinite loop
        // Cannot rely on the configured values, needs to inspect the matrices directly

        planeMask = ~0;

        boolean reverse = Util.isReverseZProjection(projectionMatrix);
        boolean infinite = Util.isInfiniteProjection(projectionMatrix);

        if (reverse) {
            v.z = -1.0f;
        }

        if (infinite) {
            if (reverse) {
                planeMask = ~(FrustumIntersection.PLANE_MASK_NZ);
            } else {
                planeMask = ~(FrustumIntersection.PLANE_MASK_PZ);
            }

            v.z *= -1.0f;

            viewMatrix.transformTranspose(v);

            return v;
        }

        return original.call(instance, v);
    }

    @Redirect(
        method = "offsetToFullyIncludeCameraCube",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/FrustumIntersection;intersectAab(FFFFFF)I",
            remap = false
        )
    )
    private int fixIntersectAab1(FrustumIntersection instance, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        int ret = instance.intersectAab(
            minX, minY, minZ,
            maxX, maxY, maxZ,
            planeMask
        );
        if(planeMask != ~0 && ret == FrustumIntersection.INTERSECT) {
            // i couldn't get it to work for infinite projections, because they have NaN values.
            return FrustumIntersection.INSIDE;
        }
        return ret;
    }

    @Redirect(
        method = "cubeInFrustum(DDDDDD)I",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/FrustumIntersection;intersectAab(FFFFFF)I",
            remap = false
        )
    )
    private int fixIntersectAab2(FrustumIntersection instance, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        int ret = instance.intersectAab(
            minX, minY, minZ,
            maxX, maxY, maxZ,
            planeMask
        );
        if(planeMask != ~0 && ret == FrustumIntersection.INTERSECT) {
            // i couldn't get it to work for infinite projections, because they have NaN values.
            return FrustumIntersection.INSIDE;
        }
        return ret;
    }
}
