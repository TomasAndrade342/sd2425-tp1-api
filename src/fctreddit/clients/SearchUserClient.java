package fctreddit.clients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import fctreddit.api.Discovery;
import fctreddit.api.User;
import fctreddit.api.java.Result;
import fctreddit.clients.grpc.GrpcUsersClient;
import fctreddit.clients.java.UsersClient;
import fctreddit.clients.rest.RestUsersClient;
import jakarta.ws.rs.core.GenericType;


public class SearchUserClient {

    private static Logger Log = Logger.getLogger(SearchUserClient.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.println("Use: java " + SearchUserClient.class.getCanonicalName() + " mcIp mcPort query");
            return;
        }

        String multiCastIp = args[0];
        int multiCastPort = Integer.valueOf(args[1]);
        String query = args[2];

        Discovery disc = new Discovery(new InetSocketAddress(multiCastIp, multiCastPort));
        disc.start();
        URI[] knownUris = disc.knownUrisOf("serv", 1);

        URI serverUrl = knownUris[0];

        UsersClient client = null;

        if(serverUrl.getPath().endsWith("rest"))
            client = new RestUsersClient(serverUrl);
        else
            client = new GrpcUsersClient(serverUrl);

        Result<List<User>> result = client.searchUsers(query);
        if( result.isOK()  )
            Log.info("Success:" + result.value() );
        else
            Log.info("SearchUsers failed with error: " + result.error());
    }

}
