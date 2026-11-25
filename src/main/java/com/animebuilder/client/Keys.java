package com.animebuilder.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class Keys {
    private Keys() {}
    public static final String CAT = "key.categories.animebuilder";
    public static final KeyMapping ROTATE = new KeyMapping("key.animebuilder.rotate", GLFW.GLFW_KEY_R, CAT);
    public static final KeyMapping LOCK = new KeyMapping("key.animebuilder.lock", GLFW.GLFW_KEY_Z, CAT);
    public static final KeyMapping BUILD = new KeyMapping("key.animebuilder.build", GLFW.GLFW_KEY_B, CAT);
    public static final KeyMapping CANCEL = new KeyMapping("key.animebuilder.cancel", GLFW.GLFW_KEY_X, CAT);
}
