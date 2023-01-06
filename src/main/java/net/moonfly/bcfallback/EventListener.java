package net.moonfly.bcfallback;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.Objects;

public class EventListener implements Listener {
    Plugin plugin;
    Config config;
    //获取配置文件中是否需要给玩家发送传送消息的选项是否为 true
    private boolean isNotify;
    private boolean isReson;
    private String tarGetSrvName; //获取目的子服
    private ServerInfo tSRVInfo;
    //定义传送后的消息内容
    private String msg;

    //获取checkDomain配置
    final boolean checkDomain;
    final List<?> domainList;
    private final String kickmsg;

    public EventListener(Plugin plugin) {
        this.plugin = plugin;
        this.config = new Config(plugin);
        this.isNotify = config.getKey("isNotify");
        this.isReson  = config.getKey("isReson");
        this.tarGetSrvName = config.getString("targetServer");
        this.tSRVInfo = ProxyServer.getInstance().getServerInfo(tarGetSrvName);
        this.checkDomain = config.getKey("checkDomain.enable");
        this.domainList = config.getList("checkDomain.domain");
        this.kickmsg  = config.getString("checkDomain.kickMsg");
    }

    @EventHandler
    //监听子服断线事件
    public void ServerKickEvent(ServerKickEvent event){
        String sRVInfo = event.getKickedFrom().getName();
        String Reason = BaseComponent.toLegacyText(event.getKickReasonComponent());
        ProxiedPlayer player = event.getPlayer();

        //判断玩家不是从fallback 子服被踢时，且客户端还在线时，才执行传送！
        // fix：客户端主动断线时导致BC空指针报错！
        if(!sRVInfo.equals(tarGetSrvName) && player.isConnected()){
            event.setCancelled(true); //阻断玩家客户端断开会话
            event.setCancelServer(tSRVInfo); //将玩家传送到设定过的大厅子服
            if (isNotify) {
                msg = ChatColor.GREEN + "[BCfallback] 已将您送回大厅！！";
                msg += "你已经从子服：[" + sRVInfo + "] 退出了！";
                if (isReson) {
                    msg += "原因：" + Reason;
                }
            }
            assert msg != null;
            event.getPlayer().sendMessage(ChatMessageType.SYSTEM, TextComponent.fromLegacyText(msg));
            event.getPlayer().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(msg));
        }
    }

    @EventHandler
    public void PlayerHandshakeEvent(PlayerHandshakeEvent handshake){
        //判断是否启用checkDomain
        if (checkDomain) {
            String address = handshake.getHandshake().getHost();
            String client = handshake.getConnection().getSocketAddress().toString().replace("/","");
            PendingConnection connection = handshake.getConnection();
            if (!domainList.contains(address)) {
                this.plugin.getLogger().warning("阻止连接,客户端IP:" + client + " 连接地址: " + address);
                connection.disconnect(new TextComponent(kickmsg));
            }
        }
    }
}