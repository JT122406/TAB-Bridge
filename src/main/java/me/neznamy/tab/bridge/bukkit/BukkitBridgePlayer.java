package me.neznamy.tab.bridge.bukkit;

import lombok.Getter;
import lombok.SneakyThrows;
import me.neznamy.tab.bridge.bukkit.nms.NMSStorage;
import me.neznamy.tab.bridge.bukkit.nms.PacketEntityView;
import me.neznamy.tab.bridge.shared.BridgePlayer;
import me.neznamy.tab.bridge.shared.TABBridge;
import me.neznamy.tab.bridge.shared.hook.LuckPermsHook;
import me.neznamy.tab.bridge.shared.util.ReflectionUtils;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

@Getter
public class BukkitBridgePlayer extends BridgePlayer {

    private static final boolean vault = Bukkit.getPluginManager().isPluginEnabled("Vault");
    private static final boolean libsDisguises = Bukkit.getPluginManager().isPluginEnabled("LibsDisguises");

    private final Player player;
    private final BukkitScoreboard scoreboard = new BukkitScoreboard(this);
    private final PacketEntityView entityView = new PacketEntityView(this);
    private final Set<String> channels;
    private Permission permission;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public BukkitBridgePlayer(Player player, int protocolVersion) {
        super(player.getName(), player.getUniqueId(), protocolVersion);
        this.player = player;
        channels = ((Set<String>) ReflectionUtils.getField(player.getClass(), "channels").get(player));
        if (vault) {
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
            if (rsp != null) permission = rsp.getProvider();
        }
    }

    @Override
    @SneakyThrows
    public void sendPluginMessage(byte[] message) {
        // 1.20.2 bug adding it with a significant delay, add ourselves to make it work
        // apparently it affects those players on older server version as well, so do this always
        channels.add(TABBridge.CHANNEL_NAME);
        player.sendPluginMessage(BukkitBridge.getInstance(), TABBridge.CHANNEL_NAME, message);
    }

    @Override
    public void sendPacket(Object packet) {
        if (NMSStorage.getInstance() == null) return;
        NMSStorage.getInstance().getPacketSender().sendPacket(player, packet);
    }

    @Override
    public String getWorld() {
        return player.getWorld().getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean checkInvisibility() {
        return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public boolean checkVanish() {
        return player.getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);
    }

    @Override
    public boolean checkDisguised() {
        if (libsDisguises) {
            try {
                return (boolean) Class.forName("me.libraryaddict.disguise.DisguiseAPI").getMethod("isDisguised", Entity.class).invoke(null, player);
            } catch (Throwable e) {
                //java.lang.NoClassDefFoundError: Could not initialize class me.libraryaddict.disguise.DisguiseAPI
            }
        }
        return false;
    }

    @Override
    public String checkGroup() {
        if (LuckPermsHook.getInstance().isInstalled()) {
            return LuckPermsHook.getInstance().getGroupFunction().apply(this);
        }
        if (vault) {
            if (permission == null || permission.getName().equals("SuperPerms")) {
                return "No permission plugin found";
            } else {
                return permission.getPrimaryGroup(player);
            }
        }
        return "Vault not found";
    }

    @Override
    @SuppressWarnings("deprecation")
    public int checkGameMode() {
        return player.getGameMode().getValue();
    }
}
