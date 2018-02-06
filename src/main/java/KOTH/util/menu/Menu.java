package KOTH.util.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Mike (c) 2017. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 9/11/2017 at 1:40 AM.
 */
public class Menu implements InventoryHolder {

    private HashMap<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();
    private Inventory inv;
    private String title;
    private InventoryHolder holder;
    private int rows;
    private boolean exitOnClickOutside = true;
    private MenuCloseBehavior mcb;

    public Menu(String title, int rows, InventoryHolder holder){
        this.title = title;
        this.rows = rows;
        this.holder = holder;
    }

    public void setMenuCloseBehavior(MenuCloseBehavior mcb){
        this.mcb = mcb;
    }

    public MenuCloseBehavior getMenuCloseBehavior() {
        return mcb;
    }

    public void setExitOnClickOutside(boolean exitOnClickOutside) {
        this.exitOnClickOutside = exitOnClickOutside;
    }

    public boolean exitOnClickOutside() {
        return exitOnClickOutside;
    }

    public boolean addMenuItem(MenuItem item, int x, int y){
        return addMenuItem(item, (y * 9) + x);
    }

    public boolean addMenuItem(MenuItem item, int index){
        ItemStack slot = getInventory().getItem(index);
        if(slot != null && slot.getType() != Material.AIR) return false;
        getInventory().setItem(index, item.getItemStack());
        items.put(index, item);
        item.addToMenu(this);
        return true;
    }

    public boolean removeMenuItem(int x, int y){
        return removeMenuItem((y * 9) + x);
    }

    public boolean removeMenuItem(int index){
        ItemStack slot = getInventory().getItem(index);
        if(slot == null || slot.getType() == Material.AIR) return false;
        getInventory().clear(index);
        items.remove(index).removeFromMenu(this);
        return true;
    }

    protected void selectMenuItem(Player player, int index){
        if(items.containsKey(index)){
            MenuItem item = items.get(index);
            item.onClick(player);
        }
    }


    public void openMenu(Player player){
        if(getInventory().getViewers().contains(player)){
            throw new IllegalArgumentException(player.getName() + " is already viewing " + getInventory().getTitle());
        }
        player.openInventory(getInventory());
    }

    public void closeMenu(Player player){
        if(getInventory().getViewers().contains(player)){
            getInventory().getViewers().remove(player);
            player.closeInventory();
        }
    }

    public void switchMenu(Player player, Menu newMenu){
        MenuAPI.switchMenu(player, this, newMenu);
    }

    public Inventory getInventory() {
        if(inv == null){
            inv = Bukkit.createInventory(this, rows * 9, title);
        }
        return inv;
    }

    @Override
    protected Menu clone(){
        Menu clone = new Menu(title, rows, holder);
        clone.setExitOnClickOutside(exitOnClickOutside);
        clone.setMenuCloseBehavior(mcb);
        for(int index : items.keySet()){
            addMenuItem(items.get(index), index);
        }
        return clone;
    }

    public void updateMenu(){
        for(HumanEntity entity : getInventory().getViewers()){
            if(entity instanceof Player){
                Player player = (Player) entity;
                player.updateInventory();
            }
        }
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public void setHolder(InventoryHolder holder) {
        this.holder = holder;
    }
}
