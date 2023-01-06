package net.moonfly.bcfallback;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class EventListener implements Listener {
    Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin("BCFallback");
    config config = new net.moonfly.bcfallback.config();
    //获取配置文件中是否需要给玩家发送传送消息的选项是否为 true
    private boolean isNotify = config.getKey(plugin,"isNotify");
    private boolean isReson = config.getKey(plugin,"isReson");
    private String tarGetSrvName = config.getString(plugin,"targetServer"); //获取目的子服
    private String msg = null; //定义传送后的消息内容
    //获取checkDomain配置
    final boolean checkDomain = config.getKey(plugin,"checkDomain.enable");
    final List domainList = config.getList(plugin,"checkDomain.domain");
    String kickmsg = config.getString(plugin,"checkDomain.kickMsg");

    @EventHandler
    //监听子服断线事件
    public void ServerKickEvent(ServerKickEvent event){
        String sRVInfo = event.getKickedFrom().getName();
        String Reason = BaseComponent.toLegacyText(event.getKickReasonComponent());
        ServerInfo tSRVInfo = ProxyServer.getInstance().getServerInfo(tarGetSrvName);

        //判断玩家不是从fallback 子服被踢时，才执行传送
        if(!sRVInfo.equals(tarGetSrvName)){
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
                plugin.getLogger().warning("阻止连接,客户端IP:" + client + " 连接地址: " + address);
                connection.disconnect(new TextComponent(kickmsg));
            }
        }
    }
}