package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * GUI which shows the users all available schematics when resetting the Island.
 */
public class IslandRegenGUI extends SchematicGUI {

    private final @NotNull Player player;

    /**
     * The default constructor.
     *
     * @param player The player who wants to regen his Island
     */
    public IslandRegenGUI(@NotNull Player player) {
        this.player = player;
    }

    /**
     * Executed when the player selects the Island schematic.
     *
     * @param schematicConfig The data of the selected schematic
     */
    @Override
    public void selectSchematic(Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().regeneratingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            IridiumSkyblock.getInstance().getIslandManager().regenerateIsland(island.get(), user, schematicConfig);
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

}
