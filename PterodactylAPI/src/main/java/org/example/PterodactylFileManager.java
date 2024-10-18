package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PterodactylFileManager {
    private final String apiUrl = "https://panel.bobermc.pl";
    private final String apiKey;
    private final String lobbyUUID;
    //    private final String senderUUID;
    private final HttpClient client;

//    public PterodactylFileManager(String apiKey, String senderUUID, String lobbyUUID) {
//        this.apiKey = apiKey;
//        this.senderUUID = senderUUID;
//        this.lobbyUUID = lobbyUUID;
//        this.client = HttpClient.newHttpClient();
//    }

    public PterodactylFileManager(String apiKey, String lobbyUUID) {
        this.apiKey = apiKey;
//        this.senderUUID = senderUUID;
        this.lobbyUUID = lobbyUUID;
        this.client = HttpClient.newHttpClient();
    }


    public int deleteFile(String filePath) throws IOException, InterruptedException {
        String jsonPayload = """
                {
                  "root": "/",
                  "files": [
                    "world"
                  ]
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/client/servers/" + lobbyUUID + "/files/delete"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete File Response: " + response.statusCode());

        return response.statusCode();
    }

    public int uncompressFile(String zipFilePath, String newName) throws IOException, InterruptedException {
        String jsonPayload = String.format("""
                {
                  "root": "/",
                  "file": "%s"
                }
                """, zipFilePath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/client/servers/" + lobbyUUID + "/files/decompress"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Uncompress File Response: " + response.statusCode());
        return response.statusCode();
    }

    public int closeServer() throws IOException, InterruptedException {
        return changeServerPower("stop");
    }

    public int openServer() throws IOException, InterruptedException {
        return changeServerPower("start");
    }

    private int changeServerPower(String action) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/client/servers/" + lobbyUUID + "/power"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"signal\": \"" + action + "\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(action + " Server Response: " + response.statusCode());

        return response.statusCode();
    }

    protected int sendCommand(String command) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/api/client/servers/" + lobbyUUID + "/command"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.format("{\"command\": \"%s\"}", command)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Send Command Response: " + response.statusCode());
        return response.statusCode();
    }
}
