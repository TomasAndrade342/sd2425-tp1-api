package fctreddit.clients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.api.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;


public class UpdateUserClient {

    private static Logger Log = Logger.getLogger(UpdateUserClient.class.getName());

    public static void main(String[] args) throws IOException {
    if (args.length != 7) {
            System.err.println(
                    "Use: java " + UpdateUserClient.class.getCanonicalName()
                            + " mcIp mcPort userId oldpwd fullName email password");
            return;
        }

        String multiCastIp = args[0];
        int multiCastPort = Integer.valueOf(args[1]);
        String userId = args[2];
        String oldpwd = args[3];
        String fullName = args[4];
        String email = args[5];
        String password = args[6];

        Discovery disc = new Discovery(new InetSocketAddress(multiCastIp, multiCastPort));
            disc.start();
        URI[] knownUris = disc.knownUrisOf("serv", 1);

        URI serverUrl = knownUris[0];

        User usr = new User(userId, fullName, email, password);

        UsersClient client = null;

        if(serverUrl.getPath().endsWith("rest"))
            client = new RestUsersClient(serverUrl);
        else
            client = new GrpcUsersClient(serverUrl);

        Result<User> result = client.updateUser(userId, password, usr);
        if( result.isOK()  )
            Log.info("Success:" + result.value() );
        else
            Log.info("Update user failed with error: " + result.error());
    }

}
