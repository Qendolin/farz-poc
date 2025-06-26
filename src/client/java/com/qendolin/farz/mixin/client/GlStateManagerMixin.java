package com.qendolin.farz.mixin.client;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.qendolin.farz.FarZClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {
	@ModifyVariable(at = @At(value = "HEAD"), method = "_depthFunc",remap = false, argsOnly = true)
	private static int reverseDepthFunc(int func) {
		if(FarZClient.normal()) {
			return func;
		}
		return switch (func) {
			case GL11.GL_LESS -> GL11.GL_GREATER;
			case GL11.GL_GREATER -> GL11.GL_LESS;
			case GL11.GL_LEQUAL -> GL11.GL_GEQUAL;
			case GL11.GL_GEQUAL -> GL11.GL_LEQUAL;
			default -> func;
		};
	}
}