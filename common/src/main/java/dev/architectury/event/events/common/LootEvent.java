/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events related to loot tables and loot generation.
 */
public interface LootEvent {
    /**
     * An event to modify loot tables when they are loaded.
     * This can be used to add new drops via new loot pools to existing loot tables
     * without replacing the entire table.
     *
     * <h2>Built-in loot tables</h2>
     * <p>{@linkplain ModifyLootTable The event interface} includes a {@code builtin} parameter.
     * If it's {@code true}, the loot table is built-in to vanilla or a mod.
     * Otherwise, it's from a user data pack. The parameter can be used to only modify built-in loot tables
     * and let user-provided loot tables act as full "overwrites".
     *
     * <p>This event only runs for built-in loot tables on Forge due to the limitations of
     * {@code LootTableLoadEvent}.
     *
     * <h2>Example: adding diamonds as a drop for dirt</h2>
     * <pre>{@code
     * LootEvent.MODIFY_LOOT_TABLE.register((lootTables, id, context, builtin) -> {
     *     // Check that the loot table is dirt and built-in
     *     if (builtin && Blocks.DIRT.getLootTable().equals(id)) {
     *         // Create a loot pool with a single item entry of Items.DIAMOND
     *         LootPool.Builder pool = LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND));
     *         context.addPool(pool);
     *     }
     * });
     * }</pre>
     *
     * @see ModifyLootTable#modifyLootTable(ResourceKey, LootTableModificationContext, boolean)
     */
    Event<ModifyLootTable> MODIFY_LOOT_TABLE = EventFactory.createLoop();
    
    @FunctionalInterface
    interface ModifyLootTable {
        /**
         * Modifies a loot table.
         *
         * @param key     the loot table key
         * @param context the context used to modify the loot table
         * @param builtin if {@code true}, the loot table is built-in;
         *                if {@code false}, it is from a user data pack
         */
        void modifyLootTable(ResourceKey<LootTable> key, LootTableModificationContext context, boolean builtin);
    }
    
    /**
     * A platform-specific bridge for modifying a specific loot table.
     */
    @ApiStatus.NonExtendable
    interface LootTableModificationContext {
        /**
         * Adds a pool to the loot table.
         *
         * @param pool the pool to add
         */
        void addPool(LootPool.Builder pool);
    }
}
