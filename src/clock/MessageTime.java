package clock;

/**
 * A wrapper for bundling up an item and its integer priority.
 * 
 * @param <T>
 */
public class MessageTime<T> {

    private final T message;
    private final int time;

    public PriorityItem(T message, int time) {
        this.message = message;
        this.time = time;
    }

    public T getMessage() {
        return message;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "(" + getMessage() + ", " + getTime() + ")";
    }
}
