package me.naznamy.tab.bridge.fabric;

import me.neznamy.tab.bridge.shared.BridgePlayer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class FabricBridgePlayer extends BridgePlayer {
    public FabricBridgePlayer(ServerPlayer player) {
        super(player.getDisplayName().getString(), player.getUUID());
    }

    @Override
    public void sendPluginMessage(byte[] message) {

    }

    @Override
    public String getWorld() {
        return "";
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public boolean checkInvisibility() {
        return false;
    }

    @Override
    public boolean checkVanish() {
        return false;
    }

    @Override
    public boolean checkDisguised() {
        return false;
    }

    @Override
    public String checkGroup() {
        return "";
    }

    @Override
    public int checkGameMode() {
        return 0;
    }

    @Override
    public Object getPlayer() {
        return null;
    }
}
