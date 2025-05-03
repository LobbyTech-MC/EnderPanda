package dev.j3fftw.enderpanda.armor;

import dev.j3fftw.enderpanda.Items;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PandaBoots extends SlimefunArmorPiece {

    public PandaBoots() {
        super(Items.ENDER_PANDA_CATEGORY, Items.PANDA_BOOTS, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                        Items.PANDA_FRAGMENT, Items.PANDA_FRAGMENT, Items.PANDA_FRAGMENT,
                        Items.PANDA_FRAGMENT, null, Items.PANDA_FRAGMENT,
                        null, null, null
                }, new PotionEffect[0]
        );
        final ItemStack is = Items.PANDA_BOOTS;
        is.addEnchantment(Enchantment.UNBREAKING, 1);
        is.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 5);
        is.addUnsafeEnchantment(Enchantment.FEATHER_FALLING, 8);
    }
}
