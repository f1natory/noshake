package no.noshake.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class HeldItemRendererMixin {
    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void noshake$disableHandRotationSmoothing(
            float tickDelta,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            LocalPlayer player,
            int light,
            CallbackInfo ci
    ) {
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        player.yBob = yaw;
        player.yBobO = yaw;
        player.xBob = pitch;
        player.xBobO = pitch;
    }
}
