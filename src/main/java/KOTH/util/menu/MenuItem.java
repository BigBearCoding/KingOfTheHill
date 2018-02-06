package KOTH.util.menu;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/11/2017 at 1:53 AM.
 */
public abstract class MenuItem {

    private Menu menu;
    private int id;
    private MaterialData icon;
    private ItemMeta savedMeta;
    private String name;
    private int amount = 1;
    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    private List<String> descriptions = new ArrayList<String>();

    public MenuItem(ItemStack stack){
        this(stack.getItemMeta().getDisplayName(), stack.getData(), stack.getAmount());
    }

    public MenuItem(String text){
        this(text, new MaterialData(Material.PAPER));
    }

    public MenuItem(String text, MaterialData data){
        this(text, data, 1);
    }

    public MenuItem(String text, MaterialData data, int amount){
        this.name = text;
        this.icon = data;
        this.amount = amount;
    }

    protected void addToMenu(Menu menu){
        this.menu = menu;
    }

    protected void removeFromMenu(Menu menu){
        if(this.menu == menu) this.menu = null;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getId() {
        return id;
    }

    public MaterialData getIcon() {
        return icon;
    }

    public ItemMeta getSavedMeta() {
        return savedMeta;
    }

    public void setSavedMeta(ItemMeta savedMeta) {
        this.savedMeta = savedMeta;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescriptions(List<String> lines){
        this.descriptions = lines;
    }

    public void addDescription(String line){
        descriptions.add(line);
    }

    public void addDescriptions(String... lines){
        for(String s : lines){
            addDescription(s);
        }
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public void addEnchantment(Enchantment enchantment, int level){
        this.enchantments.put(enchantment, level);
    }

    public void addEnchantment(Enchantment enchantment){
        this.addEnchantment(enchantment, enchantment.getMaxLevel());
    }

    protected ItemStack getItemStack(){
        ItemStack slot = new ItemStack(getIcon().getItemType(), getAmount());
        slot.addUnsafeEnchantments(enchantments);
        if(this.savedMeta == null){
            ItemMeta meta = slot.getItemMeta();
            meta.setLore(descriptions);
            meta.setDisplayName(name);
            slot.setItemMeta(meta);
        }else slot.setItemMeta(this.getSavedMeta());
        return slot;
    }

    public abstract void onClick(Player player);

}
