package mingli29.jebb.client.render;

import mingli29.jebb.item.JebbItemGroups;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class JebbBannerRenderer {
    public static final int WIDTH = 162;

    private static final int TILE = 16;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private JebbBannerRenderer() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, String labelKey, JebbItemGroups.SectionBannerStyle style) {
        int height = bannerHeight(style);
        BlockState backdrop = backdropState(style);

        Minecraft mc = Minecraft.getInstance();
        TextureAtlasSprite sprite = mc.getBlockRenderer()
                .getBlockModelShaper()
                .getParticleIcon(backdrop);

        for (int i = 0; i < 10; i++) {
            graphics.blit(x + i * TILE, y, 0, TILE, height, sprite);
        }
        graphics.blit(x + WIDTH - TILE, y, 0, TILE, height, sprite);

        graphics.fill(x, y, x + WIDTH, y + 1, BORDER_COLOR);
        graphics.fill(x, y + height - 1, x + WIDTH, y + height, BORDER_COLOR);

        Font font = mc.font;
        Component title = Component.translatable(labelKey);
        int textWidth = font.width(title);
        int textX = x + (WIDTH - textWidth) / 2;
        int textY = y + (height - font.lineHeight) / 2 + 1;
        graphics.drawString(font, title, textX, textY, TEXT_COLOR, true);
    }

    private static int bannerHeight(JebbItemGroups.SectionBannerStyle style) {
        return switch (style) {
            case DEFAULT -> 16;
            case VARIANT_VERTICAL_SLAB, VARIANT_CORNER_PILLAR, VARIANT_QUARTER, VARIANT_HORIZONTAL_SLAB -> 16;
        };
    }

    private static BlockState backdropState(JebbItemGroups.SectionBannerStyle style) {
        return switch (style) {
            case DEFAULT -> Blocks.STRIPPED_OAK_LOG.defaultBlockState();
            case VARIANT_VERTICAL_SLAB, VARIANT_HORIZONTAL_SLAB -> Blocks.OAK_SLAB.defaultBlockState();
            case VARIANT_CORNER_PILLAR -> Blocks.STONE_BRICKS.defaultBlockState();
            case VARIANT_QUARTER -> Blocks.POLISHED_ANDESITE.defaultBlockState();
        };
    }
}
