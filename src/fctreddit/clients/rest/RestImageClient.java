package fctreddit.clients.rest;

import java.net.URI;
import java.util.logging.Logger;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestUsers;
import fctreddit.clients.java.ImageClient;

public class RestImageClient extends ImageClient {

    private static Logger Log = Logger.getLogger(RestUsersClient.class.getName());

    final URI serverURI;
    final Client client;
    final ClientConfig config;

    final WebTarget target;

    public RestImageClient( URI serverURI ) {

        this.serverURI = serverURI;
        this.config = new ClientConfig();
        config.property( ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        this.client = ClientBuilder.newClient(config);
        target = client.target( serverURI ).path( RestUsers.PATH );
    }
    @Override
    public Result<String> createImage(String userId, byte[] imageContent, String password) {
        return null;
    }

    @Override
    public Result<byte[]> getImage(String userId, String imageId) {
        return null;
    }

    @Override
    public Result<Void> deleteImage(String userId, String imageId, String password) {
        return null;
    }
}
