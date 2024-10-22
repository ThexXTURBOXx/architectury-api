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

package dev.architectury.mixin.forge;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel extends Level {
    protected MixinClientLevel(WritableLevelData arg, ResourceKey<Level> arg2, RegistryAccess arg3, Holder<DimensionType> arg4, boolean bl, boolean bl2, long l, int i) {
        super(arg, arg2, arg3, arg4, bl, bl2, l, i);
    }
    
    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void tickEntities(CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("architecturyClientLevelPreTick");
        ClientTickEvent.CLIENT_LEVEL_PRE.invoker().tick((ClientLevel) (Object) this);
        profiler.pop();
    }
    
    @Inject(method = "tickEntities", at = @At("RETURN"))
    private void tickEntitiesPost(CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("architecturyClientLevelPostTick");
        ClientTickEvent.CLIENT_LEVEL_POST.invoker().tick((ClientLevel) (Object) this);
        profiler.pop();
    }
}
