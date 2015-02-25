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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class MenuFactory {

    private MenuFactory() {}

    public static Menu createMenu(String name, int size) {
        return Menu.createMenu(name, size);
    }

    public static Menu createMenu(String name, int size, Menu parent) {
        return createMenu(name, size).setParent(parent);
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

    public static void dispose(Menu menu) {
        HandlerList.unregisterAll(menu);
    }
}
