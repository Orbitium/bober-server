package me.orbitium.pterodactylManager.api;

import me.orbitium.pterodactylManager.PterodactylManager;
import me.orbitium.pterodactylManager.utils.RequestResult;
import me.orbitium.pterodactylManager.utils.ServerStatus;

import java.util.List;

public class CommandSender {

    public RequestResult sendServerUpdate(ServerStatus newStatus) {
        String sender = PterodactylManager.instance.getSender();
        String command = String.format("""
                gamenetwork statusupdate %s %s
                 """, sender, newStatus.name()).replaceAll("\n", "");
        try {
            return PterodactylManager.instance.networkFileManager.sendCommand(command);
        } catch (Exception e) {
            PterodactylManager.prtInfo("An error occurred when sending server status update!");
            return RequestResult.ERROR;
        }
    }

    public RequestResult sendStatusInfo(GameServer gameServer, String targetServer) {
        String sender = PterodactylManager.instance.getSender();
        String currentStatus = gameServer.getStatus().name();
        String command = String.format("""
                gamenetwork statusinfo %s %s
                """, sender, currentStatus).replaceAll("\n", "");
        try {
            PterodactylFileManager networkFileManager = new PterodactylFileManager(PterodactylManager.instance.getApiKey(), targetServer);
            System.out.println(command);
            return networkFileManager.sendCommand(command);
        } catch (Exception e) {
            PterodactylManager.prtInfo("An error occurred when sending server status update!");
            return RequestResult.ERROR;
        }
    }

    public RequestResult requestStatusInfo(String targetServer) {
        String sender = PterodactylManager.instance.getSender();
        String command = String.format("""
                gamenetwork getstatus %s
                 """, sender).replaceAll("\n", "");
        try {
            PterodactylFileManager networkFileManager = new PterodactylFileManager(PterodactylManager.instance.getApiKey(), targetServer);
            return networkFileManager.sendCommand(command);
        } catch (Exception e) {
            PterodactylManager.prtInfo("An error occurred when sending server status update!");
            return RequestResult.ERROR;
        }
    }

    public RequestResult sendStartGame(String targetServer, List<String> players) {
        StringBuilder builder = new StringBuilder();
        for (String player : players)
            builder.append(player);

        String sender = PterodactylManager.instance.getSender();
        StringBuilder command = new StringBuilder("gamenetwork startgame ");
        for (String player : players)
            command.append(player).append(" ");

        try {
            PterodactylFileManager networkFileManager = new PterodactylFileManager(PterodactylManager.instance.getApiKey(), targetServer);
            return networkFileManager.sendCommand(command.toString());
        } catch (Exception e) {
            PterodactylManager.prtInfo("An error occurred when sending start game!");
            return RequestResult.ERROR;
        }
    }

}
