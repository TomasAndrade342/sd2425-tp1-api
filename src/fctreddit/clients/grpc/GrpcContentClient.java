package fctreddit.clients.grpc;

import fctreddit.api.Post;
import fctreddit.api.User;
import fctreddit.api.java.Content;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ContentClient;
import fctreddit.impl.grpc.generated_java.ContentGrpc;
import fctreddit.impl.grpc.generated_java.ContentProtoBuf;
import fctreddit.impl.grpc.generated_java.UsersProtoBuf;
import fctreddit.impl.grpc.util.DataModelAdaptor;
import fctreddit.impl.grpc.util.DataModelAdaptorContent;
import io.grpc.Channel;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.PickFirstLoadBalancerProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static fctreddit.clients.grpc.GrpcUsersClient.statusToErrorCode;

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
        try {
            ContentProtoBuf.CreatePostResult res = stub.createPost(ContentProtoBuf.CreatePostArgs.newBuilder()
                    .setPost(DataModelAdaptorContent.Post_to_GrpcPost(post))
                    .setPassword(userPassword)
                    .build());

            return Result.ok(res.getPostId());
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<List<String>> getPosts(long timestamp, String sortOrder) {

        try {
            ContentProtoBuf.GetPostsResult res = stub.getPosts(ContentProtoBuf.GetPostsArgs.newBuilder()
                            .setTimestamp(timestamp)
                            .setSortOrder(sortOrder)
                            .build()
            );

            List<String> posts = res.getPostIdList();

            return Result.ok(posts);
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Post> getPost(String postId) {
        try {

            ContentProtoBuf.GrpcPost res = stub.getPost(ContentProtoBuf.GetPostArgs.newBuilder()
                    .setPostId(postId)
                    .build());
            Post ret = DataModelAdaptorContent.GrpcPost_to_Post(res);

            return Result.ok(ret);
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<List<String>> getPostAnswers(String postId, long maxTimeout) {
        try {

            ContentProtoBuf.GetPostsResult res = stub.getPostAnswers(ContentProtoBuf.GetPostAnswersArgs.newBuilder()
                    .setPostId(postId)
                    .setTimeout(maxTimeout)
                    .build());

            List<String> posts = res.getPostIdList();

            return Result.ok(posts);
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Post> updatePost(String postId, String userPassword, Post post) {
        try {

            ContentProtoBuf.GrpcPost res = stub.updatePost(ContentProtoBuf.UpdatePostArgs.newBuilder()
                    .setPostId(postId).setPassword(userPassword)
                    .setPost(DataModelAdaptorContent.Post_to_GrpcPost(post))
                    .build());

            return Result.ok(DataModelAdaptorContent.GrpcPost_to_Post(res));
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> deletePost(String postId, String userPassword) {
        try {

            ContentProtoBuf.EmptyMessage res = stub.deletePost(ContentProtoBuf.DeletePostArgs.newBuilder()
                    .setPostId(postId).setPassword(userPassword)
                    .build());

            return Result.ok(null);
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> upVotePost(String postId, String userId, String userPassword) {
        try {

            ContentProtoBuf.EmptyMessage res = stub.upVotePost(ContentProtoBuf.ChangeVoteArgs.newBuilder()
                    .setPostId(postId)
                    .setUserId(userId).setPassword(userPassword)
                    .build());

            return Result.ok(null);
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> removeUpVotePost(String postId, String userId, String userPassword) {
        try{

            ContentProtoBuf.EmptyMessage res = stub.removeUpVotePost(ContentProtoBuf.ChangeVoteArgs.newBuilder()
                    .setPostId(postId)
                    .setUserId(userId).setPassword(userPassword)
                    .build());

            return Result.ok(null);
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> downVotePost(String postId, String userId, String userPassword) {
        try{
            ContentProtoBuf.EmptyMessage res = stub.downVotePost(ContentProtoBuf.ChangeVoteArgs.newBuilder()
                    .setPostId(postId)
                    .setUserId(userId).setPassword(userPassword)
                    .build());

            return Result.ok(null);
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> removeDownVotePost(String postId, String userId, String userPassword) {
        try{

            ContentProtoBuf.EmptyMessage res = stub.removeDownVotePost(ContentProtoBuf.ChangeVoteArgs.newBuilder()
                    .setPostId(postId)
                    .setUserId(userId).setPassword(userPassword)
                    .build());

            return Result.ok(null);
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Integer> getupVotes(String postId) {
        try{

            ContentProtoBuf.VoteCountResult res = stub.getupVotes(ContentProtoBuf.GetPostArgs.newBuilder()
                    .setPostId(postId)
                    .build());
            return Result.ok(res.getCount());
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Integer> getDownVotes(String postId) {
        try{

            ContentProtoBuf.VoteCountResult res = stub.getDownVotes(ContentProtoBuf.GetPostArgs.newBuilder()
                    .setPostId(postId)
                    .build());
            return Result.ok(res.getCount());
        }catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }
}
