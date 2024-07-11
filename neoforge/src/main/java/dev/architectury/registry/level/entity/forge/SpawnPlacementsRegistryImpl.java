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

package dev.architectury.registry.level.entity.forge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpawnPlacementsRegistryImpl {
    private static List<Entry<?>> entries = new ArrayList<>();
    
    private record Entry<T extends Mob>(Supplier<? extends EntityType<T>> type, SpawnPlacementType spawnPlacement,
                                        Heightmap.Types heightmapType,
                                        SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
    }
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.<RegisterSpawnPlacementsEvent>addListener(event -> {
                for (Entry<?> entry : entries) {
                    Entry<Mob> casted = (Entry<Mob>) entry;
                    event.register(casted.type().get(), casted.spawnPlacement(), casted.heightmapType(), casted.spawnPredicate(), RegisterSpawnPlacementsEvent.Operation.OR);
                }
                entries = null;
            });
        });
    }
    
    public static <T extends Mob> void register(Supplier<? extends EntityType<T>> type, SpawnPlacementType spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        if (entries != null) {
            entries.add(new Entry<>(type, spawnPlacement, heightmapType, spawnPredicate));
        } else {
            throw new IllegalStateException("SpawnPlacementsRegistry.register must not be called after the registry has been collected!");
        }
    }
}
