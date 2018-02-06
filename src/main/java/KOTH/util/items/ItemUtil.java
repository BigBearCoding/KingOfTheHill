package KOTH.util.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/12/2017 at 1:35 AM.
 */
public class ItemUtil {
    
    private ItemStack is;

    public ItemUtil(ItemStack itemStack) {
        this.is = itemStack;
    }
    
    public boolean isLeather(){
        return is.getType() == Material.LEATHER_HELMET || 
                is.getType() == Material.LEATHER_CHESTPLATE ||
                is.getType() == Material.LEATHER_LEGGINGS ||
                is.getType() == Material.LEATHER_BOOTS;
    }
    
    public boolean isArmor(){
        return isLeather() ||
                is.getType() == Material.CHAINMAIL_HELMET ||
                is.getType() == Material.CHAINMAIL_CHESTPLATE ||
                is.getType() == Material.CHAINMAIL_LEGGINGS ||
                is.getType() == Material.CHAINMAIL_BOOTS ||
                is.getType() == Material.GOLD_HELMET ||
                is.getType() == Material.GOLD_CHESTPLATE ||
                is.getType() == Material.GOLD_LEGGINGS ||
                is.getType() == Material.GOLD_BOOTS ||
                is.getType() == Material.IRON_HELMET ||
                is.getType() == Material.IRON_CHESTPLATE ||
                is.getType() == Material.IRON_LEGGINGS ||
                is.getType() == Material.IRON_BOOTS ||
                is.getType() == Material.DIAMOND_HELMET ||
                is.getType() == Material.DIAMOND_CHESTPLATE ||
                is.getType() == Material.DIAMOND_LEGGINGS ||
                is.getType() == Material.DIAMOND_BOOTS;
    }
}
