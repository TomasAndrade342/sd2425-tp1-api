package fctreddit.impl.server.rest;

import java.util.Arrays;
import java.util.logging.Logger;

import fctreddit.api.User;
import fctreddit.api.java.Image;
import fctreddit.api.java.Result;
import fctreddit.api.rest.RestImage;
import fctreddit.impl.server.java.JavaImage;
import jakarta.ws.rs.WebApplicationException;

import static fctreddit.impl.server.rest.UsersResource.errorCodeToStatus;

public class ImageResource implements RestImage {

    private static Logger Log = Logger.getLogger(ImageResource.class.getName());

    final Image impl;

    public ImageResource() {
        impl = new JavaImage();
    }

    @Override
    public String createImage(String userId, byte[] imageContents, String password) {
        Log.info("createImage = " + userId + "imageContents = " + Arrays.toString(imageContents) + "; pwd = " + password);

        Result<String> res = impl.createImage(userId, imageContents, password);
        if(!res.isOK()) {
            throw new WebApplicationException(errorCodeToStatus(res.error()));
        }
        return res.value();
    }

    @Override
    public byte[] getImage(String userId, String imageId) {
        Log.info("getImage : Image = " + userId + "; imageId = " + imageId);

        Result<byte[]> res = impl.getImage(userId, imageId);
        if(!res.isOK()) {
            throw new WebApplicationException(errorCodeToStatus(res.error()));
        }
        return res.value();
    }

    @Override
    public void deleteImage(String userId, String imageId, String password) {
        Log.info("deleteImage : Image = " + userId + "imageId = " + imageId + "; pwd = " + password);

        Result<Void> res = impl.deleteImage(userId, imageId, password);

        if(!res.isOK()){
            throw new WebApplicationException(errorCodeToStatus(res.error()));
        }
    }
}
