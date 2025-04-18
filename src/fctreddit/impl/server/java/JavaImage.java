package fctreddit.impl.server.java;

import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Handler;
import java.nio.file.Path;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Image;
import fctreddit.clients.java.ClientFactory;
import fctreddit.clients.java.UsersClient;
import java.util.logging.*;
import fctreddit.impl.server.rest.ImageResource;

public class JavaImage implements Image {
    private static Logger Log = Logger.getLogger(JavaImage.class.getName());
    private static final String BASE_IMAGE_DIR = "images";
    private static final String IP_PORT;

    static {
        try {
            IP_PORT = "http://" + InetAddress.getLocalHost().getHostName() + ":8080/images/";
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

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
        Log.info("This is a debug message!\n");
        System.out.println("This is a debug message!\n");
        try {
            Path imagePath = Paths.get(BASE_IMAGE_DIR, userId, imageId);

            if (Files.notExists(imagePath)) {
                return Result.error(Result.ErrorCode.NOT_FOUND);
            }

            byte[] imageContent = Files.readAllBytes(imagePath);
            return Result.ok(imageContent);
        } catch (IOException e) {
            // Optional: Log the exception
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
    }

    /**
     * Deletes an image identified by imageId
     *
     * @param imageId the identifier of the image
     * @return 	<OK, Void> in the case of success.
     * 			NOT_FOUND if the image or user does not exists
     * 			FORBIDDEN if user password is incorrect
     * 		   	BAD_REQUEST password is null
     */
    @Override
    public Result<Void> deleteImage(String userId, String imageId, String password) {

        if (password == null) {
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
        UsersClient users;
        try {
            users = ClientFactory.getUsersClient();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(Result.ErrorCode.INTERNAL_ERROR);
        }
        Result<User> OGUser = users.getUser(userId, password);
        if (!OGUser.isOK()) {
            return Result.error(OGUser.error());
        }

        Path imagePath = Paths.get(BASE_IMAGE_DIR, userId, imageId);
        if (Files.notExists(imagePath)) {
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        try {
            Files.delete(imagePath);
            return Result.ok();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
