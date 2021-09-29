package container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeContainerManager implements TimeUpdater, Runnable{
    private final ConcurrentHashMap<Long, TimeThread> container;
    private final long liveTime; //in millis

    public TimeContainerManager() {
        this(10000);
    }

    public TimeContainerManager(long liveTime) {
        this.liveTime = liveTime;
        this.container = new ConcurrentHashMap<>();
    }

    @Override
    public void update(long dt) {
        boolean anyoneDead = false;

        for (Map.Entry<Long, TimeThread> p : container.entrySet()) {
            TimeThread t = p.getValue();
            t.update(dt);

            if (!t.isAlive()) {
                dead(p.getKey());
                anyoneDead = true;
            }
        }

        if (anyoneDead)
            printAll();
    }

    @Override
    public void addOrReset(long key, String addr, long id) {
        if (container.containsKey(id)) {
            container.get(id).reset();
        } else {
            TimeThread t = new TimeThread(liveTime, addr);
            container.put(id, t);
            printAll();
        }
    }

    private void dead(long id) {
        container.remove(id);
    }

    private void printAll() {
        System.out.println("Updating");
        for (Map.Entry<Long, TimeThread> p : container.entrySet()) {
            System.out.println(p.getKey() + " " + p.getValue().toString());
        }
        System.out.println();
    }

    @Override
    public void run() {
        long prev = System.currentTimeMillis();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long curr = System.currentTimeMillis();
            update(curr - prev);
            prev = curr;
        }
    }
}
