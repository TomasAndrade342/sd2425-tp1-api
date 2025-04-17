package fctreddit.impl.server.java;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.nio.file.Path;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.java.Image;
import fctreddit.clients.java.ClientFactory;
import fctreddit.clients.java.UsersClient;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class JavaImage implements Image {
    private static final String BASE_IMAGE_DIR = "images";
    private static final String IP_PORT = "http://localhost:8080/images/";

    public JavaImage() {}

    @Override
    public Result<String> createImage(String userId, byte[] imageContent, String password) throws IOException {
        UsersClient users = ClientFactory.getUsersClient();

        Result<User> OGUser = users.getUser(userId, password);
        if (!OGUser.isOK()) {
            return Result.error(OGUser.error());
        }

        if (imageContent == null || imageContent.length == 0) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        Path userDir = Paths.get(BASE_IMAGE_DIR, userId);
        Files.createDirectories(userDir);

        String fileName = UUID.randomUUID().toString();
        Path imagePath = userDir.resolve(fileName);

        Files.write(imagePath, imageContent);

        String imageURI = IP_PORT + userId + "/" + fileName;

        return Result.ok(imageURI);
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
