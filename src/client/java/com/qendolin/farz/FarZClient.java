package com.qendolin.farz;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarZClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("FarZ");

	@Override
	public void onInitializeClient() {
		String mode = System.getProperty("farz.mode");
		if(mode == null) mode = "default (reverse)";
        LOGGER.info("FarZ Mode: {}", mode);
	}
}