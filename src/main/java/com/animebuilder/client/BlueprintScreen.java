package com.animebuilder.client;

import com.animebuilder.catalog.Catalog;
import com.animebuilder.network.Net;
import com.animebuilder.structure.Structure;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BlueprintScreen extends Screen {
    private static final int LEFT_W = 172, RIGHT_W = 188, ROW_H = 22, MAT_H = 20;
    private static final int FULL_BRIGHT = 15728880;
    private static final int PREVIEW_MAX_BLOCKS = 7000;

    private final List<String> cats = Catalog.categories();
    private int catIdx = 0;
    private List<String> ids = new ArrayList<>();
    private String selected;
    private List<Map.Entry<Item, Integer>> mats = new ArrayList<>();
    private int listScroll, matScroll;
    private float yaw = 45f, pitch = 25f, zoom = 1f;
    private int tickCount;

    private Button rotateBtn, previewBtn, buildBtn;

    public BlueprintScreen() { super(Component.literal("Чертежи аниме-построек")); loadCategory(); }

    private void loadCategory() {
        ids = cats.isEmpty() ? new ArrayList<>() : Catalog.idsIn(cats.get(catIdx));
        listScroll = 0;
    }

    private void select(String id) {
        selected = id; matScroll = 0;
        Structure s = Catalog.get(id);
        mats = new ArrayList<>();
        if (s != null) {
            mats.addAll(s.materials().entrySet());
            mats.sort(Comparator.comparingInt((Map.Entry<Item, Integer> e) -> e.getValue()).reversed());
        }
        ClientHooks.requestScan(id);
    }

    // layout helpers
    private int previewX1() { return LEFT_W + 14; }
    private int previewX2() { return width - RIGHT_W - 14; }
    private int previewY1() { return 30; }
    private int previewY2() { return height - 38; }

    @Override
    protected void init() {
        int x1 = previewX1(), x2 = previewX2();
        addRenderableWidget(Button.builder(Component.literal("<"), b -> { catIdx = Math.floorMod(catIdx - 1, Math.max(1, cats.size())); loadCategory(); })
                .bounds(8, 8, 20, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> { catIdx = Math.floorMod(catIdx + 1, Math.max(1, cats.size())); loadCategory(); })
                .bounds(LEFT_W - 12, 8, 20, 20).build());

        int bw = (x2 - x1 - 8) / 3;
        int by = height - 30;
        rotateBtn = addRenderableWidget(Button.builder(Component.literal("Повернуть"), b -> ClientState.rot = (ClientState.rot + 1) & 3)
                .bounds(x1, by, bw, 20).build());
        previewBtn = addRenderableWidget(Button.builder(Component.literal("Предпросмотр в мире"), b -> startWorldPreview())
                .bounds(x1 + bw + 4, by, bw, 20).build());
        buildBtn = addRenderableWidget(Button.builder(Component.literal("Построить здесь"), b -> buildHere())
                .bounds(x1 + 2 * (bw + 4), by, bw, 20).build());
    }

    private void startWorldPreview() {
        if (selected == null) return;
        ClientState.placingId = selected;
        ClientState.locked = false;
        ClientState.lockedPos = null;
        onClose();
    }

    private void buildHere() {
        if (selected == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (ClientState.lastTarget == null) {
            if (mc.player != null) mc.player.displayClientMessage(Component.literal("§cОткройте чертёж, глядя на блок земли — или используйте предпросмотр в мире."), true);
            onClose();
            return;
        }
        Net.CH.sendToServer(new Net.BuildRequest(selected, ClientState.lastTarget, ClientState.rot));
        onClose();
    }

    @Override
    public void tick() {
        if (selected != null && ++tickCount % 20 == 0) ClientHooks.requestScan(selected);
        boolean has = selected != null;
        previewBtn.active = has;
        buildBtn.active = has;
        rotateBtn.active = has;
        rotateBtn.setMessage(Component.literal("Повернуть (" + (ClientState.rot * 90) + "°)"));
    }

    @Override public boolean isPauseScreen() { return false; }

    // ------------------------------------------------------------------ rendering
    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBackground(g);
        // panels
        g.fill(4, 4, LEFT_W + 4, height - 4, 0xCC0B0B12);
        g.fill(width - RIGHT_W - 8, 4, width - 4, height - 4, 0xCC0B0B12);

        // category title
        String cat = cats.isEmpty() ? "—" : cats.get(catIdx);
        g.drawCenteredString(font, font.plainSubstrByWidth(cat, LEFT_W - 50), LEFT_W / 2 + 4, 14, 0xFFD966);

        // list
        int listTop = 36, listBottom = height - 10;
        g.enableScissor(6, listTop, LEFT_W + 2, listBottom);
        for (int i = 0; i < ids.size(); i++) {
            int y = listTop + i * ROW_H - listScroll;
            if (y + ROW_H < listTop || y > listBottom) continue;
            String id = ids.get(i);
            boolean sel = id.equals(selected);
            boolean hov = mx >= 6 && mx <= LEFT_W + 2 && my >= Math.max(y, listTop) && my < Math.min(y + ROW_H - 2, listBottom);
            g.fill(8, y, LEFT_W, y + ROW_H - 2, sel ? 0xFF3B5BA5 : hov ? 0xFF2A2A44 : 0xFF181826);
            g.drawString(font, font.plainSubstrByWidth(Catalog.nameOf(id), LEFT_W - 18), 12, y + 7, sel ? 0xFFFFFF : 0xCCCCCC, false);
        }
        g.disableScissor();

        // right panel
        int rx = width - RIGHT_W - 2;
        Structure s = selected == null ? null : Catalog.get(selected);
        if (s != null) {
            g.drawString(font, font.plainSubstrByWidth(s.name.getString(), RIGHT_W - 8), rx, 10, 0xFFD966, false);
            g.drawString(font, s.sizeX + " × " + s.sizeY + " × " + s.sizeZ + "  |  блоков: " + s.blockCount(), rx, 22, 0xAAAAAA, false);
            Boolean ok = ClientState.hasEnough(s);
            Component status = ok == null ? Component.literal("Проверяю ресурсы…").withStyle(net.minecraft.ChatFormatting.GRAY)
                    : ok ? Component.literal("Ресурсов хватает").withStyle(net.minecraft.ChatFormatting.GREEN)
                    : Component.literal("Не хватает ресурсов").withStyle(net.minecraft.ChatFormatting.RED);
            g.drawString(font, status, rx, 34, 0xFFFFFF, false);
            g.drawString(font, "Инвентарь + сундуки рядом", rx, 46, 0x777777, false);

            int top = 60, bottom = height - 10;
            g.enableScissor(rx - 2, top, width - 6, bottom);
            for (int i = 0; i < mats.size(); i++) {
                int y = top + i * MAT_H - matScroll;
                if (y + MAT_H < top || y > bottom) continue;
                Map.Entry<Item, Integer> e = mats.get(i);
                int have = ClientState.have(selected, e.getKey());
                int need = e.getValue();
                g.renderItem(new ItemStack(e.getKey()), rx, y);
                String nm = font.plainSubstrByWidth(e.getKey().getDescription().getString(), RIGHT_W - 78);
                g.drawString(font, nm, rx + 20, y + 4, 0xE0E0E0, false);
                String cnt = (have < 0 ? "?" : String.valueOf(have)) + "/" + need;
                int col = have < 0 ? 0xAAAAAA : have >= need ? 0x55FF55 : 0xFF5555;
                g.drawString(font, cnt, width - 10 - font.width(cnt), y + 4, col, false);
            }
            g.disableScissor();
        }

        super.render(g, mx, my, pt);          // buttons
        renderPreview(g, previewX1(), previewY1(), previewX2(), previewY2());   // last: 3D content writes depth
    }

    private void renderPreview(GuiGraphics g, int x1, int y1, int x2, int y2) {
        g.fill(x1, y1, x2, y2, 0xCC12141C);
        if (selected == null) {
            g.drawCenteredString(font, "Выберите постройку слева", (x1 + x2) / 2, (y1 + y2) / 2 - 4, 0x888888);
            return;
        }
        Structure s = Catalog.get(selected);
        if (s == null) return;
        Minecraft mc = Minecraft.getInstance();
        Structure.Placement pl = s.get(ClientState.rot);

        g.enableScissor(x1, y1, x2, y2);
        RenderSystem.clear(256, Minecraft.ON_OSX);
        RenderSystem.enableDepthTest();
        PoseStack ps = g.pose();
        ps.pushPose();
        double diag = Math.sqrt((double) s.sizeX * s.sizeX + (double) s.sizeY * s.sizeY + (double) s.sizeZ * s.sizeZ);
        float scale = (float) (Math.min(x2 - x1, y2 - y1) / diag * 0.9 * zoom);
        ps.translate((x1 + x2) / 2f, (y1 + y2) / 2f, 150f);
        ps.scale(scale, -scale, scale);
        ps.mulPose(Axis.XP.rotationDegrees(pitch));
        ps.mulPose(Axis.YP.rotationDegrees(yaw));
        ps.translate(0f, -s.sizeY / 2f, 0f);
        Lighting.setupFor3DItems();

        MultiBufferSource.BufferSource buf = g.bufferSource();
        List<Structure.Entry> list = pl.visible();
        int step = Math.max(1, list.size() / PREVIEW_MAX_BLOCKS);
        for (int i = 0; i < list.size(); i += step) {
            Structure.Entry e = list.get(i);
            ps.pushPose();
            ps.translate(e.x(), e.y(), e.z());
            mc.getBlockRenderer().renderSingleBlock(e.state(), ps, buf, FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }
        buf.endBatch();
        ps.popPose();
        Lighting.setupForFlatItems();
        g.disableScissor();
        g.drawString(font, "ЛКМ-перетаскивание: вращать • колесо: масштаб", x1 + 4, y2 - 11, 0x666666, false);
    }

    // ------------------------------------------------------------------ input
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) return true;
        int listTop = 36, listBottom = height - 10;
        if (button == 0 && mx >= 6 && mx <= LEFT_W + 2 && my >= listTop && my < listBottom) {
            int idx = (int) ((my - listTop + listScroll) / ROW_H);
            if (idx >= 0 && idx < ids.size()) { select(ids.get(idx)); return true; }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (button == 0 && mx >= previewX1() && mx <= previewX2() && my >= previewY1() && my <= previewY2()) {
            yaw += (float) dx * 0.8f;
            pitch = Mth.clamp(pitch + (float) dy * 0.5f, -15f, 85f);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        if (mx < LEFT_W + 4) {
            int max = Math.max(0, ids.size() * ROW_H - (height - 46));
            listScroll = Mth.clamp(listScroll - (int) (delta * 18), 0, max);
            return true;
        }
        if (mx > width - RIGHT_W - 8) {
            int max = Math.max(0, mats.size() * MAT_H - (height - 70));
            matScroll = Mth.clamp(matScroll - (int) (delta * 18), 0, max);
            return true;
        }
        zoom = Mth.clamp(zoom + (float) delta * 0.1f, 0.3f, 3f);
        return true;
    }
}
