package com.qendolin.farz.mixin.client;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.qendolin.farz.FarZClient;
import com.qendolin.farz.Util;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import net.minecraft.client.renderer.CachedPerspectiveProjectionMatrixBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CachedOrthoProjectionMatrixBuffer.class)
public class CachedOrthoProjectionMatrixBufferMixin {
    @Shadow @Final private float zNear;

    @Shadow @Final private boolean invertY;

    @Shadow @Final private float zFar;

    @ModifyReturnValue(method = "createProjectionMatrix", at = @At("RETURN"))
    private Matrix4f getReverseZOrthogonalMatrix(Matrix4f original, float w, float h) {
        return new Matrix4f().setOrtho(0.0F, w, this.invertY ? h : 0.0F, this.invertY ? 0.0F : h, this.zFar, this.zNear, FarZClient.ZERO_TO_ONE);
    }
}
