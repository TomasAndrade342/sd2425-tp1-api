package fctreddit.impl.server.grpc;

import fctreddit.api.java.Content;
import fctreddit.impl.grpc.generated_java.ContentGrpc;
import fctreddit.impl.server.java.JavaContent;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;

public class GrpcContentServerStub implements ContentGrpc.AsyncService, BindableService {

    Content impl = new JavaContent();

    @Override
    public final ServerServiceDefinition bindService() {
        return ContentGrpc.bindService(this);
    }

}
