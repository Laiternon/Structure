package com.animebuilder.network;

import com.animebuilder.AnimeBuilderMod;
import com.animebuilder.BlueprintItem;
import com.animebuilder.build.BuildService;
import com.animebuilder.catalog.Catalog;
import com.animebuilder.client.ClientState;
import com.animebuilder.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Net {
    private static final String V = "1";
    public static final SimpleChannel CH = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AnimeBuilderMod.MODID, "main"), () -> V, V::equals, V::equals);

    private Net() {}

    public static void init() {
        int i = 0;
        CH.registerMessage(i++, BuildRequest.class, BuildRequest::encode, BuildRequest::decode, BuildRequest::handle);
        CH.registerMessage(i++, ScanRequest.class, ScanRequest::encode, ScanRequest::decode, ScanRequest::handle);
        CH.registerMessage(i++, ScanResult.class, ScanResult::encode, ScanResult::decode, ScanResult::handle);
    }

    private static boolean hasBlueprint(ServerPlayer p) {
        if (p.getAbilities().instabuild) return true;
        Inventory inv = p.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack st = inv.getItem(i);
            if (st.getItem() instanceof BlueprintItem) return true;
        }
        return false;
    }

    // ---------------------------------------------------------------- client -> server: build
    public static class BuildRequest {
        final String id; final BlockPos pos; final int rot;
        public BuildRequest(String id, BlockPos pos, int rot) { this.id = id; this.pos = pos; this.rot = rot; }

        static void encode(BuildRequest m, FriendlyByteBuf b) { b.writeUtf(m.id, 64); b.writeBlockPos(m.pos); b.writeByte(m.rot); }
        static BuildRequest decode(FriendlyByteBuf b) { return new BuildRequest(b.readUtf(64), b.readBlockPos(), b.readByte()); }

        static void handle(BuildRequest m, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context c = ctx.get();
            c.enqueueWork(() -> {
                ServerPlayer p = c.getSender();
                if (p == null) return;
                Structure s = Catalog.get(m.id);
                if (s == null) return;
                if (!hasBlueprint(p)) { p.sendSystemMessage(Component.literal("§cНужен чертёж в инвентаре.")); return; }
                if (p.distanceToSqr(Vec3.atCenterOf(m.pos)) > BuildService.MAX_DISTANCE * (double) BuildService.MAX_DISTANCE) {
                    p.sendSystemMessage(Component.literal("§cСлишком далеко от места постройки."));
                    return;
                }
                BuildService.tryBuild(p, s, m.pos, Math.floorMod(m.rot, 4));
            });
            c.setPacketHandled(true);
        }
    }

    // ---------------------------------------------------------------- client -> server: resource scan
    public static class ScanRequest {
        final String id;
        public ScanRequest(String id) { this.id = id; }

        static void encode(ScanRequest m, FriendlyByteBuf b) { b.writeUtf(m.id, 64); }
        static ScanRequest decode(FriendlyByteBuf b) { return new ScanRequest(b.readUtf(64)); }

        static void handle(ScanRequest m, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context c = ctx.get();
            c.enqueueWork(() -> {
                ServerPlayer p = c.getSender();
                if (p == null) return;
                Structure s = Catalog.get(m.id);
                if (s == null) return;
                Map<Item, Integer> have = BuildService.scan(p, s);
                Map<String, Integer> out = new LinkedHashMap<>();
                for (Map.Entry<Item, Integer> e : have.entrySet()) out.put(BuildService.keyOf(e.getKey()), e.getValue());
                CH.send(PacketDistributor.PLAYER.with(() -> p), new ScanResult(m.id, out));
            });
            c.setPacketHandled(true);
        }
    }

    // ---------------------------------------------------------------- server -> client: scan result
    public static class ScanResult {
        public final String id; public final Map<String, Integer> have;
        public ScanResult(String id, Map<String, Integer> have) { this.id = id; this.have = have; }

        static void encode(ScanResult m, FriendlyByteBuf b) {
            b.writeUtf(m.id, 64);
            b.writeVarInt(m.have.size());
            for (Map.Entry<String, Integer> e : m.have.entrySet()) { b.writeUtf(e.getKey(), 128); b.writeVarInt(e.getValue()); }
        }
        static ScanResult decode(FriendlyByteBuf b) {
            String id = b.readUtf(64);
            int n = b.readVarInt();
            Map<String, Integer> map = new LinkedHashMap<>();
            for (int i = 0; i < n; i++) map.put(b.readUtf(128), b.readVarInt());
            return new ScanResult(id, map);
        }
        static void handle(ScanResult m, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context c = ctx.get();
            c.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientState.onScan(m)));
            c.setPacketHandled(true);
        }
    }

    @SuppressWarnings("unused")
    private static Item itemOf(String key) { return ForgeRegistries.ITEMS.getValue(new ResourceLocation(key)); }
}
