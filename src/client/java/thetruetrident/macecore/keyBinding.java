package thetruetrident.macecore;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class keyBinding {
    public static final KeyBinding SWORD_SWAP_FINAL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.maceutils.swordswap", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_M, // The keycode of the key
            "category.maceutils.main" // The translation key of the keybinding's category.
    ));

    public static KeyBinding getSwordSwapBind() {
        return SWORD_SWAP_FINAL;
    }
}

