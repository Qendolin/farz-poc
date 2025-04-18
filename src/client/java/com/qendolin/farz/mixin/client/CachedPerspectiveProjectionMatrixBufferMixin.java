package com.qendolin.farz.mixin.client;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.qendolin.farz.Util;
import net.minecraft.client.renderer.CachedPerspectiveProjectionMatrixBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CachedPerspectiveProjectionMatrixBuffer.class)
public class CachedPerspectiveProjectionMatrixBufferMixin {
    @Shadow @Final private float zNear;

    @Shadow @Final private float zFar;

    @ModifyReturnValue(method = "createProjectionMatrix", at = @At("RETURN"))
    private Matrix4f getReverseZProjectionMatrix(Matrix4f matrix, int w, int h, float fov) {
        float aspect = (float) w / h;
        return Util.createProjectionMatrix(fov, aspect, zNear, zFar);
    }
}
