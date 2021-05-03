package me.zowpy.ztags.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemBuilder implements Listener {
    private ItemStack is;

    public ItemBuilder(Material mat) {
        this.is = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder amount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name) {
        ItemMeta meta = this.is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList();
        }

        ((List)lore).add(name);
        meta.setLore((List)lore);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        List<String> toSet = new ArrayList();
        ItemMeta meta = this.is.getItemMeta();
        String[] var7 = lore;
        int var6 = lore.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            String string = var7[var5];
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> toSet = new ArrayList();
        ItemMeta meta = this.is.getItemMeta();
        Iterator var5 = lore.iterator();

        while(var5.hasNext()) {
            String string = (String)var5.next();
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.is.setDurability((short)durability);
        return this;
    }

    /** @deprecated */
    @Deprecated
    public ItemBuilder data(int data) {
        this.is.setData(new MaterialData(this.is.getType(), (byte)data));
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.is.getItemMeta();
        meta.setLore(new ArrayList());
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        Iterator var2 = this.is.getEnchantments().keySet().iterator();

        while(var2.hasNext()) {
            Enchantment e = (Enchantment)var2.next();
            this.is.removeEnchantment(e);
        }

        return this;
    }

    public ItemBuilder color(Color color) {
        if (this.is.getType() != Material.LEATHER_BOOTS && this.is.getType() != Material.LEATHER_CHESTPLATE && this.is.getType() != Material.LEATHER_HELMET && this.is.getType() != Material.LEATHER_LEGGINGS) {
            throw new IllegalArgumentException("color() only applicable for leather armor!");
        } else {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.is.getItemMeta();
            meta.setColor(color);
            this.is.setItemMeta(meta);
            return this;
        }
    }

    public ItemStack build() {
        return this.is;
    }
}
