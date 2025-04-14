package fctreddit.impl.server.rest;

import java.util.logging.Logger;

import fctreddit.api.java.Image;
import fctreddit.api.rest.RestImage;
import fctreddit.impl.server.java.JavaImage;

public class ImageResource implements RestImage {

    private static Logger Log = Logger.getLogger(ImageResource.class.getName());

    final Image impl;

    public ImageResource() {
        impl = new JavaImage();
    }


    @Override
    public String createImage(String userId, byte[] imageContents, String password) {
        return "";
    }

    @Override
    public byte[] getImage(String userId, String imageId) {
        return new byte[0];
    }

    @Override
    public void deleteImage(String userId, String imageId, String password) {

    }
}
