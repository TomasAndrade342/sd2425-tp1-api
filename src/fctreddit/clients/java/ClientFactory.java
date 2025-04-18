package fctreddit.clients.java;

import fctreddit.api.Discovery;
import fctreddit.clients.grpc.GrpcContentClient;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.rest.RestContentClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.clients.grpc.GrpcImageClient;
import fctreddit.clients.rest.RestImageClient;

import java.io.IOException;
import java.net.URI;

public class ClientFactory {
    private static final String USERS_SERVICE_NAME = "Users";
    private static final String IMAGE_SERVICE_NAME = "Image";
    private static final String CONTENT_SERVICE_NAME = "Content";

    private static URI getServerUri(String serviceName) throws IOException {
        Discovery d = new Discovery(Discovery.DISCOVERY_ADDR);
        d.start();
        URI[] knownUris = d.knownUrisOf(serviceName, 1);
        return knownUris[0];
    }

    public static UsersClient getUsersClient() throws IOException {
        URI serverUrl = getServerUri(USERS_SERVICE_NAME);
        if (serverUrl.getPath().endsWith("rest"))
            return new RestUsersClient(serverUrl);
        else
            return new GrpcUsersClient(serverUrl);
    }

    public static ImageClient getImageClient() throws IOException{
        URI serverUrl = getServerUri(IMAGE_SERVICE_NAME);
        if (serverUrl.getPath().endsWith("rest"))
            return new RestImageClient(serverUrl);
        else
            return new GrpcImageClient(serverUrl);
    }

    public static ContentClient getContentClient() throws IOException {
        URI serverUrl = getServerUri(CONTENT_SERVICE_NAME);
        if (serverUrl.getPath().endsWith("rest"))
            return new RestContentClient(serverUrl);
        else
            return new GrpcContentClient(serverUrl);
    }
}
