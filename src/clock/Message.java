package clock;

/**
 * Minimal "person" class.
 *
 * At the moment a Person object just holds their name, but in a more realistic
 * system, there would obviously be more.
 */
public class Message {

    protected String text;

    public Message(String text) {
        this.text = text;
    }

    public String getMessage() {
        return text;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
