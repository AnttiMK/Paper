package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.meta.trim.ArmorTrim;

public record PaperItemArmorTrim(
    net.minecraft.world.item.equipment.trim.ArmorTrim impl
) implements ItemArmorTrim, Handleable<net.minecraft.world.item.equipment.trim.ArmorTrim> {

    @Override
    public net.minecraft.world.item.equipment.trim.ArmorTrim getHandle() {
        return this.impl;
    }

    @Override
    public boolean showInTooltip() {
        return this.impl.showInTooltip();
    }

    @Override
    public ItemArmorTrim showInTooltip(final boolean showInTooltip) {
        return new PaperItemArmorTrim(this.impl.withTooltip(showInTooltip));
    }

    @Override
    public ArmorTrim armorTrim() {
        return new ArmorTrim(CraftTrimMaterial.minecraftHolderToBukkit(this.impl.material()), CraftTrimPattern.minecraftHolderToBukkit(this.impl.pattern()));
    }

    static final class BuilderImpl implements ItemArmorTrim.Builder {

        private ArmorTrim armorTrim;
        private boolean showInTooltip = true;

        BuilderImpl(final ArmorTrim armorTrim) {
            this.armorTrim = armorTrim;
        }

        @Override
        public ItemArmorTrim.Builder showInTooltip(final boolean showInTooltip) {
            this.showInTooltip = showInTooltip;
            return this;
        }

        @Override
        public ItemArmorTrim.Builder armorTrim(final ArmorTrim armorTrim) {
            this.armorTrim = armorTrim;
            return this;
        }

        @Override
        public ItemArmorTrim build() {
            return new PaperItemArmorTrim(new net.minecraft.world.item.equipment.trim.ArmorTrim(
                CraftTrimMaterial.bukkitToMinecraftHolder(this.armorTrim.getMaterial()),
                CraftTrimPattern.bukkitToMinecraftHolder(this.armorTrim.getPattern()),
                this.showInTooltip
            ));
        }
    }
}
