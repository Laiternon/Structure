package com.animebuilder.client;

import com.animebuilder.AnimeBuilderMod;
import com.animebuilder.catalog.Catalog;
import com.animebuilder.network.Net;
import com.animebuilder.structure.Structure;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AnimeBuilderMod.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private static int ticks;

    private ClientEvents() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) { ClientState.stopPlacing(); return; }
        if (ClientState.placingId == null) return;

        Structure s = Catalog.get(ClientState.placingId);
        if (s == null) { ClientState.stopPlacing(); return; }

        while (Keys.ROTATE.consumeClick()) ClientState.rot = (ClientState.rot + 1) & 3;
        while (Keys.CANCEL.consumeClick()) {
            ClientState.stopPlacing();
            mc.player.displayClientMessage(Component.literal("Предпросмотр отменён"), true);
            return;
        }
        while (Keys.LOCK.consumeClick()) {
            if (ClientState.locked) { ClientState.locked = false; ClientState.lockedPos = null; }
            else { BlockPos a = ClientState.anchor(); if (a != null) { ClientState.locked = true; ClientState.lockedPos = a; } }
        }
        while (Keys.BUILD.consumeClick()) {
            BlockPos a = ClientState.anchor();
            if (a == null) {
                mc.player.displayClientMessage(Component.literal("§cНаведите прицел на блок земли"), true);
            } else {
                Net.CH.sendToServer(new Net.BuildRequest(s.id, a, ClientState.rot));
                ClientState.stopPlacing();
                return;
            }
        }

        if (++ticks % 20 == 0) ClientHooks.requestScan(s.id);
        if (ticks % 5 == 0) {
            Boolean ok = ClientState.hasEnough(s);
            String res = ok == null ? "§7ресурсы: проверка…" : ok ? "§aресурсов хватает" : "§cне хватает ресурсов";
            mc.player.displayClientMessage(Component.literal("§f" + s.name.getString() + " §7| §e" + Keys.ROTATE.getTranslatedKeyMessage().getString()
                    + "§7 повернуть  §e" + Keys.LOCK.getTranslatedKeyMessage().getString() + "§7 "
                    + (ClientState.locked ? "освободить" : "зафиксировать") + "  §e" + Keys.BUILD.getTranslatedKeyMessage().getString()
                    + "§7 построить  §e" + Keys.CANCEL.getTranslatedKeyMessage().getString() + "§7 отмена §7| " + res), true);
        }
    }

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (ClientState.placingId == null) return;
        Structure s = Catalog.get(ClientState.placingId);
        BlockPos a = ClientState.anchor();
        if (s == null || a == null) return;
        GhostRenderer.render(e.getPoseStack(), e.getCamera().getPosition(), s, ClientState.rot, a, ClientState.hasEnough(s));
    }
}
