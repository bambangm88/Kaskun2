package com.example.kaskun.api;


import static com.example.kaskun.util.Utility.BASE_URL_API;

public class Server {
    public static ApiService getAPIService() {

        return Client.getClient(BASE_URL_API).create(ApiService.class);
    }

}
