package fctreddit.clients.grpc;

import com.google.protobuf.ByteString;
import fctreddit.api.java.Result;
import fctreddit.clients.java.ImageClient;
import fctreddit.impl.grpc.generated_java.ImageGrpc;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf;
import io.grpc.*;
import io.grpc.internal.PickFirstLoadBalancerProvider;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

import static fctreddit.clients.grpc.GrpcUsersClient.statusToErrorCode;

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
        try {
            ImageProtoBuf.CreateImageResult res = stub.createImage(ImageProtoBuf.CreateImageArgs.newBuilder()
                    .setUserId(userId)
                    .setImageContents(ByteString.copyFrom(imageContent))
                    .setPassword(password)
                    .build());

            return Result.ok(res.getImageId());
        } catch (StatusRuntimeException sre) {
            return Result.error( statusToErrorCode(sre.getStatus()));
        }
    }
    @Override
    public Result<byte[]> getImage (String userId, String imageId) {
        try {
            Iterator<ImageProtoBuf. GetImageResult> iterator = stub.getImage( ImageProtoBuf.GetImageArgs.newBuilder()
                    .setImageId(imageId)
                    .setUserId(userId) .build());
            if (iterator.hasNext()) {
                ImageProtoBuf.GetImageResult res = iterator.next();
                return Result.ok(res.getData().toByteArray());
            } else {
                return Result.error(Result. ErrorCode.NOT_FOUND);
            }
        } catch (StatusRuntimeException sre) {
            return Result.error(statusToErrorCode(sre.getStatus()));
        }
    }

    @Override
    public Result<Void> deleteImage(String userId, String imageId, String password) {

        return null;
    }
}
