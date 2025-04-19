package fctreddit.impl.server.java;

import fctreddit.api.Post;
import fctreddit.api.User;
import fctreddit.api.java.Content;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ClientFactory;
import fctreddit.clients.java.UsersClient;
import fctreddit.impl.server.persistence.Hibernate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class JavaContent implements Content {

    private static Logger Log = Logger.getLogger(JavaContent.class.getName());
    private Hibernate hibernate;

    public JavaContent() {hibernate = Hibernate.getInstance();}

    @Override
    public Result<String> createPost(Post post, String userPassword) {
        UsersClient users = null;
        try {
            users = ClientFactory.getUsersClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (post.getAuthorId().isBlank() || post.getContent().isBlank()) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        Result<User> OGUser = users.getUser(post.getAuthorId(), userPassword);
        if (!OGUser.isOK()) {
            if (OGUser.error().equals(Result.ErrorCode.NOT_FOUND)) {
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
            else if (OGUser.error().equals(Result.ErrorCode.FORBIDDEN)) {
                return Result.error(Result.ErrorCode.FORBIDDEN);
            }
        }

        String parentUrl = post.getParentUrl();
        if (parentUrl != null) {
            String[] urlParts = parentUrl.split("/");
            String postId =  urlParts[urlParts.length-1];
            Result<Post> OGPost = getPost(postId);
            if (!OGPost.isOK()) {
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        }

        post.setCreationTimestamp(System.currentTimeMillis());
        UUID postId = UUID.randomUUID();
        post.setPostId(postId.toString());
        hibernate.persist(post);

        return Result.ok(postId.toString());
    }

    @Override
    public Result<List<String>> getPosts(long timestamp, String sortOrder) {
        return null;
    }

    @Override
    public Result<Post> getPost(String postId) {
        Post post = hibernate.get(Post.class, postId);
        if (post == null) {
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }
        return Result.ok(post);
    }

    @Override
    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        if (postId == null || !getPost(postId).isOK()) {
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }
        try {
            List<String> list = hibernate.jpql("SELECT p.postId FROM Post p WHERE p.parentUrl LIKE '%/" + postId + "' ORDER BY p.creationTimestamp ASC", String.class);
            return Result.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result<Post> updatePost(String postId, String userPassword, Post post) {
        return null;
    }

    @Override
    public Result<Void> deletePost(String postId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> upVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> downVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
        return null;
    }

    @Override
    public Result<Integer> getupVotes(String postId) {
        return null;
    }

    @Override
    public Result<Integer> getDownVotes(String postId) {
        return null;
    }
}
