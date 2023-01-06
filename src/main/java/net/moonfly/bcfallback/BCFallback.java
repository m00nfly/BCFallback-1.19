package net.moonfly.bcfallback;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class BCFallback extends Plugin {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN+"=============================================");
        getLogger().info(ChatColor.GREEN+" 插件已启动! - Auth：m00nfly   Ver：1.0");
        getLogger().info(ChatColor.YELLOW+"一个BC增强插件，域名校验，子服断线自动传送大厅等");
        getProxy().getPluginManager().registerListener(this, new EventListener());
        getLogger().info(ChatColor.GREEN+"=============================================");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
