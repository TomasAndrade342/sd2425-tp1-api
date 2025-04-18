package fctreddit.clients.rest;

import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestUsers;
import fctreddit.clients.java.ContentClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class RestContentClient extends ContentClient {

    private static Logger Log = Logger.getLogger(RestContentClient.class.getName());

    final URI serverURI;
    final Client client;
    final ClientConfig config;

    final WebTarget target;

    public RestContentClient( URI serverURI ) {
        this.serverURI = serverURI;
        this.config = new ClientConfig();
        config.property( ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        this.client = ClientBuilder.newClient(config);
        target = client.target( serverURI ).path( RestUsers.PATH );
    }

    public Result<String> createPost(Post post, String userPassword) {
        return null;
    }

    public Result<List<String>> getPosts(long timestamp, String sortOrder) {
        return null;
    }

    public Result<Post> getPost(String postId) {
        return null;
    }

    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        return null;
    }

    public Result<Post> updatePost(String postId, String userPassword, Post post) {
        return null;
    }

    public Result<Void> deletePost(String postId, String userPassword) {
        return null;
    }

    public Result<Void> upVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    public Result<Void> downVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    public Result<Integer> getupVotes(String postId) {
        return null;
    }

    public Result<Integer> getDownVotes(String postId) {
        return null;
    }
}
