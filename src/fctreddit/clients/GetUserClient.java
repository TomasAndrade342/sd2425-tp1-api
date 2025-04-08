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

public class GetUserClient {

    private static Logger Log = Logger.getLogger(GetUserClient.class.getName());


    public static void main(String[] args) throws IOException {

        if( args.length != 4) {
            System.err.println( "Use: java " + GetUserClient.class.getCanonicalName() + " mcIP mcPort userId password");
            return;
        }
        String multiCastIp = args[0];
        int multiCastPort = Integer.valueOf(args[1]);
        String userId = args[2];
        String password = args[3];

        Discovery disc = new Discovery(new InetSocketAddress(multiCastIp, multiCastPort));
        disc.start();
        URI[] knownUris = disc.knownUrisOf("Users", 1);
        URI serverUrl = knownUris[0];

        UsersClient client = null;

        if(serverUrl.getPath().endsWith("rest"))
            client = new RestUsersClient(serverUrl);
        else
            client = new GrpcUsersClient( serverUrl);

        Result<User> result = client.getUser(userId, password);
        if( result.isOK()  )
            Log.info("Get user:" + result.value() );
        else
            Log.info("Get user failed with error: " + result.error());

    }

}
