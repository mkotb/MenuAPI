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
package io.mazenmc.menuapi.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class BasicItem implements Item {
    private ItemListener listener;
    private ItemStack stack;

    private BasicItem(ItemStack stack, ItemListener listener) {
        this.stack = stack;
        this.listener = listener;
    }

    public static BasicItem create(ItemStack stack, ItemListener listener) {
        return new BasicItem(stack, listener);
    }

    @Override
    public ItemStack stack() {
        return stack;
    }

    @Override
    public void act(Player player, ClickType clickType) {
        listener.act(player, clickType);
    }
}
