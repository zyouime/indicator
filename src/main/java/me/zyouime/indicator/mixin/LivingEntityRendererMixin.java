package me.zyouime.indicator.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zyouime.indicator.util.AnimData;
import me.zyouime.indicator.util.HeartType;
import me.zyouime.indicator.util.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> implements Wrapper {

    private Random random = new Random();
    private Map<Integer, Integer> entitiesHealth = new WeakHashMap<>();
    private Map<Integer, AnimData> entityAnimData = new WeakHashMap<>();
    private static final Identifier GUI_ICONS_TEXTURE = new Identifier("textures/gui/icons.png");

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    private void render(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!SETTINGS.enabled.getValue()) return;
        if (livingEntity instanceof ArmorStandEntity) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.cameraEntity != null && client.world != null) {
            Vec3d entityPos = livingEntity.getCameraPosVec(1.0F);
            Vec3d cameraPos = client.cameraEntity.getCameraPosVec(1.0F);
            BlockHitResult hitResult = client.world.raycast(new RaycastContext(cameraPos, entityPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.cameraEntity));
            if (hitResult.getType() == HitResult.Type.BLOCK) return;
        }

        boolean isClientPlayer = livingEntity instanceof ClientPlayerEntity;
        boolean isPlayer = livingEntity instanceof PlayerEntity;
        boolean isInvisible = livingEntity.isInvisible();

        if (isInvisible) {
            if (isClientPlayer) {
                if (SETTINGS.showYourDisplayName.getValue()) {
                    this.renderLabelIfPresent(livingEntity, livingEntity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
                }
                if (!SETTINGS.showYourHealth.getValue()) return;
            } else {
                if (SETTINGS.showDisplayNameForInvisibleEntities.getValue()) {
                    this.renderLabelIfPresent(livingEntity, livingEntity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
                }
                if (!SETTINGS.showHealthInInvisibleEntities.getValue()) return;
            }
        } else if (isClientPlayer) {
            if (SETTINGS.showYourDisplayName.getValue()) {
                this.renderLabelIfPresent(livingEntity, livingEntity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
            }
            if (!SETTINGS.showYourHealth.getValue()) return;
        }

        if (isPlayer) {
            if (isNotSurvivalMode((PlayerEntity) livingEntity)) return;
            if (isClientPlayer) {
                if (!SETTINGS.showYourHealth.getValue()) return;
            } else {
                if (!SETTINGS.showOnPlayers.getValue()) return;
            }
        } else if (!SETTINGS.showOnMobs.getValue()) return;

        matrixStack.push();
        double d = this.dispatcher.getSquaredDistanceToCamera(livingEntity);
        float pixelSize = SETTINGS.heartsSize.getValue() / 1000f;
        float offset = (pixelSize - 0.025f) * 10;
        float heartsOffset = SETTINGS.heartsOffset.getValue();
        if (!(livingEntity instanceof PlayerEntity)) {
            if (livingEntity.hasCustomName() && !livingEntity.isInvisible()) {
                heartsOffset += 0.25f;
            } else if (livingEntity.isInvisible() && livingEntity.getDisplayName() != null) {
                heartsOffset += 0.25f;
            }
        }

        float posTOP = livingEntity.getHeight() + heartsOffset + offset;
        matrixStack.translate(0, posTOP, 0);
        if (livingEntity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) livingEntity;
            if (d <= 4096.0) {
                matrixStack.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0D);
                if (d < 100.0 && player.getScoreboard().getObjectiveForSlot(2) != null) {
                    matrixStack.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0D);
                }
            }
        }
        int ticks = livingEntity.age;
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.scale(pixelSize, pixelSize, pixelSize);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        vertexConsumer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        RenderSystem.enableDepthTest();
        Matrix4f model = matrixStack.peek().getPositionMatrix();

        int health, healthYellow;
        int maxHealth = MathHelper.ceil(livingEntity.getMaxHealth());
        if (livingEntity instanceof PlayerEntity && SETTINGS.scoreboardMode.getValue()) {
            Scoreboard scoreboard = ((PlayerEntity) livingEntity).getScoreboard();
            health = scoreboard.getPlayerScore(livingEntity.getName().getString(), scoreboard.getObjectiveForSlot(2)).getScore();
            healthYellow = 0;
        } else {
            health = MathHelper.ceil(livingEntity.getHealth());
            healthYellow = MathHelper.ceil(livingEntity.getAbsorptionAmount());
        }

        int entityId = livingEntity.getId();
        long l = Util.getMeasuringTimeMs();
        AnimData animData = entityAnimData.get(entityId);
        if (animData == null) {
            animData = new AnimData();
            entityAnimData.put(entityId, animData);
        }
        Integer lastHealth = entitiesHealth.get(entityId);
        if (lastHealth != null) {
            if (health < lastHealth) {
                animData.lastHealthCheckTime = l;
                animData.heartJumpEndTick = ticks + 20;
            } else if (health > lastHealth) {
                animData.lastHealthCheckTime = l;
                animData.heartJumpEndTick = ticks + 10;
            }
            if (l - animData.lastHealthCheckTime > 1000L) {
                animData.lastHealthCheckTime = l;
                animData.heartJumpEndTick = 0;
            }
        }
        entitiesHealth.put(entityId, health);
        boolean bl = animData.heartJumpEndTick > ticks && (animData.heartJumpEndTick - ticks) / 3L % 2L == 1L;

        this.random.setSeed(ticks * 312871L);
        int hearts = MathHelper.ceil(health / 2.0f);
        boolean lastHalf = (health & 1) == 1;
        int heartsNormal = MathHelper.ceil(maxHealth / 2.0f);
        int heartsYellow = MathHelper.ceil(healthYellow / 2.0f);
        boolean lastYellowHalf = (healthYellow & 1) == 1;
        int heartsTotal = heartsNormal + heartsYellow;
        int maxHeartsPerRow = SETTINGS.inOneRow.getValue() ? heartsTotal : 10;
        int rowsTotal = (heartsTotal + maxHeartsPerRow - 1) / maxHeartsPerRow;
        int rowOffset = Math.max(10 - (rowsTotal - 2), 3);
        int pixelsTotal = maxHeartsPerRow * 8 + 1;
        float maxXY = pixelsTotal / 2.0f;
        for (int heart = 0; heart < heartsTotal; heart++) {
            int row = heart / maxHeartsPerRow;
            int col = heart % maxHeartsPerRow;
            float x = maxXY - col * 8;
            float y = row * rowOffset;
            float z = row * 0.01F;
            if (heartsTotal < maxHeartsPerRow) {
                int a = 10 - heartsTotal;
                x -= (8 * a) / 2f;
            }
            if (health <= 4) {
                y += this.random.nextInt(2);
            }
            if (bl && SETTINGS.animation.getValue()) {
                drawHeart(model, vertexConsumer, x, y, z, HeartType.BLINK_EMPTY);
            } else {
                drawHeart(model, vertexConsumer, x, y, z, HeartType.EMPTY);
            }
            HeartType type;
            if (heart < hearts) {
                type = HeartType.RED_FULL;
                if (heart == hearts - 1 && lastHalf) {
                    type = HeartType.RED_HALF;
                }
            } else if (heart < heartsNormal) {
                type = HeartType.EMPTY;
            } else {
                type = HeartType.YELLOW_FULL;
                if (heart == heartsTotal - 1 && lastYellowHalf) {
                    type = HeartType.YELLOW_HALF;
                }
            }
            if (type != HeartType.EMPTY) {
                drawHeart(model, vertexConsumer, x, y, z, type);
            }
        }
        tessellator.draw();
        matrixStack.pop();
        RenderSystem.enableDepthTest();
    }

    private static void drawHeart(Matrix4f model, VertexConsumer vertexConsumer, float x, float y, float z, HeartType type) {
        float minU = type.u / 256F;
        float maxU = minU + 9F / 256F;
        float minV = type.v / 256F;
        float maxV = minV + 9F / 256F;
        float heartSize = 9F;
        drawVertex(model, vertexConsumer, x, y - heartSize, z, minU, maxV);
        drawVertex(model, vertexConsumer, x - heartSize, y - heartSize, z, maxU, maxV);
        drawVertex(model, vertexConsumer, x - heartSize, y, z, maxU, minV);
        drawVertex(model, vertexConsumer, x, y, z, minU, minV);
    }

    private static void drawVertex(Matrix4f model, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(model, x, y, z).texture(u, v).next();
    }

    private boolean isNotSurvivalMode(PlayerEntity player) {
        return player.isSpectator() || player.isCreative();
    }
}
