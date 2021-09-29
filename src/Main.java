import container.TimeContainerManager;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String multicastAddr;

        if (args.length >= 1) {
            multicastAddr = args[0];
        } else {
            multicastAddr = "224.0.0.1";
        }
        int port = 4003;
        long id = new Random(System.currentTimeMillis()).nextLong();
        System.out.println("Using multi-cast address: " + multicastAddr);
        System.out.println("id: " + id);

        TimeContainerManager manager = new TimeContainerManager();
        Writer writer = new Writer(multicastAddr, port, id);
        Reader reader = new Reader(multicastAddr, port, manager);
        new Thread(writer).start();
        new Thread(reader).start();
        new Thread(manager).start();
    }
}
