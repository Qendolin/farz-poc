package com.qendolin.farz;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarZClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("FarZ");

	public static final String MODE;
	public static final boolean ZERO_TO_ONE;
	public static final boolean FLOATING_POINT_DEPTH;

	static {
		String mode = System.getProperty("farz.mode");
		if(mode == null) mode = "reverse";
		MODE = mode;
		String fallback = System.getProperty("farz.fallback");
		if(fallback != null) {
			ZERO_TO_ONE = false;
			FLOATING_POINT_DEPTH = false;
		} else {
			ZERO_TO_ONE = true;
			FLOATING_POINT_DEPTH = true;
		}
	}

	@Override
	public void onInitializeClient() {
        LOGGER.info("FarZ Mode: {}", MODE);
        LOGGER.info("Zero To One Depth: {}", ZERO_TO_ONE);
        LOGGER.info("Floating Point Depth: {}", FLOATING_POINT_DEPTH);
	}
}