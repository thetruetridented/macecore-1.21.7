package thetruetrident.macecore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static thetruetrident.macecore.MacecoreClient.clientConfig;
import static thetruetrident.macecore.MacecoreClient.targetEntity;


public class aimAssist implements ClientModInitializer {

    @Override
    public void onInitializeClient() {


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;

            if (clientConfig.aimAssist && targetEntity != null) {
                if (targetEntity.distanceTo(client.player) < clientConfig.aimAssistRangeInBlocks ) {
                    aimAtEntitySmooth(client, targetEntity, clientConfig.aimAssistSpeedInDegreesPerTick); // 5 degrees per tick
                }

            }
        });
    }

    private void aimAtEntitySmooth(MinecraftClient client, Entity target, float speed) {
        Vec3d playerPos = client.player.getCameraPosVec(1.0F);
        Vec3d targetPos = target.getEyePos();

        double dx = targetPos.x - playerPos.x;
        double dy = targetPos.y - playerPos.y;
        double dz = targetPos.z - playerPos.z;

        double distXZ = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (MathHelper.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        float targetPitch = (float) (-(MathHelper.atan2(dy, distXZ) * (180F / Math.PI)));

        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();


        float yawDiff = Math.abs(MathHelper.wrapDegrees(targetYaw - currentYaw));
        float pitchDiff = Math.abs(MathHelper.wrapDegrees(targetPitch - currentPitch));

        if (yawDiff <= clientConfig.aimAssistLenience && pitchDiff <= clientConfig.aimAssistLenience) {
            float newYaw = approachAngle(currentYaw, targetYaw, speed);
            float newPitch = approachAngle(currentPitch, targetPitch, speed);
            if (client.targetedEntity != targetEntity) {
                client.player.setYaw(newYaw);
                client.player.setPitch(newPitch);
            }

        }
    }

    private float approachAngle(float current, float target, float maxChange) {
        float delta = MathHelper.wrapDegrees(target - current);
        if (delta > maxChange) delta = maxChange;
        if (delta < -maxChange) delta = -maxChange;
        return current + delta;
    }
}