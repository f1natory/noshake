package no.noshake.api.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import no.noshake.api.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Redirect(
            method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView")
    )
    private void noshake$customBob(GameRenderer instance, CameraRenderState camera, PoseStack poseStack) {
        ConfigManager config = ConfigManager.get();
        if (!config.isCustomBob()) {
            ((GameRendererInvoker) instance).noshake$invokeBobView(camera, poseStack);
            return;
        }

        CameraEntityRenderState entity = camera.entityRenderState;
        if (entity == null || !entity.isPlayer) {
            return;
        }

        float drop = entity.bob * config.getDownStrength();
        if (drop != 0.0f) {
            poseStack.translate(0.0f, -drop, 0.0f);
        }
    }
}
