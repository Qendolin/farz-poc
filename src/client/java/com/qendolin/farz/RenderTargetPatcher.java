package com.qendolin.farz;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RenderTargetPatcher {

    private final List<WeakReference<RenderTarget>> renderTargets = new ArrayList<>();

    public void addRenderTarget(RenderTarget renderTarget) {
        if (renderTarget != null) {
            renderTargets.add(new WeakReference<>(renderTarget));
        }
    }

    public void updateBuffers() {
        int w = Minecraft.getInstance().getWindow().getWidth();
        int h = Minecraft.getInstance().getWindow().getHeight();

        for (WeakReference<RenderTarget> weakRef : renderTargets) {
            RenderTarget renderTarget = weakRef.get();
            if (renderTarget != null && renderTarget.getDepthTexture() != null) {
                if(renderTarget.getDepthTexture().isClosed()) continue;
                renderTarget.destroyBuffers();
                renderTarget.createBuffers(w, h);
            }
        }
    }
}
