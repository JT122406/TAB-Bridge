package me.naznamy.tab.bridge.fabric;

import me.neznamy.tab.bridge.shared.BridgePlayer;
import me.neznamy.tab.bridge.shared.TABBridge;
import me.neznamy.tab.bridge.shared.message.outgoing.WorldChange;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class FabricBridge implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                TABBridge.getInstance().submitTask(() -> TABBridge.getInstance().getDataBridge().processQueue(handler.getPlayer())));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                TABBridge.getInstance().submitTask(() -> {
                    FabricBridgePlayer player = (FabricBridgePlayer) TABBridge.getInstance().getPlayer(handler.getPlayer().getUUID());
                    if (player != null)
                        TABBridge.getInstance().removePlayer(player);
                }));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((serverPlayer, origin, destination) -> {
            BridgePlayer p = TABBridge.getInstance().getPlayer(serverPlayer.getUUID());
            if (p == null) return;
            p.sendPluginMessage(new WorldChange(destination.toString()));
        }));
    }
}
