package KOTH.util.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mike (c) 2018. All rights reserved.
 * Any code contained within KOTH (this document), and any associated APIs with similar branding
 * are the sole property of Michael Petramalo.  Distribution, reproduction, taking sections, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with the third-party, you.
 * Thanks.
 * Created on 1/29/2018 at 1:51 PM.
 */
public class MenuAPI implements Listener{

    private static Plugin main;

    public MenuAPI(Plugin plugin){
        main = plugin;
    }

    public static Menu createMenu(String title, int rows, InventoryHolder holder){
        return new Menu(title, rows, holder);
    }

    public static Menu cloneMenu(Menu menu){
        return menu.clone();
    }

    public static void removeMenu(Menu menu){
        for(HumanEntity entity : menu.getInventory().getViewers()){
            if(entity instanceof Player){
                menu.closeMenu((Player) entity);
            }else{
                entity.closeInventory();
            }
        }
    }

    public static void switchMenu(final Player player, Menu fromMenu, final Menu toMenu) {
        fromMenu.closeMenu(player);
        new BukkitRunnable() {
            public void run() {
                toMenu.openMenu(player);
            }
        }.runTask(main);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuItemClicked(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Menu) {
            Menu menu = (Menu) inventory.getHolder();
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                    if (menu.exitOnClickOutside()) {
                        menu.closeMenu(player);
                    }
                } else {
                    int index = event.getRawSlot();
                    if (index < inventory.getSize()) {
                        menu.selectMenuItem(player, index);
                    } else {
                        if (menu.exitOnClickOutside()) {
                            menu.closeMenu(player);
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMenuClosed(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (inventory.getHolder() instanceof Menu) {
                Menu menu = (Menu) inventory.getHolder();
                MenuCloseBehavior menuCloseBehaviour = menu.getMenuCloseBehavior();
                if (menuCloseBehaviour != null) {
                    menuCloseBehaviour.onClose((Player) event.getPlayer());
                }
            }
        }
    }

}
