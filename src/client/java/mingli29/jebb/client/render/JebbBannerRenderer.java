package mingli29.jebb.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

public final class JebbBannerRenderer {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 18;

    private static final int TILE = 16;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private JebbBannerRenderer() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, String labelKey) {
        Minecraft mc = Minecraft.getInstance();
        TextureAtlasSprite sprite = mc.getBlockRenderer()
                .getBlockModelShaper()
                .getParticleIcon(Blocks.STONE_BRICKS.defaultBlockState());

        for (int i = 0; i < 10; i++) {
            graphics.blit(x + i * TILE, y, 0, TILE, HEIGHT, sprite);
        }
        graphics.blit(x + WIDTH - TILE, y, 0, TILE, HEIGHT, sprite);

        graphics.fill(x, y, x + WIDTH, y + 1, BORDER_COLOR);
        graphics.fill(x, y + HEIGHT - 1, x + WIDTH, y + HEIGHT, BORDER_COLOR);

        Font font = mc.font;
        Component title = Component.translatable(labelKey);
        int textWidth = font.width(title);
        int textX = x + (WIDTH - textWidth) / 2;
        int textY = y + (HEIGHT - font.lineHeight) / 2 + 1;
        graphics.drawString(font, title, textX, textY, TEXT_COLOR, true);
    }
}
