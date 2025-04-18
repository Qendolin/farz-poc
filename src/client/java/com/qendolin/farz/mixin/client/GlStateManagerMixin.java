package com.qendolin.farz.mixin.client;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDepthFunc(I)V", remap = false), method = "_depthFunc", index = 0, remap = false)
	private static int reverseDepthFunc(int func) {
		return switch (func) {
			case GL11.GL_LESS -> GL11.GL_GREATER;
			case GL11.GL_GREATER -> GL11.GL_LESS;
			case GL11.GL_LEQUAL -> GL11.GL_GEQUAL;
			case GL11.GL_GEQUAL -> GL11.GL_LEQUAL;
			default -> func;
		};
	}
}