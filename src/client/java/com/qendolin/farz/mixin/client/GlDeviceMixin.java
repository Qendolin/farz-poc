package com.qendolin.farz.mixin.client;

import com.mojang.blaze3d.opengl.GlDevice;
import com.qendolin.farz.FarZClient;
import org.lwjgl.opengl.GL45;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlDevice.class)
public class GlDeviceMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void fixClipVolumeDepth(CallbackInfo ci) {
        if(FarZClient.ZERO_TO_ONE)
            GL45.glClipControl(GL45.GL_LOWER_LEFT, GL45.GL_ZERO_TO_ONE);
    }
}
