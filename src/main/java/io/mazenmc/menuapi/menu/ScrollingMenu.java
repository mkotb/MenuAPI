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

import io.mazenmc.menuapi.MenuFactory;
import io.mazenmc.menuapi.items.Item;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A menu that allows for scrolling.
 *
 * This class is designed to be put for a one viewers perspective,
 * meaning if there are multiple viewers one scroll will take place on all screens.
 *
 * Unlike other implementations, effects made to ScrollingMenu do not take place unless the flush method is called.
 *
 * When slots are set, your indexes are added by 10. Moreover, slot at (0,0) are at position (1,0) for the inventory or
 * slot at the index 0 are at the position 9.
 *
 * @see io.mazenmc.menuapi.menu.ScrollingMenu#flush()
 */
public class ScrollingMenu extends Menu {
    private static final ItemStack SCROLL = new ItemStack(Material.LADDER);

    private Map<Integer, Item> extendedMenu = new HashMap<>();
    private ItemStack panel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
    private int index = 0;

    protected ScrollingMenu(String name) {
        super(name, 54);
        flush();
    }

    public static ScrollingMenu create(String name) {
        return new ScrollingMenu(name);
    }

    private void updateButtons() {
        ItemStack scrollDown = SCROLL;
        boolean canScrollDown = canScroll(ScrollDirection.DOWN);

        MultiMenu.setName(scrollDown, canScrollDown ? "&6Scroll down" : "&8Cannot scroll down!");
        pushItem(0, 5, MenuFactory.createItem(scrollDown, (player, type) -> scrollDown(player)));

        ItemStack scrollUp = SCROLL;
        boolean canScrollUp = canScroll(ScrollDirection.UP);

        MultiMenu.setName(scrollUp, canScrollUp ? "&6Scroll up" : "&8Cannot scroll up!");
        pushItem(0, 0, MenuFactory.createItem(scrollUp, (player, type) -> scrollUp(player)));
    }

    private int highestIndex() {
        int highestIndex = 0;

        for (Map.Entry<Integer, Item> entry : extendedMenu.entrySet()) {
            int ind = entry.getKey();

            if (ind > highestIndex) {
                highestIndex = ind;
            }
        }

        return (int) Math.floor(highestIndex / 9);
    }

    public void setPanel(ItemStack panel) {
        this.panel = panel;
    }

    public void scrollDown(Player player) {
        if (!canScroll(ScrollDirection.DOWN)) {
            if (player != null) {
                player.playSound(player.getLocation(), Sound.CREEPER_HISS, 25, 50);
            }

            return;
        }

        ++index;
        flush();

        if (player != null) {
            player.playSound(player.getLocation(), Sound.CLICK, 50, 50);
        }
    }

    public void scrollDown() { //0c866fb1
        scrollDown(null);
    }

    public void scrollUp(Player player) {
        if (index == 0) {
            if (player != null) {
                player.playSound(player.getLocation(), Sound.CREEPER_HISS, 25, 50);
            }

            return;
        }

        --index;
        flush();

        if (player != null) {
            player.playSound(player.getLocation(), Sound.CLICK, 50, 50);
        }
    }

    public void scrollUp() {
        scrollUp(null);
    }

    public boolean canScroll(ScrollDirection direction) {
        switch (direction) {
            case UP:
                return index != 0;

            case DOWN:
                return highestIndex() != index;

            default:
                return false;
        }
    }

    public void flush() {
        for (int x = 0; x < 9; x++) {
            pushItem(x, 0, MenuFactory.createItem(panel, MenuFactory.EMPTY_LISTENER));
            pushItem(x, 5, MenuFactory.createItem(panel, MenuFactory.EMPTY_LISTENER));
        }

        for (int z = 1; z < 5; z++) {
            for (int x = 0; x < 9; x++) {
                pushItem(x, z, extendedMenu.get(((z - 1) * 9 + x) + index * 9));
            }
        }

        updateButtons();
    }

    @Override
    public Menu setItem(int index, Item item) {
        extendedMenu.put(index, item);
        return this;
    }

    private void pushItem(int index, Item item) {
        if (item == null) {
            inventory().setItem(index, null);
        } else {
            inventory().setItem(index, item.stack());
            items.put(index, item);
        }
    }

    private void pushItem(int x, int z, Item item) {
        pushItem(z * 9 + x, item);
    }

    public enum ScrollDirection {
        UP,
        DOWN
    }
}
