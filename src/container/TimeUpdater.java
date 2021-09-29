package container;

public interface TimeUpdater {
    void update(long dt);
    void addOrReset(long key, String addr, long id);
}
