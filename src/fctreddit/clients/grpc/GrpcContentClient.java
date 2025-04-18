package fctreddit.clients.grpc;

import fctreddit.api.Post;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.impl.grpc.generated_java.ContentGrpc;
import io.grpc.Channel;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.PickFirstLoadBalancerProvider;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcContentClient extends ContentClient {

    static {
        LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
    }

    final ContentGrpc.ContentBlockingStub stub;

    public GrpcContentClient(URI serverURI) {
        Channel channel = ManagedChannelBuilder.forAddress(serverURI.getHost(), serverURI.getPort()).usePlaintext().build();
        stub = ContentGrpc.newBlockingStub( channel ).withDeadlineAfter(READ_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Result<String> createPost(Post post, String userPassword) {
        return null;
    }

    @Override
    public Result<List<String>> getPosts(long timestamp, String sortOrder) {
        return null;
    }

    @Override
    public Result<Post> getPost(String postId) {
        return null;
    }

    @Override
    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        return null;
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
