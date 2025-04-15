package fctreddit.api;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Discovery {
    private static Logger Log = Logger.getLogger(Discovery.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
    }

    static final public InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);
    static final int DISCOVERY_ANNOUNCE_PERIOD = 1000;
    static final int DISCOVERY_RETRY_TIMEOUT = 5000;
    static final int MAX_DATAGRAM_SIZE = 65536;

    private static final String DELIMITER = "\t";

    private final InetSocketAddress addr;
    private final String serviceName;
    private final String serviceURI;
    private final MulticastSocket ms;
    private volatile boolean running;

    private final Map<String, HashSet<URI>> serviceUriMap;

    public Discovery(InetSocketAddress addr, String serviceName, String serviceURI)
            throws SocketException, UnknownHostException, IOException {
        this.addr = addr;
        this.serviceName = serviceName;
        this.serviceURI = serviceURI;
        this.serviceUriMap = new ConcurrentHashMap<>();
        this.running = true;

        if (this.addr == null) {
            throw new RuntimeException("A multinet address has to be provided.");
        }

        this.ms = new MulticastSocket(addr.getPort());
        this.ms.joinGroup(addr, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public Discovery(InetSocketAddress addr) throws SocketException, UnknownHostException, IOException {
        this(addr, null, null);
    }

    public void start() {
        if (this.serviceName != null && this.serviceURI != null) {

            Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s", addr, serviceName,
                    serviceURI));

            byte[] announceBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();
            DatagramPacket announcePkt = new DatagramPacket(announceBytes, announceBytes.length, addr);

            try {
                new Thread(() -> {
                    while (running) {
                        try {
                            ms.send(announcePkt);
                            Thread.sleep(DISCOVERY_ANNOUNCE_PERIOD);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new Thread(() -> {
            DatagramPacket pkt = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);
            while (running) {
                try {
                    pkt.setLength(MAX_DATAGRAM_SIZE);
                    ms.receive(pkt);
                    String msg = new String(pkt.getData(), 0, pkt.getLength());
                    String[] msgElems = msg.split(DELIMITER);
                    if (msgElems.length == 2) { // periodic announcement
                        // System.out.printf("FROM %s (%s) : %s\n", pkt.getAddress().getHostName(),
                        // pkt.getAddress().getHostAddress(), msg);
                        String name = msgElems[0];
                        URI currUri = URI.create(msgElems[1]);
                        serviceUriMap.putIfAbsent(name, new HashSet<URI>());
                        serviceUriMap.get(name).add(currUri);
                    }
                } catch (IOException e) {
                    // do nothing
                }
            }
        }).start();
    }

    public URI[] knownUrisOf(String serviceName, int minReplies) {
        long startTime = System.currentTimeMillis();

        while (true) {
            long elapsedTime = System.currentTimeMillis() - startTime;

            int currReplies;
            if (serviceUriMap.get(serviceName) != null) {
                currReplies = serviceUriMap.get(serviceName).size();
            } else {
                currReplies = 0;
            }
            if (elapsedTime >= DISCOVERY_RETRY_TIMEOUT || currReplies >= minReplies) {
                this.running = false;
                return serviceUriMap.get(serviceName).toArray(new URI[0]);
            }
        }
    }

    /**public static void main(String[] args) throws Exception {
        Discovery discovery = new Discovery(DISCOVERY_ADDR, "test",
                "http://" + InetAddress.getLocalHost().getHostAddress());
        discovery.start();
    }*/
}