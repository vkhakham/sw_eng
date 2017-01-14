package common;

import java.io.Serializable;

public class Message implements Serializable {
    public final String uri;
    public final Object obj;

    public Message(String uri, Object obj) {
        this.uri = uri;
        this.obj = obj;
    }
}
