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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Menu with multiple sub menus, or "pages",
 *
 * This class extends Menu, moreover this instance is the main page or menu.
 * You can use the setMenu at index 0 to override this.
 */
public final class MultiMenu extends Menu {
    private static final Menu EMPTY_MENU = MenuFactory.createMenu("Empty Menu", 18);


    private ItemStack next = new ItemStack(Material.ARROW);
    private ItemStack back = new ItemStack(Material.BED);

    protected Menu[] menus;

    protected MultiMenu(String name, int size) {
        this(name, size, 5);
    }

    protected MultiMenu(String name, int size, int pages) {
        super(name, size);

        menus = new Menu[pages];

        menus[0] = this; // this menu is the front page
        setName(next, "&3Next");
        setName(back, "&3Back");

        for (int i = 1; i < pages; i++) {
            menus[i] = EMPTY_MENU;
        }
        
        updateMenus();
    }

    /**
     * Creates a MultiMenu with 5 empty menus/pages
     * @param name Name of the main menu
     * @param size Size of the main menu
     * @return Created menu
     */
    public static MultiMenu create(String name, int size) {
        return new MultiMenu(name, size);
    }

    /**
     * Create a MultiMenu with the following specifications
     * @param name Name of the main menu
     * @param size Size of the main menu
     * @param pages Amount of pages or menus in this MultiMenu, not graved in stone
     */
    public static MultiMenu create(String name, int size, int pages) {
        return new MultiMenu(name, size, pages);
    }

    static void setName(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        stack.setItemMeta(meta);
    }

    /**
     * Set menu at the specified index, can be over the size.
     * If the index is over the menu size, it will resize the menus array.
     *
     * @param index Index you wish to set the menu at
     * @param menu Menu you wish to set
     */
    public void setMenu(int index, Menu menu) {
        if (menus.length <= index && menus.length != (index - 1)) {
            resizeMenus(index - (menus.length - 1));
        }

        menus[index] = menu;
        updateMenus();

        if (menu != this) { // compare references
            menu.setParent(this);
        }
    }

    private void updateMenus() {
        for (int index = 0; index< menus.length; ++index) {
            Menu menu = menuAt(index);
            int indexSize = menu.size() - 1;

            Menu next = (menus.length == (index + 1)) ? menu : menus[index + 1];
            Menu back = (index == 0) ? menu : menus[index - 1];

            menu.setItem(indexSize, MenuFactory.createItem(this.next,
                    (p, c) -> next.showTo(p)));
            menu.setItem(indexSize - 8, MenuFactory.createItem(this.back,
                    (p, c) -> back.showTo(p)));
        }
    }

    public Menu menuAt(int index) {
        return menus[index];
    }

    public ItemStack nextItem() {
        return next;
    }

    public ItemStack backItem() {
        return back;
    }

    public void setNext(ItemStack next) {
        this.next = next;
    }

    public void setBack(ItemStack back) {
        this.back = back;
    }

    private void resizeMenus(int amount) {
        Menu[] menus = new Menu[(this.menus.length + amount)];

        System.arraycopy(this.menus, 0, menus, 0, this.menus.length);

        for (int i = this.menus.length; i < menus.length; i++) {
            menus[i] = EMPTY_MENU;
        }

        this.menus = menus;
    }
}
