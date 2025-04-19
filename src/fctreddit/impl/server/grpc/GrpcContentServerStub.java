package fctreddit.impl.server.grpc;

import fctreddit.api.java.Content;
import fctreddit.impl.grpc.generated_java.ContentGrpc;
import fctreddit.impl.grpc.generated_java.ContentProtoBuf;
import fctreddit.impl.server.java.JavaContent;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;

public class GrpcContentServerStub implements ContentGrpc.AsyncService, BindableService {

    Content impl = new JavaContent();

    @Override
    public final ServerServiceDefinition bindService() {
        return ContentGrpc.bindService(this);
    }

    public void createPost(ContentProtoBuf.CreatePostArgs request, StreamObserver<ContentProtoBuf.CreatePostResult> responseObserver) {

    }

    public void getPosts(ContentProtoBuf.GetPostsArgs request, StreamObserver<ContentProtoBuf.GetPostsResult> responseObserver) {

    }

    public void getPost (ContentProtoBuf.GetPostArgs request, StreamObserver<ContentProtoBuf.GrpcPost> responseObserver) {

    }

    public void getPostAnswers (ContentProtoBuf.GetPostAnswersArgs request, StreamObserver<ContentProtoBuf.GetPostsResult> responseObserver) {

    }

    public void updatePost (ContentProtoBuf.UpdatePostArgs request, StreamObserver<ContentProtoBuf.GrpcPost> responseObserver) {

    }

    public void deletePost (ContentProtoBuf.DeletePostArgs request, StreamObserver<ContentProtoBuf.EmptyMessage> responseObserver) {

    }

    public void upVotePost (ContentProtoBuf.ChangeVoteArgs request, StreamObserver<ContentProtoBuf.EmptyMessage> responseObserver) {

    }

    public void removeUpVotePost (ContentProtoBuf.ChangeVoteArgs request, StreamObserver<ContentProtoBuf.EmptyMessage> responseObserver) {

    }

    public void downVotePost (ContentProtoBuf.ChangeVoteArgs request, StreamObserver<ContentProtoBuf.EmptyMessage> responseObserver) {

    }

    public void removeDownVotePost (ContentProtoBuf.ChangeVoteArgs request, StreamObserver<ContentProtoBuf.EmptyMessage> responseObserver) {

    }

    public void getUpVotes (ContentProtoBuf.GetPostArgs request, StreamObserver<ContentProtoBuf.VoteCountResult> responseObserver) {

    }

    public void getDownVotes (ContentProtoBuf.GetPostArgs request, StreamObserver<ContentProtoBuf.VoteCountResult> responseObserver) {

    }
}




