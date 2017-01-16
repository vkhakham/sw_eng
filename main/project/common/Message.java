package common;

import java.io.Serializable;

public class Message implements Serializable {
    public Uri uri;
    public Integer id;
    public Integer sessionId;
    public ClientType clientType;
    public Object data;
    public ErrorType error;

    public Message(Uri uri, Integer id, Integer sessionId, ClientType clientType, Object data) {
        this.uri = uri;
        this.data = data;
        this.id = id;
        this.sessionId = sessionId;
        this.clientType = clientType;
        this.error = ErrorType.Blank;
    }

    public Message clone() {
        return new Message(uri, id, sessionId, clientType, data);
    }
}
