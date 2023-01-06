# BCFallback

BCFallback 是一个为 BungeeCord 编写的增强插件，原本只是为了在 BC 上实现当子服断线时自动传送玩家到其他子服的简单功能，因此而命名；

目前已适配 BungeeCord 1.19

### 功能：

 - 子服关机或离线时自动将玩家传送回大厅，避免玩家直接掉线（大厅通过配置文件指定）
 - 客户端连接域名验证，只允许使用插件配置的合法域名或IP才能连接 BungeeCord，阻挡直接的IP扫描探测

### 效果

禁止非法域名访问

子服被踢自动传回大厅



### TODO

 - 自动读取 BungeeCord config 中定义的子服信息
 - 增加插件指令功能，OP可以更改和热加载插件配置
 - 在BC上实现多子服间的聊天同步

### 感谢

   本插件是我初学 java 的第一个入门练手项目，感谢 bungeecord 的开发者提供了优秀的 API 才得以让本人的第一个 java 项目得以实现；

[BungeeCord API](https://github.com/SpigotMC/BungeeCord)

