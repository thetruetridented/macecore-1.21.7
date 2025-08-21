package thetruetrident.macecore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import static thetruetrident.macecore.MacecoreClient.*;


public class PearlCatcher implements ClientModInitializer {
    int shiftWithDelay = 0;
//	ClientTickEvents.END_CLIENT_TICK.register((client) -> {
    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (clientConfig.pearlCatching) {
                if (shiftWithDelay == 1) {
                    sendLog("A §5§l(PEARL CATCH) §9was attempted" + client.player.getPitch(), client.player);
                    swapSlot(client.player, "charge");
                    MinecraftClient mc = MinecraftClient.getInstance();

                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_TRIAL_SPAWNER_DETECT_PLAYER, 0F, 111));
                    if (mc.player.getPitch() >= -89 && mc.player.getPitch() <= -66) {
                        mc.player.setPitch(mc.player.getPitch() + 2);

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);

                    }
                    if (mc.player.getPitch() >= -66 && mc.player.getPitch() <= -56) {
                        mc.player.setPitch(mc.player.getPitch() + 4);

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);

                    }
                    if (mc.player.getPitch() >= -56 && mc.player.getPitch() <= -50) {
                        mc.player.setPitch(mc.player.getPitch() + 5);

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    }
                    if (mc.player.getPitch() >= -90 && mc.player.getPitch() <= -89) {

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    }

                    if (mc.player.getPitch() >= -50 && mc.player.getPitch() <= -40) {
                        mc.player.setPitch(mc.player.getPitch() + 8);

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    }
                    if (mc.player.getPitch() >= -40 && mc.player.getPitch() <= -30) {
                        mc.player.setPitch(mc.player.getPitch() + 11);

                        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    }


                }

                shiftWithDelay = shiftWithDelay - 1;
            }

        });



        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            if (itemStack.getItem() == Items.ENDER_PEARL) {
                    if (player.getPitch() < -25) {


                    shiftWithDelay = 3;


                return ActionResult.PASS;
            }}

            return ActionResult.PASS;
        });


}}