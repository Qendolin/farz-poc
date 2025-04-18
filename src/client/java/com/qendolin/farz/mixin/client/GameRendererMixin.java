package com.qendolin.farz.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.qendolin.farz.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final public static float PROJECTION_Z_NEAR;

    @Shadow public abstract float getDepthFar();

    @ModifyReturnValue(method = "getProjectionMatrix", at = @At("RETURN"))
    private Matrix4f getReverseZInfiniteProjectionMatrix(Matrix4f matrix, float fov) {
        float aspect = (float) minecraft.getWindow().getWidth() / minecraft.getWindow().getHeight();
        return Util.createProjectionMatrix(fov, aspect, PROJECTION_Z_NEAR, getDepthFar());
    }
}
