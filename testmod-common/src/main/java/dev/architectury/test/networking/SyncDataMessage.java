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

package dev.architectury.test.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class SyncDataMessage extends BaseS2CMessage {
    private final CompoundTag serverData;
    
    /**
     * To send this message, call new SyncDataMessage(tag).sendToPlayer(player) / sendToAll(server) / etc.
     *
     * @see BaseS2CMessage
     */
    public SyncDataMessage(CompoundTag tag) {
        serverData = tag;
    }
    
    public SyncDataMessage(RegistryFriendlyByteBuf buf) {
        serverData = buf.readNbt();
    }
    
    @Override
    public MessageType getType() {
        return TestModNet.SYNC_DATA;
    }
    
    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(serverData);
    }
    
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.getPlayer().displayClientMessage(Component.literal("Received data from server: " + serverData), false);
    }
}