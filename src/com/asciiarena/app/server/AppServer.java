package com.asciiarena.app.server;

import com.asciiarena.lib.server.Server;
import com.asciiarena.lib.server.ServerConfig;

public class AppServer
{
    public static void main(String[] args) 
    {
        ServerConfig config = new ServerConfig();
        config.port = 3000;
        config.game.players = 4;
        config.game.pointsToWin = 1;
        config.game.map.width = 30;
        config.game.map.height = 30;
        config.game.map.seed = "";

        Server server = new Server(config);
        server.listen();
    }
}
