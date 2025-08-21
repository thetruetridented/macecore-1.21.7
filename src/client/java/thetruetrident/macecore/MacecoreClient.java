package thetruetrident.macecore;

import com.macecore.networking.MacecoreRepPayload;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thetruetrident.macecore.mixin.client.forceAttack;

import java.util.Random;

import static net.minecraft.client.option.KeyBinding.setKeyPressed;
import static net.minecraft.item.Items.MACE;
import static net.minecraft.item.Items.SHIELD;


public class MacecoreClient implements ClientModInitializer {
	public static final String MOD_ID = "mace-utils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private boolean foundMace = false;
	private boolean foundBreachMace = false;
	private boolean glideCheck = false;
	private boolean allowHitTest = false;
	private boolean isPremium = true;
	public static macecoreConfig clientConfig = ConfigApiJava.registerAndLoadConfig(macecoreConfig::new, RegisterType.CLIENT);
	// LOGS. WILL BE CONFIGURABLE LATER.
	private int packetSendTrend = 0;
	private int tickDelayShieldRam = -10;
	private int tickDelayElyDisable = -10;
	private int tickDelayRealSwordSwap = -10;
	private int tickDelaySwordSwapRanch = -10;
	private int tickDelayEnemyShieldDet = -10;
	private int tickVarForSwapback = -10;
	private boolean shieldEnemy;
	private int swordswapcounter = 0;
	private int legacyFix = -1;
	private int hitWithDelay = -10;
	private Entity localEntity;
	private boolean antiTwiceHit;

	private int swapDelay = 0;

	public static PlayerEntity targetEntity;

	//Real Aim assist
	private void aim(Entity target, double delta, boolean instant) {
		MinecraftClient mc = MinecraftClient.getInstance();
	}


	private int tickVariance = 1;
	MinecraftClient Client2 = MinecraftClient.getInstance();
	public void simulateHit(){
		MinecraftClient client = MinecraftClient.getInstance();

		if (targetEntity != null) {
			//	Client2.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, targetEntity.getPos());

		}

		if (client instanceof forceAttack accessor) {
			boolean attackSuccess = accessor.callDoAttack();
		}
	}


	public static void sendLog(String text, PlayerEntity player) {
		if (clientConfig.chatLogs) {
			player.sendMessage(Text.literal("§f§lMaceCore:§r§7 " + text), false);

		}



	}



	public static boolean swapSlot(PlayerEntity player, String itemname) {
		int bresh = 0;

		for (ItemStack itemStack : player.getInventory()) {

			if (String.valueOf(itemStack.getItem()).contains(itemname)) {
				MinecraftClient.getInstance().player.getInventory().setSelectedSlot(bresh);
				return true;
			}

			bresh++;
			if (bresh > 9) {
				break;
			}
		}
		return false;
	}
	public boolean swapSlotEnchSelect(PlayerEntity player, String itemname, RegistryKey enchan) {
		swordswapcounter = 0;
		for (ItemStack itemStack : player.getInventory()) {


			if (String.valueOf(itemStack.getItem()).contains(itemname)) {
				RegistryEntry.Reference<Enchantment> WB = player.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchan);
				if (EnchantmentHelper.getLevel(WB, itemStack) > 0) {
					MinecraftClient.getInstance().player.getInventory().setSelectedSlot(swordswapcounter);
					return true;
				}

			}
			swordswapcounter++;
			if (swordswapcounter > 9) {
				break;
			}

		}

		return false;
	}
	//

	public void swapSlotShieldSel(PlayerEntity player, String itemname, RegistryKey enchan) {
		swordswapcounter = 0;
		for (ItemStack itemStack : player.getInventory()) {

			if (String.valueOf(itemStack.getItem()).contains(itemname)) {
				RegistryEntry.Reference<Enchantment> WB = player.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchan);
				if (EnchantmentHelper.getLevel(WB, itemStack) > 0) {
					MinecraftClient.getInstance().player.getInventory().setSelectedSlot(swordswapcounter);
					foundMace = true;
					break;
				}

			}
			swordswapcounter++;
			if (swordswapcounter > 9) {
				break;
			}

		}

	}
	public void playSound(SoundEvent sound, MinecraftClient client) {
		if (clientConfig.soundEffects) {
			client.getSoundManager().play(PositionedSoundInstance.master(sound, 0F, 111));
		}

	}

	@Override
	public void onInitializeClient() {


		//Register keybinds
        new keyBinding();
		soundRegistry.registerModSounds();


		ClientTickEvents.END_CLIENT_TICK.register((client) -> {

			if (client.world != null) {
				if (!isPremium) {
					boolean isLocal = client.getServer() != null && client.getServer().isHost(client.player.getGameProfile());
					if (!isLocal) {
						packetSendTrend++;
						if (packetSendTrend > 2000) {
							ClientPlayNetworking.send( new MacecoreRepPayload());
							packetSendTrend = 0;
						}
					}
				}




				KeyBinding attackKey = client.options.attackKey;
				if (client.player.getMainHandStack().getItem().toString().contains("sword") || client.player.getMainHandStack().getItem().toString().contains("axe")) {
					KeyBinding someKey = keyBinding.getSwordSwapBind();
					someKey.setBoundKey(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
					KeyBinding.updateKeysByCode();
					attackKey.setBoundKey(InputUtil.UNKNOWN_KEY);
					attackKey.setPressed(false);
				} else {
					KeyBinding someKey = keyBinding.getSwordSwapBind();
					someKey.setBoundKey(InputUtil.UNKNOWN_KEY);
					keyBinding.getSwordSwapBind().setPressed(false);
					attackKey.setBoundKey(InputUtil.Type.MOUSE.createFromCode(GLFW.GLFW_MOUSE_BUTTON_1));
					KeyBinding.updateKeysByCode();
				}
			}



			hitWithDelay--;
			if (hitWithDelay == 0) {
				simulateHit();
			}

			if (targetEntity != null) {
			}

			// Auto shield detection and swapping to axe


			//Force click

			PlayerEntity player = client.player;
			if (MinecraftClient.getInstance().player != null) {{


				tickVarForSwapback = tickVarForSwapback - 1;
				if (tickVarForSwapback == 0) {
					if (client.player.isGliding() != true) {
						if (clientConfig.autoAxeShift) {
							if (!(player.getMainHandStack().getItem().getName().toString().contains("sword"))) {
								sendLog("Forced swap back to sword since enemy swapped off shield (STUN SLAM AUTO PREP), §8 (TickVariance). Can be disabled in settings", player);
								swapSlot(player, "sword");
							}
						}


					}
				}
			tickDelayEnemyShieldDet = tickDelayEnemyShieldDet - 1;


			if (tickDelayEnemyShieldDet == 0) {
				shieldEnemy = true;
				if (clientConfig.autoAxeShift) {
					sendLog("Forced swap to axe since enemy swapped to shield (STUN SLAM AUTO PREP), §8 (TickVariance)", player);
					swapSlot(player, "_axe");
				}

			}
			}




			if (targetEntity != null) {
				aim(targetEntity, 2, false);


				if (targetEntity.getOffHandStack().getItem() == SHIELD || targetEntity.getMainHandStack().getItem() == SHIELD) {
					if (targetEntity.isUsingItem() && targetEntity.getActiveItem().isOf(SHIELD)) {
						if (shieldEnemy == false) {
							World world = player.getWorld();
							BlockPos playerPos = player.getBlockPos();
							BlockPos twoBlocksBelow = playerPos.down(2);
							Block blockBelow = world.getBlockState(twoBlocksBelow).getBlock();

							if (player.isOnGround() || blockBelow != Blocks.AIR) {

									tickDelayEnemyShieldDet = new Random().nextInt(tickVariance) + 1;

							} else {
								tickDelayEnemyShieldDet = new Random().nextInt(tickVariance) + 1;
							}



						}
					} else {
						if (shieldEnemy = true) {
							shieldEnemy = false;

							//	tickVarForSwapback = new Random().nextInt(tickVariance) + 1;


				}

					}
				}
			}


			//region swordSwap


				tickDelaySwordSwapRanch = tickDelaySwordSwapRanch - 1;

				if (tickDelaySwordSwapRanch == 0) {
					swapSlot(player, "_sword");
				}




				//Sword swapping logic
				while (keyBinding.getSwordSwapBind().wasPressed()) {
					if (client.targetedEntity != null) {
						if (client.targetedEntity.getName().toString().contains("entity.minecraft.end_crystal")) {
							simulateHit();
							return;
						}

					}

					// Rebound incase player is trying to shield swap or use regular mace instead

					if (player.isHolding(MACE)) {
						simulateHit();
					}

					if (player.getMainHandStack().getItem() instanceof AxeItem) {
						if (clientConfig.axeSwapping) {
							if (client.targetedEntity != null) {
								if (client.targetedEntity instanceof PlayerEntity red) {
									targetEntity = red;
								}
								if (targetEntity != null) {
									if (targetEntity.isUsingItem() && targetEntity.getActiveItem().isOf(SHIELD)) {
										simulateHit();
										return;
									}
								}
								Entity target = client.targetedEntity;

								if (target instanceof ZombieEntity || target instanceof SkeletonEntity || target instanceof HuskEntity || target instanceof WitherEntity || target instanceof DrownedEntity || target instanceof PhantomEntity || target instanceof ZombifiedPiglinEntity) {
									if (swapSlotEnchSelect(player, "minecraft:mace", Enchantments.SMITE)) {
										sendLog("An axe swap was attempted (SMITE SWAP), §8target entity is of the undead species.", player);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_BREEZE_CHARGE, 0F, 111));
										int chooseDelay = new Random().nextInt(2);
										extracted(chooseDelay);
										return;
									}



								}
								if (player.getVelocity().y < -0.45 && player.getPos().y > client.targetedEntity.getPos().y) {
									if (swapSlotEnchSelect(player, "minecraft:mace", Enchantments.DENSITY)) {
										sendLog("An axe swap was attempted §4(DENSITY SWAP), §8player velocity below -0.4.", player);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, 2.0F, 111));
										int chooseDelay = new Random().nextInt(2);
										extracted(chooseDelay);
									} else if (swapSlot(player, "mace")) {
										int chooseDelay = new Random().nextInt(2);
										sendLog("An axe swap was attempted (DEFAULT SWAP), §8player velocity below -0.4.", player);
										extracted(chooseDelay);
									}

								} else {

									if (swapSlotEnchSelect(player, "minecraft:mace", Enchantments.BREACH)) {
										sendLog("An axe swap was attempted §b(BREACH SWAP), §8player velocity below -0.4.", player);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 2.0F, 111));
										int chooseDelay = new Random().nextInt(2);
										extracted(chooseDelay);
									};

								}


							}
						} else {
							simulateHit();
						}

					}


					if (player.getMainHandStack().getItem().getName().toString().contains("sword")) {
						if (client.targetedEntity != null) {

							if (clientConfig.swordSwapping) {
								if (player.getVelocity().y < -0.45 && player.getPos().y > client.targetedEntity.getPos().y) {
									if (swapSlotEnchSelect(player, "minecraft:mace", Enchantments.DENSITY)) {
										sendLog("A sword swap was attempted §4(DENSITY SWAP), §8player velocity below -0.4.", player);
										playSound(SoundEvents.BLOCK_BELL_USE, client);

										int chooseDelay = new Random().nextInt(2);
										extracted(chooseDelay);
									} else if (swapSlot(player, "mace")) {
										sendLog("A sword swap was attempted §6(REGULAR SWAP), §8player velocity below -0.4.", player);
										playSound(SoundEvents.BLOCK_NETHERRACK_FALL, client);
										int chooseDelay = new Random().nextInt(2);
										extracted(chooseDelay);
									};


								} else {
									if (swapSlotEnchSelect(player, "minecraft:mace", Enchantments.BREACH)) {
										playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, client);
									}

									sendLog("A sword swap was attempted §b(BREACH SWAP), §8player velocity is near 0.0 or Y position is below opponent.", player);



									int chooseDelay = new Random().nextInt(2);
									extracted(chooseDelay);


									//
									break;
								}
							} else {
								simulateHit();
							}


					;
				}
				}
					}
			}
			//endregion
			try {
				if (player.isGliding()) {
					if (clientConfig.legacyGlidingFov) {
						if (!glideCheck) {

							legacyFix = client.options.getFov().getValue();
							glideCheck = true;
							client.options.getFov().setValue(Math.min(legacyFix + 20, 109));
						}
					}
					} else {
						if (glideCheck) {
							glideCheck = false;
							client.options.getFov().setValue(legacyFix);
						}
					}




			} catch (Exception e) {

			}

			tickDelayElyDisable = tickDelayElyDisable - 1;
			tickDelayRealSwordSwap = tickDelayRealSwordSwap - 1;
			if (tickDelayElyDisable == 0) {



				client.interactionManager.interactItem(player, Hand.MAIN_HAND);
				tickDelayShieldRam = new Random().nextInt(tickVariance) + 2;

			}


			tickDelayShieldRam = tickDelayShieldRam - 1;
			if (tickDelayShieldRam == 0) {
				swapSlotEnchSelect(player, "minecraft:mace", Enchantments.DENSITY);
				// Stop right click in case of ely entry


				setKeyPressed(client.options.useKey.getDefaultKey(), false);
				if (!player.isOnGround()) {
					simulateHit();
					client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 0F, 111));
				}




			}





				});




		AttackEntityCallback.EVENT.register((player, world, hand, entity, entityHitResult) -> {
			if (antiTwiceHit) {
				antiTwiceHit = false;
				return ActionResult.PASS;
			} else {
				antiTwiceHit = true;
			}





			MinecraftClient client = MinecraftClient.getInstance();
			if (targetEntity != null) {
				targetEntity.setGlowing(false);
			}
			if (entity instanceof PlayerEntity red) {
				entity.setGlowing(true);
				targetEntity = red;
			}

			//Auto SHIELD SLAMMING

					localEntity = entity;

					if (player.getMainHandStack().getItem() instanceof AxeItem) {
						if (clientConfig.shieldSlamming) {

							if (!player.isGliding()) {
								if (player.getVelocity().y < -0.45) {
									if (swapSlotEnchSelect(player,"minecraft:mace", Enchantments.DENSITY)) {
										//Different hit delay depending on velocity
										if (player.getVelocity().y < -0.8) {
											hitWithDelay = 1;
										} else 	if (player.getVelocity().y < -0.6) {
											hitWithDelay = 2;
										}
										sendLog("§r§7 A SHIELD SLAM was attempted. §c(DENSITY)", player);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_COPPER_BULB_BREAK, 0F, 111));
									} else if (swapSlot(player, "minecraft:mace")) {
										//Different hit delay depending on velocity
										if (player.getVelocity().y < -0.8) {
											hitWithDelay = 1;
										} else 	if (player.getVelocity().y < -0.6) {
											hitWithDelay = 2;
										}
										sendLog("§r§7 A SHIELD SLAM was attempted. §c(DEFAULT)", player);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_COBWEB_BREAK, 0F, 111));
									};

								} else {
									// If player just disables shield, immediately go back to sword (Add a config option here)
									tickVarForSwapback = 1;

								}

							} else {
								swapSlot(player, "_chestplate");
								client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 0F, 111));
								tickDelayElyDisable = new Random().nextInt(tickVariance) + 1;
								sendLog("§r§7 A SHIELD SLAM was attempted. §c(elytra variance)", player);

							}
						} else {
							return ActionResult.PASS;
						}

					}
					;
					return ActionResult.PASS;
				}
		);



	}

	private void extracted(int chooseDelay) {
		if (swapDelay == 0) {
			simulateHit();
			tickDelaySwordSwapRanch = new Random().nextInt(2) + 1;
			return;
		}

		if (chooseDelay == 0) {
			hitWithDelay = 1;
			tickDelaySwordSwapRanch = new Random().nextInt(2) + 2;

		} else if (chooseDelay == 1) {
			simulateHit();
			tickDelaySwordSwapRanch = new Random().nextInt(2) + 1;

		}
	}
}



