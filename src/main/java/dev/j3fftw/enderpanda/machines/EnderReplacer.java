package dev.j3fftw.enderpanda.machines;

import dev.j3fftw.enderpanda.Items;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;

public class EnderReplacer extends SlimefunItem implements EnergyNetComponent {

    private static final int ENERGY_CONSUMPTION = 60;
    private static final int ENERGY_CAPACITY = 1024;


    public EnderReplacer() {
        super(
            Items.ENDER_PANDA_CATEGORY,
            new SlimefunItemStack("ENDER_REPLACER", Material.STONE, "&5Ender Replacer",
                "Somekind of lore", "Somekind of lore"),
            RecipeType.ANCIENT_ALTAR,
            new ItemStack[] {
                SlimefunItems.RUNE_ENDER, SlimefunItems.STEEL_PLATE, SlimefunItems.RUNE_ENDER,
                SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_PLATE,
                SlimefunItems.RUNE_ENDER, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE
            }
        );

        setupInterface();

        addItemHandler((ItemHandler) onTick());
    }

    private void setupInterface() {
        new BlockMenuPreset(id, "&5Ender Replacer") {

            @Override
            public void init() {
                for (int i = 0; i < 27; i++) {
                    this.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                //todo add power
                this.addItem(12, new CustomItem(Material.GUNPOWDER, "&5Power"), ChestMenuUtils.getEmptyClickHandler());
                this.addItem(14, null, (player, i, itemStack, clickAction) -> {
                    ItemStack is = player.getItemOnCursor();
                    return is.getType() == Material.BAMBOO || itemStack.getType() != Material.AIR;
                });
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventroy.bypass")
                    || SlimefunPlugin.getProtectionManager()
                    .hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[14];
            }

        };
    }


    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 1024;
    }

    private Object onTick() {
        return new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(@Nonnull Block b, @Nonnull SlimefunItem item, @Nonnull Config config) {
                BlockMenu inv = BlockStorage.getInventory(b);
                if (!Bukkit.getAllowEnd()
                    || !b.getWorld().getUID().equals(Bukkit.getWorlds().get(!Bukkit.getAllowNether() ? 1 : 2).getUID())
                    || getCapacity() > ENERGY_CAPACITY
                )
                    return;
                Collection<Entity> entities =
                    b.getWorld().getNearbyEntities(b.getLocation(), 7, 3, 7,
                        ent -> ent.getType() == EntityType.ENDERMAN);
                if (entities.isEmpty()) return;
                for (Entity e : entities) {
                    for (int slot : getInputSlots()) {
                        e.remove();
                        b.getWorld().spawnEntity(e.getLocation(), EntityType.PANDA);
                        b.getWorld().spawnParticle(Particle.DRAGON_BREATH, e.getLocation(), 1);
                        if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), Items.SPECIAL_BAMBOO, false)) {
                            if (ChargableBlock.getCharge(b) < ENERGY_CONSUMPTION) return;

                            ChargableBlock.addCharge(b, -ENERGY_CONSUMPTION);
                            inv.consumeItem(slot);

                        }
                    }
                }
            }

            private int[] getInputSlots() {
                return new int[] {14};
            }
        };
    }
}


