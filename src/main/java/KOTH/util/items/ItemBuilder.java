package KOTH.util.items;

import KOTH.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/11/2017 at 1:55 AM.
 */
public class ItemBuilder implements Listener{

    private ItemStack is;

    private static Plugin plugin = Main.get().getServer().getPluginManager().getPlugin("KingOfTheHill");
    private static boolean listener = false;
    private static final HashMap<String, PotionEffect> effects = new HashMap<String, PotionEffect>();

    public ItemBuilder(ItemStack itemStack){
        this.is = itemStack;
    }

    public ItemBuilder(Material material){
        this(new ItemStack(material, 1));
    }

    public ItemBuilder(Material material, int amount){
        this(new ItemStack(material, amount));
    }

    public ItemBuilder name(final String name){
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(final int amount){
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(final String... strings){
        ItemMeta meta = is.getItemMeta();
        meta.setLore(Arrays.asList(strings));
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final String lore){
        ItemMeta meta = is.getItemMeta();
        List<String> l = meta.getLore();
        if(l == null){
            l = new ArrayList<String>();
        }
        l.add(lore);
        meta.setLore(l);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(final int durability){
        is.setDurability((short) durability);
        return this;
    }

    public ItemBuilder data(final int data){
        is.setData(new MaterialData(is.getType(), (byte) data));
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment, final int level){
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment){
        is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder enchantAll(final HashMap<Enchantment, Integer> enchants){
        is.addUnsafeEnchantments(enchants);
        return this;
    }

    public ItemBuilder type(final Material type){
        is.setType(type);
        return this;
    }

    public ItemBuilder unbreakable(final boolean unbreakable){
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(unbreakable);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(){
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(true);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearLore(){
        ItemMeta meta = is.getItemMeta();
        meta.setLore(new ArrayList<String>());
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchants(){
        for (final Enchantment e : is.getEnchantments().keySet()) {
            is.removeEnchantment(e);
        }
        return this;
    }

    public ItemBuilder color(Color color){
        if(new ItemUtil(is).isLeather()){
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(color);
            is.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("color() method only applicable for leather armor");
    }

    public ItemBuilder effect(PotionEffect effect){
        if(!listener){
            Bukkit.getPluginManager().registerEvents(this, plugin);
            listener = true;
        }
        String name = is.getItemMeta().getDisplayName();
        while(effects.containsKey(name)){
            name += "#";
        }
        effects.put(name, effect);
        return this;
    }

    public ItemBuilder effect(PotionEffectType type, int duration, int amp, boolean ambient){
        return effect(new PotionEffect(type, duration == -1 ? 999999999 : duration, amp, ambient));
    }

    public ItemBuilder effect(PotionEffectType type, int duration, int amp){
        return effect(new PotionEffect(type, duration == -1 ? 999999999 : duration, amp));
    }

    public ItemBuilder effect(PotionEffectType type, int duration){
        return effect(new PotionEffect(type, duration == -1 ? 999999999 : duration, 1));
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e){
        if(e.getItem().hasItemMeta()){
            HashMap<String, PotionEffect> copy = (HashMap<String, PotionEffect>) effects.clone();
            String name = e.getItem().getItemMeta().getDisplayName();
            while (copy.containsKey(name)){
                e.getPlayer().addPotionEffect(copy.get(name), true);
                copy.remove(name);
                name += "#";
            }
        }
    }
}
