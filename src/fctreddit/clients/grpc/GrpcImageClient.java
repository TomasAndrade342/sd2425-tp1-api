package fctreddit.clients.grpc;

import fctreddit.api.java.Result;
import fctreddit.clients.java.ImageClient;
import fctreddit.impl.grpc.generated_java.ImageGrpc;
import io.grpc.*;
import io.grpc.internal.PickFirstLoadBalancerProvider;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class GrpcImageClient extends ImageClient {


    static {
        LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
    }

    final ImageGrpc.ImageBlockingStub stub;

    public GrpcImageClient(URI serverURI) {
        Channel channel = ManagedChannelBuilder.forAddress(serverURI.getHost(), serverURI.getPort()).usePlaintext().build();
        stub = ImageGrpc.newBlockingStub(channel).withDeadlineAfter(READ_TIMEOUT, TimeUnit.MILLISECONDS);
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
