package com.animebuilder.client;

import com.animebuilder.network.Net;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class ClientHooks {
    private ClientHooks() {}

    public static void openScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        HitResult hr = mc.hitResult;
        ClientState.lastTarget = (hr instanceof BlockHitResult b && hr.getType() == HitResult.Type.BLOCK) ? b.getBlockPos() : null;
        // Make the front of the building face the player.
        Direction d = mc.player.getDirection();
        ClientState.rot = d.get2DDataValue();
        mc.setScreen(new BlueprintScreen());
    }

    public static void requestScan(String id) {
        if (id != null) Net.CH.sendToServer(new Net.ScanRequest(id));
    }
}
