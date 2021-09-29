import container.TimeContainerManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public class Reader implements Runnable {
    private final String multicastAddr;
    private final int port;
    private static final int maxSizeBytes = 4*1024;

    private TimeContainerManager timeContainer;

    Reader(String multicastAddr, int port, TimeContainerManager timeContainer) {
        this.multicastAddr = multicastAddr;
        this.port = port;

        this.timeContainer = timeContainer;
    }

    @Override
    public void run() {
        MulticastSocket socket;
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(new InetSocketAddress(InetAddress.getByName(multicastAddr), port), null);
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[maxSizeBytes], maxSizeBytes);
                socket.receive(datagramPacket);
                MyPacket myPacket = readObject(datagramPacket);
                timeContainer.addOrReset(myPacket.id, datagramPacket.getAddress().toString(), myPacket.id);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static MyPacket readObject(DatagramPacket p) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(p.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (MyPacket) ois.readObject();
    }
}
