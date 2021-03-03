package io.github.simplycmd.zombies;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ZombieRenderer extends MobEntityRenderer<Zombie, ZombieModel> {
    public ZombieRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ZombieModel(), 0.5f);
    }

    @Override
    public Identifier getTexture(Zombie entity) {
        return new Identifier(Main.MOD_ID, "textures/entity/zombie/zombie.png");
    }
}