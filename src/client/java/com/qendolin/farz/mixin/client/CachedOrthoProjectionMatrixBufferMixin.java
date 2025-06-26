package com.qendolin.farz.mixin.client;


import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.qendolin.farz.FarZClient;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CachedOrthoProjectionMatrixBuffer.class)
public class CachedOrthoProjectionMatrixBufferMixin {
    @Shadow @Final private float zNear;

    @Shadow @Final private boolean invertY;

    @Shadow @Final private float zFar;

    @Unique
    private int invalidate = 0;

    @Definition(id = "width", field = "Lnet/minecraft/client/renderer/CachedOrthoProjectionMatrixBuffer;width:F")
    @Expression("this.width != ?")
    @ModifyExpressionValue(method = "getBuffer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean invalidateBuffer(boolean original) {
        if(invalidate < FarZClient.invalidate) {
            invalidate = FarZClient.invalidate;
            return true;
        }
        return original;
    }

    @ModifyReturnValue(method = "createProjectionMatrix", at = @At("RETURN"))
    private Matrix4f getReverseZOrthogonalMatrix(Matrix4f original, float w, float h) {
        if(FarZClient.normal()) {
            return original;
        }
        return new Matrix4f().setOrtho(0.0F, w, this.invertY ? h : 0.0F, this.invertY ? 0.0F : h, this.zFar, this.zNear, FarZClient.zeroToOne());
    }
}
