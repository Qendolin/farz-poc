package com.qendolin.farz.mixin.client;

import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.qendolin.farz.FarZClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GlCommandEncoder.class)
public class GlCommandEncoderMixin {

    @ModifyArg(
        method = "createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalInt;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/blaze3d/systems/RenderPass;",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClearDepth(D)V", remap = false), index = 0)
    private double reverseZ1(double depth) {
        if(FarZClient.normal()) {
            return depth;
        }
        return (1.0 - depth);
    }

    @ModifyArg(
        method = "clearColorAndDepthTextures(Lcom/mojang/blaze3d/textures/GpuTexture;ILcom/mojang/blaze3d/textures/GpuTexture;D)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClearDepth(D)V", remap = false), index = 0)
    private double reverseZ2(double depth) {
        if(FarZClient.normal()) {
            return depth;
        }
        return (1.0 - depth);
    }

    @ModifyArg(
        method = "clearColorAndDepthTextures(Lcom/mojang/blaze3d/textures/GpuTexture;ILcom/mojang/blaze3d/textures/GpuTexture;DIIII)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClearDepth(D)V", remap = false), index = 0)
    private double reverseZ3(double depth) {
        if(FarZClient.normal()) {
            return depth;
        }
        return (1.0 - depth);
    }

    @ModifyArg(
        method = "clearDepthTexture",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClearDepth(D)V", remap = false), index = 0)
    private double reverseZ4(double depth) {
        if(FarZClient.normal()) {
            return depth;
        }
        return (1.0 - depth);
    }
}
