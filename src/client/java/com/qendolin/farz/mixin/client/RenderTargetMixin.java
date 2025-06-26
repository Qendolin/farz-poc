package com.qendolin.farz.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.qendolin.farz.FarZClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderTarget.class, remap = false)
public class RenderTargetMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void trackRenderTarget(CallbackInfo ci) {
        FarZClient.renderTargetPatcher.addRenderTarget((RenderTarget) (Object) this);
    }
}
