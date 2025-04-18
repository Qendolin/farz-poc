package com.qendolin.farz.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.opengl.GlConst;
import com.qendolin.farz.FarZClient;
import org.lwjgl.opengl.GL32;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlConst.class)
public class GlConstMixin {

    @ModifyReturnValue(method = "toGlInternalId", at = @At("RETURN"), remap = false)
    private static int depthToFloat(int original) {
        if(FarZClient.FLOATING_POINT_DEPTH && original == GL32.GL_DEPTH_COMPONENT32) {
            return GL32.GL_DEPTH_COMPONENT32F;
        }
        return original;
    }
}
