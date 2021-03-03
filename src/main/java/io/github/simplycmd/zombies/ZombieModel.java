package io.github.simplycmd.zombies;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class ZombieModel extends EntityModel<Zombie> {
    private final ModelPart base;

    public ZombieModel() {
        this.textureHeight = 64;
        this.textureWidth = 64;
        base = new ModelPart(this, 0, 0);
        base.addCuboid(-6, -12, -6, 12, 24, 12);
    }

    @Override
    public void setAngles(Zombie entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        // translate model down
        matrices.translate(0, 1.125, 0);

        // render cube
        base.render(matrices, vertices, light, overlay);
    }
}