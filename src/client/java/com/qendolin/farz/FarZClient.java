package com.qendolin.farz;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL45;


public class FarZClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("FarZ");


    private static boolean vanilla;
    private static boolean zeroToOne;
    private static boolean floatingPointDepth;
    private static boolean infinite;
    private static boolean reverse;
    public static int invalidate = 0;
    public static final RenderTargetPatcher renderTargetPatcher = new RenderTargetPatcher();

    static {
        if (System.getProperty("farz.vanilla") != null) {
            vanilla = true;
        }
        if (System.getProperty("farz.zeroToOne") != null) {
            zeroToOne = true;
        }
        if (System.getProperty("farz.floatingPointDepth") != null) {
            floatingPointDepth = true;
        }
        if (System.getProperty("farz.infinite") != null) {
            infinite = true;
        }
    }

    @Override
    public void onInitializeClient() {

        printStatus(null);

        KeyMapping toggleVanilla = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.farz.vanilla",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.farz.main"));

        KeyMapping toggleZeroToOne = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.farz.zeroToOne",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.farz.main"));

        KeyMapping toggleFloatingPoint = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.farz.floatingPoint",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                "category.farz.main"));

        KeyMapping toggleInfiniteKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.farz.infinite",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.farz.main"));

        KeyMapping toggleReverseKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                "key.farz.reverse",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.farz.main"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVanilla.consumeClick()) {
                FarZClient.vanilla = !FarZClient.vanilla;
                printStatus(client.player);
                invalidate++;
            }

            while (toggleZeroToOne.consumeClick()) {
                FarZClient.zeroToOne = !FarZClient.zeroToOne;
                if (FarZClient.zeroToOne) {
                    GL45.glClipControl(GL45.GL_LOWER_LEFT, GL45.GL_ZERO_TO_ONE);
                } else {
                    GL45.glClipControl(GL45.GL_LOWER_LEFT, GL45.GL_NEGATIVE_ONE_TO_ONE);
                }
                printStatus(client.player);
                invalidate++;
            }

            while (toggleFloatingPoint.consumeClick()) {
                FarZClient.floatingPointDepth = !FarZClient.floatingPointDepth;
                printStatus(client.player);
                invalidate++;

                renderTargetPatcher.updateBuffers();
            }

            while (toggleInfiniteKey.consumeClick()) {
                FarZClient.infinite = !FarZClient.infinite;
                printStatus(client.player);
                invalidate++;
            }

            while (toggleReverseKey.consumeClick()) {
                FarZClient.reverse = !FarZClient.reverse;
                printStatus(client.player);
                invalidate++;
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            LocalPlayer player = client.player;
            if (player != null) {
                printStatus(player);
                player.displayClientMessage(Component.literal("Use keys Y/U/I/O/P to change!"), false);
            }
        });
    }

    private static void printStatus(LocalPlayer player) {
        if(player == null) {
            LOGGER.info("Vanilla (Override): {}", vanilla);
            LOGGER.info("Floating Point: {}", floatingPointDepth);
            LOGGER.info("Infinite: {}", infinite);
            LOGGER.info("Zero To One: {}", zeroToOne);
            LOGGER.info("Reverse: {}", reverse);
            return;
        }

        player.displayClientMessage(Component.literal("--------------------"), false);
        player.displayClientMessage(Component.literal("Vanilla (Override): " + vanilla), false);
        player.displayClientMessage(Component.literal("Floating Point: " + floatingPointDepth), false);
        player.displayClientMessage(Component.literal("Infinite: " + infinite), false);
        player.displayClientMessage(Component.literal("Zero To One: " + zeroToOne), false);
        player.displayClientMessage(Component.literal("Reverse: " + reverse), false);
    }

    public static boolean vanilla() {
        return vanilla;
    }

    public static boolean infinite() {
        if (vanilla) return false;
        return infinite;
    }

    public static boolean normal() {
        if (vanilla) return true;
        return !reverse;
    }

    public static boolean zeroToOne() {
        if (vanilla) return false;
        return zeroToOne;
    }

    public static boolean floatingPointDepth() {
        if (vanilla) return false;
        return floatingPointDepth;
    }
}