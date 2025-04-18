package fctreddit.impl.server.grpc;

import java.io.IOException;

import fctreddit.api.java.Image;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf;
import fctreddit.impl.server.java.JavaImage;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import fctreddit.api.java.Result;
import fctreddit.impl.grpc.generated_java.ImageGrpc;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf.DeleteImageArgs;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf.DeleteImageResult;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf.GetImageArgs;
import fctreddit.impl.grpc.generated_java.ImageProtoBuf.GetImageResult;


public class GrpcImageServerStub implements ImageGrpc.AsyncService, BindableService{

    Image impl = new JavaImage();

    @Override
    public final ServerServiceDefinition bindService() {
        return ImageGrpc.bindService(this);
    }

    @Override
    public void createImage(ImageProtoBuf.CreateImageArgs request, StreamObserver<ImageProtoBuf.CreateImageResult> responseObserver) {
        Result<String> res = null;
        try {
            res = impl.createImage(request.getUserId(), request.getImageContents().toByteArray(), request.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if( ! res.isOK() )
            responseObserver.onError(errorCodeToStatus(res.error()));
        else {
            responseObserver.onNext( ImageProtoBuf.CreateImageResult.newBuilder().setImageId( res.value() ).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getImage(GetImageArgs request, StreamObserver<GetImageResult> responseObserver) {
        Result<byte[]> res = impl.getImage(request.getUserId(), request.getImageId());
        if( ! res.isOK() )
            responseObserver.onError(errorCodeToStatus(res.error()));
        else {
            responseObserver.onNext( GetImageResult.newBuilder().setData(com.google.protobuf.ByteString.copyFrom(res.value())).build() );
            responseObserver.onCompleted();
        }

    }

    @Override
    public void deleteImage(DeleteImageArgs request, StreamObserver<DeleteImageResult> responseObserver) {
        ImageGrpc.AsyncService.super.deleteImage(request, responseObserver);
    }

    protected static Throwable errorCodeToStatus(Result.ErrorCode error ) {
        var status =  switch( error) {
            case NOT_FOUND -> io.grpc.Status.NOT_FOUND;
            case CONFLICT -> io.grpc.Status.ALREADY_EXISTS;
            case FORBIDDEN -> io.grpc.Status.PERMISSION_DENIED;
            case NOT_IMPLEMENTED -> io.grpc.Status.UNIMPLEMENTED;
            case BAD_REQUEST -> io.grpc.Status.INVALID_ARGUMENT;
            default -> io.grpc.Status.INTERNAL;
        };

        return status.asException();
    }
}
