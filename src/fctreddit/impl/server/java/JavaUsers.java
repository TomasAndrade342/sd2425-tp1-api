package fctreddit.impl.server.java;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.api.java.Result.ErrorCode;
import fctreddit.api.java.Users;
import fctreddit.clients.Client;
import fctreddit.impl.server.persistence.Hibernate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class JavaUsers implements Users {

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());
    private Hibernate hibernate;

    public JavaUsers() {
        hibernate = Hibernate.getInstance();
    }

    @Override
    public Result<String> createUser(User user) {
        Log.info("createUser : " + user + '\n');

        if (user.getUserId() == null || user.getPassword() == null || user.getPassword().isEmpty() || user.getFullName() == null
                || user.getEmail() == null) {
            Log.info("User object invalid.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }
        try {
            hibernate.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
            Log.info("User already exists.");
            return Result.error(ErrorCode.CONFLICT);
        }
        return Result.ok(user.getUserId());
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        Log.info("getUser : user = " + userId + "; pwd = " + password + '\n');

        if (userId == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }
        User user = null;
        try {
            user = hibernate.get(User.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }
        if (user == null) {
            Log.info("User does not exist.");
            return Result.error(ErrorCode.NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect");
            return Result.error(ErrorCode.FORBIDDEN);
        }
        if (password == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }
        return Result.ok(user);
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        // TODO Auto-generated method stub
        Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; userData = " + user + '\n');

        /**if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null
                || user.getEmail() == null) {
            Log.info("User object invalid.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }*/
        if (userId == null) {
            Log.info("UserId or password null.");
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        User OGUser;
        try {
            OGUser = hibernate.get(User.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ErrorCode.INTERNAL_ERROR);
        }

        if (OGUser == null) {
            Log.info("User does not exist.");
            return Result.error(ErrorCode.NOT_FOUND);
        }
        if (!OGUser.getPassword().equals(password)) {
            Log.info("Password is incorrect");
            return Result.error(ErrorCode.FORBIDDEN);
        }

        if (user.getUserId() == null) {
            user.setUserId(OGUser.getUserId());
        }
        if (user.getPassword() == null) {
            user.setPassword(OGUser.getPassword());
        }
        if (user.getFullName() == null) {
            user.setFullName(OGUser.getFullName());
        }
        if (user.getEmail() == null) {
            user.setEmail(OGUser.getEmail());
        }
        if (user.getAvatarUrl() == null) {
            user.setAvatarUrl(OGUser.getAvatarUrl());
        }

        try {
            hibernate.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return Result.ok(user);
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        // TODO Auto-generated method stub
        Log.info("deleteUser : user = " + userId + "; pwd = " + password);

        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        var user = hibernate.get(User.class, userId);

        if (user == null) {
            Log.info("User does not exist.");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect.");
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        hibernate.delete(user);

        return Result.ok(user);
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        // TODO Auto-generated method stub
        Log.info("searchUsers : pattern = " + pattern + '\n');

        try {
            List<User> list = hibernate.jpql("SELECT u FROM User u WHERE u.userId LIKE '%" + pattern +"%'", User.class);
            return Result.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
