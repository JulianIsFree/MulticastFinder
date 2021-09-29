package container;

public class TimeThread {
    private final long liveTime;
    private long timeLeft;

    private final String addr;

    public TimeThread(long liveTime, String addr) {
        this.liveTime = liveTime;
        this.timeLeft = liveTime;
        this.addr = addr;
    }

    synchronized void reset() {
        timeLeft = liveTime;
    }

    synchronized void update(float dt) {
        if (isAlive()) {
            timeLeft -= dt;
        }
    }

    boolean isAlive() {
        return timeLeft >= 0;
    }

    @Override
    public String toString() {
        return addr;
    }
}
