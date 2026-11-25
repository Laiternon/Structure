package com.animebuilder.client;

import com.animebuilder.structure.Structure;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/** Draws a translucent tinted copy of the structure in the world. */
public final class GhostRenderer {
    private static final int MAX_BLOCKS = 6000;
    private static final double MAX_DIST_SQ = 96.0 * 96.0;
    private static final int FULL_BRIGHT = 15728880;

    private GhostRenderer() {}

    /** Forces every block into the translucent buffer with tint + alpha. */
    private static final class TintedSource implements MultiBufferSource {
        final MultiBufferSource.BufferSource src; final float r, g, b, a;
        TintedSource(MultiBufferSource.BufferSource src, float r, float g, float b, float a) { this.src = src; this.r = r; this.g = g; this.b = b; this.a = a; }
        @Override public VertexConsumer getBuffer(RenderType type) { return new Tint(src.getBuffer(RenderType.translucent()), r, g, b, a); }
    }

    private static final class Tint implements VertexConsumer {
        final VertexConsumer d; final float r, g, b, a;
        Tint(VertexConsumer d, float r, float g, float b, float a) { this.d = d; this.r = r; this.g = g; this.b = b; this.a = a; }
        @Override public VertexConsumer vertex(double x, double y, double z) { d.vertex(x, y, z); return this; }
        @Override public VertexConsumer color(int cr, int cg, int cb, int ca) { d.color((int) (cr * r), (int) (cg * g), (int) (cb * b), (int) (ca * a)); return this; }
        @Override public VertexConsumer uv(float u, float v) { d.uv(u, v); return this; }
        @Override public VertexConsumer overlayCoords(int u, int v) { d.overlayCoords(u, v); return this; }
        @Override public VertexConsumer uv2(int u, int v) { d.uv2(u, v); return this; }
        @Override public VertexConsumer normal(float x, float y, float z) { d.normal(x, y, z); return this; }
        @Override public void endVertex() { d.endVertex(); }
        @Override public void defaultColor(int cr, int cg, int cb, int ca) { d.defaultColor(cr, cg, cb, ca); }
        @Override public void unsetDefaultColor() { d.unsetDefaultColor(); }
    }

    public static void render(PoseStack ps, Vec3 cam, Structure s, int rot, BlockPos anchor, Boolean enough) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
        Structure.Placement pl = s.get(rot);

        float r = 1f, g = 1f, b = 1f;
        if (enough != null) { if (enough) { r = 0.7f; g = 1f; b = 0.7f; } else { r = 1f; g = 0.55f; b = 0.55f; } }
        TintedSource ghost = new TintedSource(buf, r, g, b, 0.6f);

        List<Structure.Entry> list = pl.visible();
        int step = Math.max(1, list.size() / MAX_BLOCKS);
        for (int i = 0; i < list.size(); i += step) {
            Structure.Entry e = list.get(i);
            BlockPos p = anchor.offset(e.x(), e.y(), e.z());
            double dx = p.getX() + 0.5 - cam.x, dy = p.getY() + 0.5 - cam.y, dz = p.getZ() + 0.5 - cam.z;
            if (dx * dx + dy * dy + dz * dz > MAX_DIST_SQ) continue;
            BlockState st = e.state();
            ps.pushPose();
            ps.translate(p.getX() - cam.x, p.getY() - cam.y, p.getZ() - cam.z);
            mc.getBlockRenderer().renderSingleBlock(st, ps, ghost, FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }
        buf.endBatch(RenderType.translucent());

        // bounding box outline
        AABB box = new AABB(anchor.getX() + pl.minX(), anchor.getY(), anchor.getZ() + pl.minZ(),
                anchor.getX() + pl.maxX() + 1, anchor.getY() + s.sizeY, anchor.getZ() + pl.maxZ() + 1).move(-cam.x, -cam.y, -cam.z);
        LevelRenderer.renderLineBox(ps, buf.getBuffer(RenderType.lines()), box, r, g, b, 1f);
        buf.endBatch(RenderType.lines());
    }
}
