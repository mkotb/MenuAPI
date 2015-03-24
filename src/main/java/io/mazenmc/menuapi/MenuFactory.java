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
package io.mazenmc.menuapi;

import io.mazenmc.menuapi.items.BasicItem;
import io.mazenmc.menuapi.items.Item;
import io.mazenmc.menuapi.items.ItemListener;
import io.mazenmc.menuapi.menu.Menu;
import io.mazenmc.menuapi.menu.MultiMenu;
import io.mazenmc.menuapi.menu.ScrollingMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class MenuFactory {
    public static final ItemListener EMPTY_LISTENER = (ItemListener) MenuFactory::doNothing;

    private MenuFactory() {}

    private static void doNothing(Player p, ClickType type) {}

    /**
     * Creates a menu with the inputted specifications
     * @param name Name of the menu
     * @param size Size of the menu
     * @return The generated menu
     */
    public static Menu createMenu(String name, int size) {
        return Menu.createMenu(name, size);
    }

    /**
     * Creates a menu with the inputted specifications
     * @param name Name of the menu
     * @param size Size of the menu
     * @param parent The parent of this menu, displayed to the user when it exits this one
     * @return The generated menu
     */
    public static Menu createMenu(String name, int size, Menu parent) {
        return createMenu(name, size).setParent(parent);
    }

    public static MultiMenu createMultiMenu(String name, int size) {
        return MultiMenu.create(name, size);
    }

    public static MultiMenu createMultiMenu(String name, int size, Menu parent) {
        return (MultiMenu) createMultiMenu(name, size).setParent(parent);
    }

    public static MultiMenu createMultiMenu(String name, int size, int pages) {
        return MultiMenu.create(name, size, pages);
    }

    public static MultiMenu createMultiMenu(String name, int size, int pages, Menu parent) {
        return (MultiMenu) createMultiMenu(name, size, pages).setParent(parent);
    }

    public static ScrollingMenu createScrollingMenu(String name) {
        return ScrollingMenu.create(name);
    }

    public static Item createItem(ItemStack stack, ItemListener listener) {
        return BasicItem.create(stack, listener);
    }

    public static Item createItem(ItemListener listener, Material material, String name, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.asList(lore));

        stack.setItemMeta(meta);
        return createItem(stack, listener);
    }

    /**
     * Disposes of the menu, reduces retention on the menu
     * @param menu The menu you wish to dispose
     */
    public static void dispose(Menu menu) {
        HandlerList.unregisterAll(menu);
    }
}
