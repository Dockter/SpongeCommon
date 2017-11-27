/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.item.inventory.adapter.impl;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.Lens;
import org.spongepowered.common.item.inventory.lens.SlotProvider;
import org.spongepowered.common.item.inventory.lens.impl.collections.SlotCollection;

import javax.annotation.Nullable;

/**
 * Basic Adapter for {@link IInventory} and {@link net.minecraft.inventory.Container}
 */
public class MinecraftAdapter extends AbstractInventoryAdapter<IInventory> {

    /**
     * Used to calculate {@link #getPlugin()}.
     */
    private final Container rootContainer;

    public MinecraftAdapter(Fabric<IInventory> inventory, net.minecraft.inventory.Container container) {
        super(inventory);
        this.rootContainer = (Container) container;
    }

    public MinecraftAdapter(Fabric<IInventory> inventory, @Nullable Lens<IInventory, ItemStack> root, @Nullable Inventory parent) {
        super(inventory, root, parent);
        this.rootContainer = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected SlotCollection<IInventory> initSlots(Fabric<IInventory> inventory, @Nullable Inventory parent) {
        if (parent instanceof MinecraftInventoryAdapter) {
            SlotProvider<IInventory, ItemStack> sp = ((MinecraftInventoryAdapter) parent).getSlotProvider();
            if (sp instanceof SlotCollection) {
                return ((SlotCollection) sp);
            }
        }
        return super.initSlots(inventory, parent);
    }

    @Override
    public PluginContainer getPlugin() {
        PluginContainer plugin = super.getPlugin();
        if (plugin != null) {
            return plugin;
        }
        if (this.rootContainer == null) {
            return null;
        }
        return this.rootContainer.getPlugin();
    }
}