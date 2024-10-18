package org.example;


import java.io.IOException;

public class Main {

    private static final String API_URL = "https://panel.bobermc.pl";
    private static final String API_KEY = "ptlc_jSl5gNYAsx7YHxXr61nSb1V9V77ms8U7of91N4gEm5x";  // API Key for Pterodactyl
    private static final String SERVER_ID = "07812fcb-5661-4ead-9e01-977f50ca9bbc";  // UUID of the server

    public static void main(String[] args) throws IOException, InterruptedException {
        PterodactylFileManager fileManager = new PterodactylFileManager(API_KEY, SERVER_ID);
        fileManager.closeServer();
        Thread.sleep(10000);
        fileManager.deleteFile("world");
        fileManager.uncompressFile("archive-2024-09-22T195442Z.tar.gz", "world");
        fileManager.openServer();
        Thread.sleep(10000);
        fileManager.sendCommand("say hotdogs");

//        fileManager.openServer();

//        String command = "stop";
//
//        // Create a map for the JSON payload
//        HashMap<String, String> commandPayload = new HashMap<>();
//        commandPayload.put("command", command);
//
//        // Convert the payload to JSON
//        Gson gson = new Gson();
//        String jsonPayload = gson.toJson(commandPayload);
//
//        // Create the HTTP request
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(API_URL + SERVER_ID + "/command"))
//                .header("Authorization", "Bearer " + API_KEY)
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.codePublishers.ofString(jsonPayload))
//                .build();
//
//        // Send the request
//        HttpClient client = HttpClient.newHttpClient();
//        HttpResponse<String> response = null;
//        try {
//            response = client.send(request, HttpResponse.codeHandlers.ofString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Print the response
//        System.out.println(response.code());
    }
}