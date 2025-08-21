package thetruetrident.macecore;

import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.util.Identifier;

import static thetruetrident.macecore.MacecoreClient.MOD_ID;

public class macecoreConfig extends Config {
    public macecoreConfig() {
        super(Identifier.of(MOD_ID, "core_config"));
    }

    public double tickVariance = 5.0;
    public boolean axeSwapping = true;
    public boolean pearlCatching = false;
    public boolean swordSwapping = true;
    public boolean shieldSlamming = true;
    public boolean autoAxeShift = false;

    public boolean aimAssist = false;
    public double aimAssistLenience = 30.0;
    public float aimAssistSpeedInDegreesPerTick = 5.0f;
    public double aimAssistRangeInBlocks = 5;
    public boolean freeHitting = true;
    public boolean soundEffects = true;
    public boolean legacyGlidingFov = true;
    public boolean chatLogs = false;

}