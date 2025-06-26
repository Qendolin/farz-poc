package com.qendolin.farz.mixin.client;


import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.qendolin.farz.FarZClient;
import com.qendolin.farz.Util;
import net.minecraft.client.renderer.CachedPerspectiveProjectionMatrixBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CachedPerspectiveProjectionMatrixBuffer.class)
public class CachedPerspectiveProjectionMatrixBufferMixin {
    @Shadow @Final private float zNear;

    @Shadow @Final private float zFar;

    @Unique
    private int invalidate = 0;

    @Definition(id = "width", field = "Lnet/minecraft/client/renderer/CachedPerspectiveProjectionMatrixBuffer;width:I")
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
    private Matrix4f getReverseZProjectionMatrix(Matrix4f matrix, int w, int h, float fov) {
        if(FarZClient.vanilla()) {
            return matrix;
        }
        float aspect = (float) w / h;
        return Util.createProjectionMatrix(fov, aspect, zNear, zFar);
    }
}
