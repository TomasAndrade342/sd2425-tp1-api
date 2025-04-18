package fctreddit.impl.server.rest;

import fctreddit.api.Post;
import fctreddit.api.java.Content;
import fctreddit.api.rest.RestContent;
import fctreddit.impl.server.java.JavaContent;

import java.util.List;
import java.util.logging.Logger;

public class ContentResource implements RestContent {

    private static Logger Log = Logger.getLogger(ContentResource.class.getName());

    final Content impl;

    public ContentResource() {
        impl = new JavaContent();
    }

    @Override
    public String createPost(Post post, String userPassword) {
        return "";
    }

    @Override
    public List<String> getPosts(long timestamp, String sortOrder) {
        return List.of();
    }

    @Override
    public Post getPost(String postId) {
        return null;
    }

    @Override
    public List<String> getPostAnswers(String postId, long timeout) {
        return List.of();
    }

    @Override
    public Post updatePost(String postId, String userPassword, Post post) {
        return null;
    }

    @Override
    public void deletePost(String postId, String userPassword) {

    }

    @Override
    public void upVotePost(String postId, String userId, String userPassword) {

    }

    @Override
    public void removeUpVotePost(String postId, String userId, String userPassword) {

    }

    @Override
    public void downVotePost(String postId, String userId, String userPassword) {

    }

    @Override
    public void removeDownVotePost(String postId, String userId, String userPassword) {

    }

    @Override
    public Integer getupVotes(String postId) {
        return 0;
    }

    @Override
    public Integer getDownVotes(String postId) {
        return 0;
    }
}
