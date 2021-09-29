import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public class Writer implements Runnable {
    private final String multicastAddr;
    private final int port;
    private final long id;
    private final long delay;

    Writer(String multicastAddr, int port, long id) {
        this(multicastAddr, port, id, 1000);
    }

    Writer(String multicastAddr, int port, long id, long delay) {
        this.multicastAddr = multicastAddr;
        this.port = port;
        this.id = id;
        this.delay = delay;
    }

    @Override
    public void run() {
        MulticastSocket socket;
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(new InetSocketAddress(InetAddress.getByName(multicastAddr), port), null);
            MyPacket myPacket = new MyPacket(id);
            while (true) {
                socket.send(writeDatagram(myPacket, multicastAddr, port));
                Thread.sleep(delay);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static DatagramPacket writeDatagram(Object o, String addr, int port) throws IOException {
        byte[] d = writeObject(o);
        return new DatagramPacket(d, d.length, InetAddress.getByName(addr), port);
    }

    private static byte[] writeObject(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }
}