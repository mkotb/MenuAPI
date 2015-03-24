/*
 * Copyright (c) 2015, Mazen Kotb, email@mazenmc.io
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package io.mazenmc.menuapi.menu;

import io.mazenmc.menuapi.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Menu implements Listener {
    private static final JavaPlugin OWNER = JavaPlugin.getProvidingPlugin(Menu.class);

    private String name;
    private int size;
    private Inventory inventory;
    protected Map<Integer, Item> items = new HashMap<>(); // map for quick lookup
    private Menu parent;

    protected Menu(String name, int size) { // allow for sub classes
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.size = size;

        this.inventory = Bukkit.createInventory(null, size, this.name);
        Bukkit.getPluginManager().registerEvents(this, OWNER);
    }

    public static Menu createMenu(String name, int size) {
        return new Menu(name, size);
    }

    /**
     * The name of this menu, with translated color codes
     * @return The name
     */
    public String name() {
        return name;
    }

    /**
     * The size of this menu
     * @return The size of this menu
     */
    public int size() {
        return size;
    }

    /**
     * The inventory representation of this menu
     * @return The inventory representation
     */
    public Inventory inventory() {
        return inventory;
    }

    /**
     * Returns the item at specified index
     * @param index
     * @return Found item
     */
    public Item itemAt(int index) {
        return items.get(index);
    }

    /**
     * Returns the item at specified coordinates, where x is on the horizontal axis and z is on the vertical axis.
     * @param x The x coordinate
     * @param z The z coordinate
     * @return Found item
     */
    public Item itemAt(int x, int z) {
        return items.get(z * 9 + x);
    }

    /**
     * Sets the item at the specified index
     * @param index Index of the item you wish to set
     * @param item The item you wish to set the index as
     */
    public Menu setItem(int index, Item item) {
        if (item == null) {
            inventory.remove(index);
            System.out.println("removed item at "+ index);
        } else {
            inventory.setItem(index, item.stack());
            System.out.println("set item in inventory");
        }

        items.put(index, item);
        return this;
    }

    /**
     * Sets the item at the specified coordinates, where x is on the horizontal axis and z is on the vertical axis.
     * @param x The x coordinate
     * @param z The z coordinate
     * @param item The item you wish to set the index as
     */
    public Menu setItem(int x, int z, Item item) {
        return setItem(z * 9 + x, item);
    }

    /**
     * Sets the parent of the menu, used when the player exits the menu
     */
    public Menu setParent(Menu parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Show the menu to the inputted players
     * @param players The players you wish to show the menu too
     */
    public void showTo(Player... players) {
        for (Player p : players) {
            p.openInventory(inventory);
        }
    }

    @EventHandler
    public void onExit(InventoryCloseEvent event) {
        if(!event.getInventory().equals(inventory))
            return;

        if (parent != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().openInventory(parent.inventory);
                }
            }.runTaskLater(OWNER, 2L);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getInventory().equals(inventory))
            return;

        if(event.getRawSlot() >= size)
            return;

        event.setCancelled(true);

        if(!items.containsKey(event.getSlot())) {
            return;
        }

        items.get(event.getSlot()).act((Player) event.getWhoClicked(), event.getClick());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(!event.getInventory().equals(inventory))
            return;

        event.setCancelled(true);
    }
}
