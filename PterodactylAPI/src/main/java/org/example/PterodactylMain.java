package org.example;

import java.io.IOException;

public class PterodactylMain {

    private final String identifier;
    private final String lobbyServer;
    private final String apiKey;

    public PterodactylFileManager fileManager;

    public PterodactylMain(String identifier, String lobbyServer, String apiKey) {
        this.identifier = identifier;
        this.lobbyServer = lobbyServer;
        this.apiKey = apiKey;

//        fileManager = new PterodactylFileManager(apiKey, identifier, lobbyServer);
        fileManager = new PterodactylFileManager(apiKey, lobbyServer);
    }

    public void sendServerStatusUpdate(ServerStatus newStatus) {
        try {
            fileManager.sendCommand("gamenetwork updatestatus " + identifier + " " + newStatus.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
