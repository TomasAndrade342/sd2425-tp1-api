package fctreddit.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Invocation.Builder;
import java.util.logging.Logger;

public class Client {

    private static Logger Log = Logger.getLogger(Client.class.getName());
    protected static final int MAX_RETRIES = 10;
    protected static final int RETRY_SLEEP = 5000;

    protected Response executeOperationPost(Builder req, Entity<?> e) {

        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                return req.post(e);
            } catch (ProcessingException x) {
                Log.info(x.getMessage());

                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException ex) {
                    //Nothing to be done here.
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationGet (Builder req){
        for (int i=0; i < MAX_RETRIES; i++){
            try {
                return req.get();
            } catch (ProcessingException x){
                Log.info(x.getMessage());

                try{
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e) {
                    //Nothing to be done here.
                }
                }catch (Exception x){
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationPut(Builder req, Entity<?> e) {

        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                return req.put(e);
            } catch (ProcessingException x) {
                Log.info(x.getMessage());

                try {
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException ex) {
                    //Nothing to be done here.
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return null;
    }

    protected Response executeOperationDelete (Builder req){
        for (int i=0; i < MAX_RETRIES; i++){
            try {
                return req.delete();
            } catch (ProcessingException x){
                Log.info(x.getMessage());

                try{
                    Thread.sleep(RETRY_SLEEP);
                } catch (InterruptedException e) {
                    //Nothing to be done here.
                }
            }catch (Exception x){
                x.printStackTrace();
            }
        }
        return null;
    }

}




