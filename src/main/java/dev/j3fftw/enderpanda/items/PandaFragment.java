package dev.j3fftw.enderpanda.items;

import dev.j3fftw.enderpanda.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.skull.SkullItem;
import org.bukkit.inventory.ItemStack;

public class PandaFragment extends SlimefunItem {

    private static final ItemStack panda = new CustomItem(SkullItem.fromHash(
        "d188c980aacfa94cf33088512b1b9517ba826b154d4cafc262aff6977be8a"), "&8panda");

    public PandaFragment() {
        super(Items.ENDER_PANDA_CATEGORY, Items.PANDA_FRAGMENT, RecipeType.MOB_DROP, new ItemStack[] {
            null, null, null,
            null, panda, null,
            null, null, null
        });
    }
}
