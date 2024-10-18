package me.orbitium.pterodactylManager.utils;

public enum RequestResult {
    OK,
    ERROR;
//    UNKNOWN_ADDRESS

    public static RequestResult parseResult(int statusCode) {
        switch (statusCode) {
            case 203:
            case 204:
                return RequestResult.OK;

            default:
                return RequestResult.ERROR;
        }
    }
}
