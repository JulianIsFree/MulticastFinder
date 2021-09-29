import java.io.Serializable;

public class MyPacket implements Serializable {
    public final long id;
    MyPacket(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[MyPacket:" + id + "]";
    }
}
