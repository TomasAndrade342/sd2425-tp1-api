package fctreddit.clients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Logger;

import fctreddit.api.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;
import fctreddit.clients.grpc.GrpcUsersClient;

public class CreateUserClient {

    private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());

    public static void main(String[] args) throws IOException {

        if( args.length != 6) {
            System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " mcIp mcPort userId fullName email password");
            return;
        }

        String multiCastIp = args[0];
        int multiCastPort = Integer.valueOf(args[1]);
        String userId = args[2];
        String fullName = args[3];
        String email = args[4];
        String password = args[5];

        Discovery disc = new Discovery(new InetSocketAddress(multiCastIp, multiCastPort));
        disc.start();
        URI[] knownUris = disc.knownUrisOf("Users", 1);
        System.out.println("Sending request to server.");
        URI serverUrl = knownUris[0];
        User usr = new User( userId, fullName, email, password);

        System.out.println("Sending request to server.");
        UsersClient client = null;

        if(serverUrl.getPath().endsWith("rest"))
            client = new RestUsersClient(serverUrl);
        else
            client = new GrpcUsersClient(serverUrl);

        Result<String> result = client.createUser( usr );
        if( result.isOK()  )
            Log.info("Created user:" + result.value() );
        else
            Log.info("Create user failed with error: " + result.error());

    }

}
