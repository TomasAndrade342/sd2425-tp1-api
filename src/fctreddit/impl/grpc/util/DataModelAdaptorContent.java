package fctreddit.impl.grpc.util;


import fctreddit.api.Post;
import fctreddit.impl.grpc.generated_java.ContentProtoBuf.GrpcPost;
import fctreddit.impl.grpc.generated_java.ContentProtoBuf.GrpcPost.Builder;

public class DataModelAdaptorContent {


    public static Post GrpcPost_to_Post(GrpcPost from) {
        return new Post(
                from.hasPostId() ? from.getPostId() : null,
                from.hasAuthorId() ? from.getAuthorId() : null,
                from.hasCreationTimestamp() ? from.getCreationTimestamp() : 0,
                from.hasContent() ? from.getContent() : null,
                from.hasMediaUrl() ? from.getMediaUrl() : null,
                from.hasParentUrl() ? from.getParentUrl() : null,
                from.hasUpVote() ? from.getUpVote() : 0,
                from.hasDownVote() ? from.getDownVote() : 0);
    }
    public static GrpcPost Post_to_GrpcPost(Post from )  {
        Builder c = GrpcPost.newBuilder();

        if(from.getPostId() != null)
            c.setPostId(from.getPostId());

        if(from.getAuthorId() != null)
            c.setAuthorId((from.getAuthorId()));

        c.setCreationTimestamp(from.getCreationTimestamp());

        if(from.getContent() != null)
            c.setContent(from.getContent());

        if(from.getMediaUrl() != null)
            c.setMediaUrl(from.getMediaUrl());

        if(from.getParentUrl() != null)
            c.setParentUrl(from.getParentUrl());

        c.setUpVote(from.getUpVote());

        c.setDownVote(from.getDownVote());

        return c.build();
    }

}